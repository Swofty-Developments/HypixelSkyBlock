package net.swofty.type.bedwarsgame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.hollowcube.polar.PolarLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.GameHeartbeatProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.pvp.MinestomPvP;
import net.swofty.pvp.feature.CombatFeatureSet;
import net.swofty.pvp.feature.CombatFeatures;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.utils.CombatVersion;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.ShopManager;
import net.swofty.type.bedwarsgame.shop.TeamShopManager;
import net.swofty.type.bedwarsgame.shop.TrapManager;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItemHandler;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.redis.RedisOriginServer;
import net.swofty.type.generic.tab.EmptyTabModule;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static net.swofty.pvp.feature.CombatFeatures.*;
import static net.swofty.type.generic.HypixelGenericLoader.getLoadedPlayers;

public class TypeBedWarsGameLoader implements HypixelTypeLoader {

	public static final int MAX_GAMES = 8;

	@Getter
	public static final List<Game> games = new ArrayList<>();

	@Getter
	public static final ShopManager shopManager = new ShopManager();
	@Getter
	public static final TeamShopManager teamShopManager = new TeamShopManager();
	@Getter
	public static final TrapManager trapManager = new TrapManager();
	@Getter
	public static final SimpleInteractableItemHandler itemHandler = new SimpleInteractableItemHandler();

	public static final Tag<@NotNull Boolean> PLAYER_PLACED_TAG = Tag.Boolean("player_placed");
	public static final Tag<@NotNull Integer> ARMOR_LEVEL_TAG = Tag.Integer("armor_level");

	static CombatFeatureSet combatFeatures = CombatFeatures.empty().version(CombatVersion.LEGACY).addAll(List.of(
			VANILLA_ARMOR, VANILLA_ATTACK, VANILLA_CRITICAL, //VANILLA_SWEEPING,
			VANILLA_EQUIPMENT, VANILLA_BLOCK, VANILLA_ATTACK_COOLDOWN, VANILLA_ITEM_COOLDOWN,
			VANILLA_DAMAGE, VANILLA_EFFECT, VANILLA_ENCHANTMENT, VANILLA_EXPLOSION,
			VANILLA_EXPLOSIVE, VANILLA_FALL, VANILLA_FOOD, LEGACY_VANILLA_BLOCK,
			VANILLA_REGENERATION, VANILLA_KNOCKBACK, VANILLA_POTION, //VANILLA_BOW,
			VANILLA_CROSSBOW, VANILLA_FISHING_ROD, VANILLA_MISC_PROJECTILE,
			VANILLA_PROJECTILE_ITEM, VANILLA_TRIDENT, VANILLA_SPECTATE,
			VANILLA_PLAYER_STATE, VANILLA_TOTEM//, VANILLA_DEATH_MESSAGE
	)).build();

	@Getter
	private static BedWarsMapsConfig mapsConfig;
	private static InstanceManager instanceManager;
	private static RegistryKey<@NotNull DimensionType> fullbrightDimension;
	private static int nextMapIndex = 0;
	private static List<BedWarsMapsConfig.MapEntry> filteredMaps = new ArrayList<>();
	private Gson gson;

	public static Game getGameById(@NotNull String gameId) {
		return games.stream()
				.filter(game -> game.getGameId().equals(gameId))
				.findFirst()
				.orElse(null);
	}

	@SneakyThrows
	public static Game createGame(BedWarsMapsConfig.MapEntry entry) {
		if (games.size() >= MAX_GAMES) {
			return null;
		}
		InstanceContainer mapInstance = instanceManager.createInstanceContainer(fullbrightDimension);
		mapInstance.setChunkLoader(new PolarLoader(new File("./configuration/bedwars/" + entry.getId() + ".polar").toPath()));
		mapInstance.setExplosionSupplier(combatFeatures.get(FeatureType.EXPLOSION).getExplosionSupplier());

		Game game = new Game(entry, mapInstance);
		games.add(game);
		return game;
	}

	private static synchronized BedWarsMapsConfig.MapEntry nextMapEntry() {
		if (filteredMaps == null || filteredMaps.isEmpty()) return null;
		if (nextMapIndex >= filteredMaps.size()) nextMapIndex = 0;
		return filteredMaps.get(nextMapIndex++);
	}

	private static Component header() {
		return MiniMessage.miniMessage().deserialize("<aqua>You are playing on <bold><yellow>MC.HYPIXEL.NET</yellow></bold>");
	}

	private static Component footer(HypixelPlayer player) {
		Component start = Component.empty();
		BedWarsPlayer bwPlayer = (BedWarsPlayer) player;
		if (bwPlayer.getGame() != null) {
			start = start.append(MiniMessage.miniMessage().deserialize("<aqua>Kills: <yellow>0 <aqua>Final Kills: <yellow>0 <aqua>Beds Broken: <yellow>0")).appendNewline();
		}
		return start
				.append(Component.text("§aRanks, Boosters & MORE! §c§lSTORE.HYPIXEL.NET"));
	}

	@Override
	public ServerType getType() {
		return ServerType.BEDWARS_GAME;
	}

	@Override
	public void onInitialize(MinecraftServer server) {
		gson = new GsonBuilder().create();
		instanceManager = MinecraftServer.getInstanceManager();
		fullbrightDimension = MinecraftServer.getDimensionTypeRegistry().register("fullbright", DimensionType.builder().ambientLight(0.9f).build());
		MinecraftServer.getGlobalEventHandler().addChild(combatFeatures.createNode());

		Path mapsPath = Path.of("./configuration/bedwars/maps.json");
		if (!Files.exists(mapsPath)) {
			Logger.error("maps.json not found at {}", mapsPath.toAbsolutePath());
			return;
		}
		try (InputStream in = Files.newInputStream(mapsPath)) {
			byte[] bytes = in.readAllBytes();
			String json = new String(bytes, StandardCharsets.UTF_8);
			mapsConfig = gson.fromJson(json, BedWarsMapsConfig.class);
			filteredMaps = new ArrayList<>();
			if (mapsConfig != null && mapsConfig.getMaps() != null) {
				for (BedWarsMapsConfig.MapEntry e : mapsConfig.getMaps()) {
					var cfg = e.getConfiguration();
					List<BedwarsGameType> types = (cfg != null) ? cfg.getTypes() : null;
					boolean allowed;
					if (types == null || types.isEmpty()) {
						allowed = true;
					} else {
						allowed = types.contains(BedwarsGameType.SOLO);
					}
					if (allowed) filteredMaps.add(e);
					createGame(e);
				}
			}
		} catch (Exception e) {
			Logger.error("Failed to load maps.json", e);
			return;
		}

		MinecraftServer.getConnectionManager().setPlayerProvider((gameProfile, playerConnection) -> {
			BedWarsPlayer player = new BedWarsPlayer(gameProfile, playerConnection);

			UUID uuid = gameProfile.getPlayer().getUuid();
			String username = gameProfile.getPlayer().getUsername();

			if (RedisOriginServer.origin.containsKey(uuid)) {
				player.setOriginServer(RedisOriginServer.origin.get(uuid));
				RedisOriginServer.origin.remove(uuid);
			}

			Logger.info("Received new player: " + username + " (" + uuid + ")");

			return player;
		});
	}

	@Override
	public void afterInitialize(MinecraftServer server) {
		HypixelGenericLoader.loopThroughPackage("net.swofty.type.bedwarsgame.commands", HypixelCommand.class).forEach(command -> {
			try {
				MinecraftServer.getCommandManager().register(command.getCommand());
			} catch (Exception e) {
				Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
			}
		});
		HypixelGenericLoader.loopThroughPackage("net.swofty.type.bedwarsgame.item.impl", SimpleInteractableItem.class).forEach(itemHandler::add);
		MinestomPvP.init();

		// heartbeat to orchestrator with supported maps and current load
		MinecraftServer.getSchedulerManager().buildTask(() -> {
			UUID uuid = HypixelConst.getServerUUID();
			String shortName = HypixelConst.getShortenedServerName();
			int maxPlayers = HypixelConst.getMaxPlayers();
			int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();

			// Convert internal Game objects to commons Game objects
			List<net.swofty.commons.game.Game> commonsGames = new ArrayList<>();
			for (Game internalGame : TypeBedWarsGameLoader.getGames()) {
				net.swofty.commons.game.Game commonsGame = new net.swofty.commons.game.Game();
				commonsGame.setGameId(UUID.fromString(internalGame.getGameId()));
				commonsGame.setType(ServerType.BEDWARS_GAME);
				commonsGame.setMap(internalGame.getMapEntry().getName());
				commonsGame.setGameTypeName(internalGame.getBedwarsGameType().name());

				// Get involved players from the internal game
				List<UUID> playerUuids = new ArrayList<>();
				for (Player player : internalGame.getPlayers()) {
					playerUuids.add(player.getUuid());
				}
				commonsGame.setInvolvedPlayers(playerUuids);

				// Add disconnected players for rejoin system
				commonsGame.setDisconnectedPlayers(internalGame.getDisconnectedPlayerUuids());

				commonsGames.add(commonsGame);
			}

			var heartbeat = new GameHeartbeatProtocolObject.HeartbeatMessage(
					uuid,
					shortName,
					getType(),
					maxPlayers,
					onlinePlayers,
					commonsGames
			);
			new ProxyService(ServiceType.ORCHESTRATOR).handleRequest(heartbeat);
		}).delay(TaskSchedule.seconds(5)).repeat(TaskSchedule.seconds(1)).schedule();

		MinecraftServer.getSchedulerManager().buildTask(() -> {
			Collection<HypixelPlayer> players = getLoadedPlayers();
			if (players.isEmpty())
				return;
			for (HypixelPlayer player : players) {
				player.sendPlayerListHeaderAndFooter(header(), footer(player));
			}
		}).repeat(10, TimeUnit.SERVER_TICK).schedule();
	}

	@Override
	public List<ServiceType> getRequiredServices() {
		return List.of(ServiceType.ORCHESTRATOR);
	}

	@Override
	public TablistManager getTablistManager() {
		return new TablistManager() {
			@Override
			public List<TablistModule> getModules() {
				return List.of(
						new EmptyTabModule(),
						new EmptyTabModule(),
						new EmptyTabModule(),
						new EmptyTabModule()
				);
			}
		};
	}

	@Override
	public LoaderValues getLoaderValues() {
		return new LoaderValues(
				(_) -> new Pos(-39.5, 72, 0, -90, 0), // Spawn position
				false // Announce death messages
		);
	}

	@Override
	public List<HypixelEventClass> getTraditionalEvents() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarsgame.events",
				HypixelEventClass.class
		).toList();
	}

	@Override
	public List<HypixelEventClass> getCustomEvents() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarsgame.events.custom",
				HypixelEventClass.class
		).toList();
	}

	@Override
	public List<HypixelNPC> getNPCs() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarsgame.npcs",
				HypixelNPC.class
		).toList();
	}

	@Override
	public List<ServiceToClient> getServiceRedisListeners() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarsgame.redis.service",
				ServiceToClient.class
		).toList();
	}

	@Override
	public List<ProxyToClient> getProxyRedisListeners() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarsgame.redis",
				ProxyToClient.class
		).toList();
	}

	@Override
	public @Nullable CustomWorlds getMainInstance() {
		return null;
	}

	@Override
	public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
		return List.of(BedWarsDataHandler.class);
	}
}
