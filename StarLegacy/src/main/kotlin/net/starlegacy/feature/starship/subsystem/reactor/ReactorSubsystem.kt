package net.starlegacy.feature.starship.subsystem.reactor

import kotlin.math.min
import kotlin.math.roundToInt
import net.horizonsend.ion.QuickBalance.getBalancedValue
import net.kyori.adventure.text.Component.text
import net.starlegacy.feature.starship.active.ActivePlayerStarship
import net.starlegacy.feature.starship.active.ActiveStarship
import net.starlegacy.feature.starship.subsystem.StarshipSubsystem
import net.starlegacy.feature.starship.subsystem.shield.StarshipShields

class ReactorSubsystem(
	starship: ActiveStarship
) : StarshipSubsystem(starship, starship.centerOfMass) {
	val output: Double = Math.cbrt(
		starship.blockCount.coerceAtLeast(500).toDouble()
	) * 3000.0 * (if (starship.type.isWarship) 1.0 else 0.7)
	val powerDistributor = PowerDistributor()
	val weaponCapacitor = WeaponCapacitor(this)
	val heavyWeaponBooster = HeavyWeaponBooster(this)

	var shieldOverchargePoints = 0
	var weaponsOverchargePoints = 0
	var thrusterOverchargePoints = 0

	override fun isIntact(): Boolean {
		return true
	}

	fun tick(delta: Double) {
		shieldOverchargePoints += (powerDistributor.shieldPortion * 10).toInt() - 5
		weaponsOverchargePoints += (powerDistributor.weaponPortion * 10).toInt() - 5
		thrusterOverchargePoints += (powerDistributor.thrusterPortion * 10).toInt() - 5

		if (getBalancedValue("AllowPowerModeOvercharging") == 1.0)
			(starship as? ActivePlayerStarship)?.pilot?.sendMessage(text("$shieldOverchargePoints $weaponsOverchargePoints $thrusterOverchargePoints"))

		chargeShields(delta)
		weaponCapacitor.charge(delta)
	}

	private fun chargeShields(delta: Double) {
		val reactorOutput = this.output
		val shieldPortion = this.powerDistributor.shieldPortion
		val shieldEfficiency = starship.shieldEfficiency
		val shieldPower = reactorOutput * shieldPortion * shieldEfficiency * delta
		val totalMissing = starship.shields.sumOf { shield -> shield.maxPower - shield.power }

		if (totalMissing == 0) {
			return
		}

		for (shield in starship.shields) {
			val missing = shield.maxPower - shield.power

			if (missing == 0) {
				continue
			}

			val fraction = ((missing.toDouble() / totalMissing.toDouble()) * shieldPower).roundToInt()
			shield.power += min(missing, fraction)
		}

		if (starship is ActivePlayerStarship) {
			StarshipShields.updateShieldBars(starship)
		}
	}
}
