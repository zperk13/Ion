package net.starlegacy

//import net.starlegacy.feature.machine.BaseShields
import co.aikar.commands.BukkitCommandCompletionContext
import co.aikar.commands.BukkitCommandExecutionContext
import co.aikar.commands.InvalidCommandArgument
import co.aikar.commands.PaperCommandManager
import java.io.File
import net.horizonsend.ion.server.Ion
import net.starlegacy.cache.Caches
import net.starlegacy.cache.nations.NationCache
import net.starlegacy.cache.nations.PlayerCache
import net.starlegacy.cache.nations.SettlementCache
import net.starlegacy.cache.trade.EcoStations
import net.starlegacy.command.SLCommand
import net.starlegacy.command.economy.BazaarCommand
import net.starlegacy.command.economy.CityNpcCommand
import net.starlegacy.command.economy.CollectedItemCommand
import net.starlegacy.command.economy.CollectorCommand
import net.starlegacy.command.economy.EcoStationCommand
import net.starlegacy.command.misc.BatteryCommand
import net.starlegacy.command.misc.CustomItemCommand
import net.starlegacy.command.misc.DyeCommand
import net.starlegacy.command.misc.GToggleCommand
import net.starlegacy.command.misc.GlobalGameRuleCommand
import net.starlegacy.command.misc.PlanetSpawnMenuCommand
import net.starlegacy.command.misc.PlayerInfoCommand
import net.starlegacy.command.misc.SLTimeConvertCommand
import net.starlegacy.command.misc.ShuttleCommand
import net.starlegacy.command.misc.TransportDebugCommand
import net.starlegacy.command.nations.NationCommand
import net.starlegacy.command.nations.NationRelationCommand
import net.starlegacy.command.nations.NationSpaceStationCommand
import net.starlegacy.command.nations.SettlementCommand
import net.starlegacy.command.nations.SiegeCommand
import net.starlegacy.command.nations.admin.CityManageCommand
import net.starlegacy.command.nations.admin.NPCOwnerCommand
import net.starlegacy.command.nations.admin.NationAdminCommand
import net.starlegacy.command.nations.money.NationMoneyCommand
import net.starlegacy.command.nations.money.SettlementMoneyCommand
import net.starlegacy.command.nations.roles.NationRoleCommand
import net.starlegacy.command.nations.roles.SettlementRoleCommand
import net.starlegacy.command.nations.settlementZones.SettlementPlotCommand
import net.starlegacy.command.nations.settlementZones.SettlementZoneCommand
import net.starlegacy.command.space.PlanetCommand
import net.starlegacy.command.space.SpaceWorldCommand
import net.starlegacy.command.space.StarCommand
import net.starlegacy.command.starship.BlueprintCommand
import net.starlegacy.command.starship.MiscStarshipCommands
import net.starlegacy.command.starship.StarshipDebugCommand
import net.starlegacy.command.starship.StarshipInfoCommand
import net.starlegacy.database.MongoManager
import net.starlegacy.database.schema.economy.BazaarItem
import net.starlegacy.database.schema.economy.CityNPC
import net.starlegacy.database.schema.economy.CollectedItem
import net.starlegacy.database.schema.economy.EcoStation
import net.starlegacy.database.schema.misc.Shuttle
import net.starlegacy.database.schema.starships.Blueprint
import net.starlegacy.database.slPlayerId
import net.starlegacy.feature.chat.ChannelSelections
import net.starlegacy.feature.chat.ChatChannel
import net.starlegacy.feature.economy.bazaar.Bazaars
import net.starlegacy.feature.economy.bazaar.Merchants
import net.starlegacy.feature.economy.city.CityNPCs
import net.starlegacy.feature.economy.city.TradeCities
import net.starlegacy.feature.economy.collectors.CollectionMissions
import net.starlegacy.feature.economy.collectors.Collectors
import net.starlegacy.feature.gas.Gasses
import net.starlegacy.feature.gear.Gear
import net.starlegacy.feature.machine.Turrets
import net.starlegacy.feature.misc.AutoRestart
import net.starlegacy.feature.misc.CombatNPCs
import net.starlegacy.feature.misc.CryoPods
import net.starlegacy.feature.misc.CustomItem
import net.starlegacy.feature.misc.CustomItems
import net.starlegacy.feature.misc.CustomRecipes
import net.starlegacy.feature.misc.Decomposers
import net.starlegacy.feature.misc.GameplayTweaks
import net.starlegacy.feature.misc.PlanetSpawns
import net.starlegacy.feature.misc.Shuttles
import net.starlegacy.feature.multiblock.Multiblocks
import net.starlegacy.feature.nations.NationsBalancing
import net.starlegacy.feature.nations.NationsMap
import net.starlegacy.feature.nations.NationsMasterTasks
import net.starlegacy.feature.nations.StationSieges
import net.starlegacy.feature.nations.region.Regions
import net.starlegacy.feature.nations.region.types.RegionSettlementZone
import net.starlegacy.feature.nations.region.types.RegionTerritory
import net.starlegacy.feature.space.CachedPlanet
import net.starlegacy.feature.space.CachedStar
import net.starlegacy.feature.space.Orbits
import net.starlegacy.feature.space.Space
import net.starlegacy.feature.space.SpaceMap
import net.starlegacy.feature.space.SpaceMechanics
import net.starlegacy.feature.space.SpaceWorlds
import net.starlegacy.feature.starship.DeactivatedPlayerStarships
import net.starlegacy.feature.starship.Hangars
import net.starlegacy.feature.starship.Interdiction
import net.starlegacy.feature.starship.PilotedStarships
import net.starlegacy.feature.starship.StarshipComputers
import net.starlegacy.feature.starship.StarshipDealers
import net.starlegacy.feature.starship.StarshipDetection
import net.starlegacy.feature.starship.active.ActiveStarshipMechanics
import net.starlegacy.feature.starship.active.ActiveStarships
import net.starlegacy.feature.starship.control.ContactsDisplay
import net.starlegacy.feature.starship.control.StarshipControl
import net.starlegacy.feature.starship.control.StarshipCruising
import net.starlegacy.feature.starship.factory.StarshipFactories
import net.starlegacy.feature.starship.hyperspace.Hyperspace
import net.starlegacy.feature.starship.subsystem.shield.StarshipShields
import net.starlegacy.feature.transport.Extractors
import net.starlegacy.feature.transport.TransportConfig
import net.starlegacy.feature.transport.pipe.Pipes
import net.starlegacy.feature.transport.pipe.filter.Filters
import net.starlegacy.listener.SLEventListener
import net.starlegacy.listener.gear.BlasterListener
import net.starlegacy.listener.gear.DetonatorListener
import net.starlegacy.listener.gear.DoubleJumpListener
import net.starlegacy.listener.gear.PowerArmorListener
import net.starlegacy.listener.gear.PowerToolListener
import net.starlegacy.listener.gear.SwordListener
import net.starlegacy.listener.misc.BlockListener
import net.starlegacy.listener.misc.ChatListener
import net.starlegacy.listener.misc.EntityListener
import net.starlegacy.listener.misc.FurnaceListener
import net.starlegacy.listener.misc.InteractListener
import net.starlegacy.listener.misc.InventoryListener
import net.starlegacy.listener.misc.JoinLeaveListener
import net.starlegacy.listener.misc.ProtectionListener
import net.starlegacy.listener.nations.FriendlyFireListener
import net.starlegacy.listener.nations.MovementListener
import net.starlegacy.util.MATERIALS
import net.starlegacy.util.Notify
import net.starlegacy.util.Tasks
import net.starlegacy.util.orNull
import net.starlegacy.util.redisaction.RedisActions
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.litote.kmongo.and
import org.litote.kmongo.eq
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

internal val PLUGIN: StarLegacy get() = StarLegacy.PLUGIN
internal lateinit var SETTINGS: Config

class StarLegacy : JavaPlugin() {
	lateinit var redisPool: JedisPool

	init {
		PLUGIN = this
	}

	companion object {
		lateinit var PLUGIN: StarLegacy
		var INITIALIZATION_COMPLETE: Boolean = false
			private set
	}

	lateinit var manager: PaperCommandManager

	fun namespacedKey(key: String) = NamespacedKey(this, key)

	/**
	 * Shared folder defined in config for cross-server config files
	 */
	val sharedDataFolder by lazy { File(SETTINGS.sharedFolder).apply { mkdirs() } }

	// put the get() so the classes aren't initialized right away
	private val components: List<SLComponent>
		get() = listOf(
			RedisActions,
			AutoRestart,
			Caches,
			Notify,
			Shuttles,

			ChannelSelections,
			ChatChannel.ChannelActions,

			CombatNPCs,

			CryoPods,
			CustomRecipes,
			GameplayTweaks,

			SpaceWorlds,
			Space,
			SpaceMap,
			Orbits,

			SpaceMechanics,

			NationsBalancing,
			Regions,
			NationsMap,

			StationSieges,

			Multiblocks,
			Gasses,

			TransportConfig.Companion,
			Extractors,
			Pipes,
			Filters,

			Gear,

			TradeCities,

			CollectionMissions,
			Collectors,

			CityNPCs,

			Bazaars,
			Merchants,

			PlanetSpawns,

			DeactivatedPlayerStarships,
			ActiveStarships,
			ActiveStarshipMechanics,
			PilotedStarships,
			StarshipDetection,
			StarshipComputers,
			StarshipControl,
			StarshipShields,
			StarshipCruising,
			ContactsDisplay,
			Hangars,
			Hyperspace,
			Turrets,
			StarshipFactories,
			Interdiction,
			StarshipDealers,
			Decomposers
		)

	// put the get() so the classes aren't initialized right away
	private val listeners: List<SLEventListener>
		get() = listOf(
			JoinLeaveListener,
			ChatListener,
			MovementListener,
			FriendlyFireListener,
			ProtectionListener,

			BlockListener,
			EntityListener,
			FurnaceListener,
			InteractListener,
			InventoryListener,

			BlasterListener,
			DetonatorListener,
			DoubleJumpListener,
			PowerArmorListener,
			PowerToolListener,
			SwordListener,
		)

	override fun onEnable() {
		manager = PaperCommandManager(this)
		Ion().onEnable()

		saveDefaultConfig()
		reloadConfig()

		SETTINGS = Config()
		SETTINGS.mongo = Config.Mongo(
			config.getString("host")!!,
			config.getInt("port"),
			config.getString("database")!!,
			config.getString("username")!!,
			config.getString("password")!!
		)

			//loadConfig(dataFolder, "config")

		// manually call this for MongoManager, as some of the components break if it's not ready on init
		MongoManager.onEnable()

		System.setProperty("https.protocols", "TLSv1.1,TLSv1.2") // java doesn't do https very well by default...

		enableRedis()

		for (component in components) {
			if (SETTINGS.vanilla && !component.supportsVanilla()) {
				continue
			}

			component.onEnable()
			server.pluginManager.registerEvents(component, this)
		}

		registerListeners()

		registerCommands()

		if (isMaster()) {
			// 20 ticks * 60 = 1 minute, 20 ticks * 60 * 60 = 1 hour
			Tasks.asyncRepeat(20 * 60, 20 * 60 * 60) {
				NationsMasterTasks.executeAll()
			}
		}

		INITIALIZATION_COMPLETE = true
	}

	private fun registerListeners() {
		for (listener in listeners) {
			if (SETTINGS.vanilla && !listener.supportsVanilla()) {
				continue
			}

			listener.register()
		}
	}

	private val commands
		get() = listOf(
			GToggleCommand,
			PlayerInfoCommand,
			DyeCommand,
			GlobalGameRuleCommand,

			BatteryCommand,
			CustomItemCommand,
			TransportDebugCommand,
			PlanetSpawnMenuCommand,
			SLTimeConvertCommand,
			ShuttleCommand,

			SettlementCommand,
			NationCommand,
			NationSpaceStationCommand,
			NationRelationCommand,

			CityManageCommand,
			NationAdminCommand,
			NPCOwnerCommand,

			NationMoneyCommand,
			SettlementMoneyCommand,

			NationRoleCommand,
			SettlementRoleCommand,

			SettlementPlotCommand,
			SettlementZoneCommand,

			SiegeCommand,

			PlanetCommand,
			SpaceWorldCommand,
			StarCommand,

			BazaarCommand,
			CityNpcCommand,
			CollectedItemCommand,
			CollectorCommand,
			EcoStationCommand,

			MiscStarshipCommands,
			BlueprintCommand,
			StarshipDebugCommand,
			StarshipInfoCommand
		)

	private fun registerCommands() {
		@Suppress("DEPRECATION")
		manager.enableUnstableAPI("help")

		// Add contexts
		manager.commandContexts.run {
			registerContext(CustomItem::class.java) { c: BukkitCommandExecutionContext ->
				val arg = c.popFirstArg()
				return@registerContext CustomItems[arg]
					?: throw InvalidCommandArgument("No custom item $arg found!")
			}

			registerContext(RegionSettlementZone::class.java) { c: BukkitCommandExecutionContext ->
				val arg = c.popFirstArg() ?: throw InvalidCommandArgument("Zone is required")
				return@registerContext Regions.getAllOf<RegionSettlementZone>().firstOrNull { it.name == arg }
					?: throw InvalidCommandArgument("Zone $arg not found")
			}

			registerContext(CachedStar::class.java) { c: BukkitCommandExecutionContext ->
				Space.starNameCache[c.popFirstArg().uppercase()].orNull()
					?: throw InvalidCommandArgument("No such star")
			}

			registerContext(CachedPlanet::class.java) { c: BukkitCommandExecutionContext ->
				Space.planetNameCache[c.popFirstArg().uppercase()].orNull()
					?: throw InvalidCommandArgument("No such planet")
			}

			registerContext(EcoStation::class.java) { c: BukkitCommandExecutionContext ->
				val name: String = c.popFirstArg()

				return@registerContext EcoStations.getByName(name)
					?: throw InvalidCommandArgument("Eco station $name not found")
			}
		}

		// Add static tab completions
		mapOf(
			"customitems" to CustomItems.all().joinToString("|") { it.id },
			"npctypes" to CityNPC.Type.values().joinToString("|") { it.name }
		).forEach { manager.commandCompletions.registerStaticCompletion(it.key, it.value) }

		// Add async tab completions
		@Suppress("RedundantLambdaArrow")
		mapOf<String, (BukkitCommandCompletionContext) -> List<String>>(
			"gamerules" to { _ -> Bukkit.getWorlds().first().gameRules.toList() },
			"settlements" to { _ -> SettlementCache.all().map { it.name } },
			"member_settlements" to { c ->
				val player = c.player ?: throw InvalidCommandArgument("Players only")
				val nation = PlayerCache[player].nation

				SettlementCache.all().filter { nation != null && it.nation == nation }.map { it.name }
			},
			"nations" to { _ -> NationCache.all().map { it.name } },
			"zones" to { c ->
				val player = c.player ?: throw InvalidCommandArgument("Players only")
				val settlement = PlayerCache[player].settlement

				Regions.getAllOf<RegionSettlementZone>()
					.filter { settlement != null && it.settlement == settlement }
					.map { it.name }
			},
			"plots" to { c ->
				val player = c.player ?: throw InvalidCommandArgument("Players only")
				val slPlayerId = player.slPlayerId

				Regions.getAllOf<RegionSettlementZone>()
					.filter { it.owner == slPlayerId }
					.map { it.name }
			},
			"outposts" to { c ->
				val player = c.player ?: throw InvalidCommandArgument("Players only")
				val nation = PlayerCache[player].nation
				Regions.getAllOf<RegionTerritory>().filter { it.nation == nation }.map { it.name }
			},
			"stars" to { _ -> Space.getStars().map(CachedStar::name) },
			"planets" to { _ -> Space.getPlanets().map(CachedPlanet::name) },
			"materials" to { _ -> MATERIALS.map { it.name } },
			"cities" to { _ -> TradeCities.getAll().map { it.displayName } },
			"collecteditems" to { _ -> CollectedItem.all().map { "${EcoStations[it.station].name}.${it.itemString}" } },
			"ecostations" to { _ -> EcoStations.getAll().map { it.name } },
			"shuttles" to { _ -> Shuttle.all().map { it.name } },
			"shuttleSchematics" to { _ -> Shuttles.getAllSchematics() },
			"bazaarItemStrings" to { c ->
				val player = c.player ?: throw InvalidCommandArgument("Players only")
				val slPlayerId = player.slPlayerId
				val territory = Regions.findFirstOf<RegionTerritory>(player.location)
					?: throw InvalidCommandArgument("You're not in a territory!")
				BazaarItem.findProp(
					and(BazaarItem::seller eq slPlayerId, BazaarItem::cityTerritory eq territory.id),
					BazaarItem::itemString
				).toList()
			},
			"blueprints" to { c ->
				val player = c.player ?: throw InvalidCommandArgument("Players only")
				val slPlayerId = player.slPlayerId
				Blueprint.col.find(Blueprint::owner eq slPlayerId).map { it.name }.toList()
			}
		).forEach { manager.commandCompletions.registerAsyncCompletion(it.key, it.value) }

		// Register commands
		for (command in commands) {
			if (SETTINGS.vanilla && !command.supportsVanilla()) {
				continue
			}

			manager.registerCommand(command)
		}
	}

	private fun enableRedis() {
		redisPool = JedisPool(JedisPoolConfig(), SETTINGS.redis.host)
	}

	override fun onDisable() {
		SLCommand.ASYNC_COMMAND_THREAD.shutdown()

		for (component in components.asReversed()) {
			if (SETTINGS.vanilla && !component.supportsVanilla()) {
				continue
			}

			try {
				component.onDisable()
			} catch (e: Exception) {
				e.printStackTrace()
				continue
			}
		}
		redisPool.close()

		// manually disable here since it's not a listed component for the same reason it's manual onEnable
		MongoManager.onDisable()
	}

	inline fun <reified T : Event> listen(
		priority: EventPriority = EventPriority.NORMAL,
		ignoreCancelled: Boolean = false,
		noinline block: (T) -> Unit
	): Unit = listen<T>(priority, ignoreCancelled) { _, event -> block(event) }

	inline fun <reified T : Event> listen(
		priority: EventPriority = EventPriority.NORMAL,
		ignoreCancelled: Boolean = false,
		noinline block: (Listener, T) -> Unit
	) {
		server.pluginManager.registerEvent(
			T::class.java,
			object : Listener {},
			priority,
			{ listener, event -> block(listener, event as? T ?: return@registerEvent) },
			this,
			ignoreCancelled
		)
	}

	private fun isMaster(): Boolean = SETTINGS.master
}

fun <T> redis(block: Jedis.() -> T): T = PLUGIN.redisPool.resource.use(block)
