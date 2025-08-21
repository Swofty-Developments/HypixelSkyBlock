package net.swofty.type.bedwarsgame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.hollowcube.polar.PolarLoader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.tag.Tag;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.CustomWorlds;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.pvp.MinestomPvP;
import net.swofty.pvp.feature.CombatFeatureSet;
import net.swofty.pvp.feature.CombatFeatures;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.projectile.BowModule;
import net.swofty.pvp.utils.CombatVersion;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.map.MapsConfig;
import net.swofty.type.bedwarsgame.shop.ShopService;
import net.swofty.type.bedwarsgame.shop.TeamShopService;
import net.swofty.type.bedwarsgame.shop.TrapService;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.entity.animalnpc.HypixelAnimalNPC;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.redis.RedisOriginServer;
import net.swofty.type.generic.tab.EmptyTabModule;
import net.swofty.type.generic.tab.TablistManager;
import net.swofty.type.generic.tab.TablistModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.slf4j.LoggerFactory;
import org.tinylog.Logger;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static net.swofty.pvp.feature.CombatFeatures.*;

public class TypeBedWarsGameLoader implements HypixelTypeLoader {

	public static final int MAX_GAMES = 8;
	@Getter
	public static final List<Game> games = new ArrayList<>();
	@Getter
	public static final ShopService shopService = new ShopService();
	@Getter
	public static final TeamShopService teamShopService = new TeamShopService();
	@Getter
	public static final TrapService trapService = new TrapService();
	@Getter
	private static MapsConfig mapsConfig;
	private Gson gson;

	private static InstanceManager instanceManager;
	public static final Tag<Boolean> PLAYER_PLACED_TAG = Tag.Boolean("player_placed");
	public static final Tag<Integer> ARMOR_LEVEL_TAG = Tag.Integer("armor_level");
	private static DynamicRegistry.Key<DimensionType> fullbrightDimension = null;

    static CombatFeatureSet combatFeatures = CombatFeatures.empty().version(CombatVersion.LEGACY).addAll(List.of(
            VANILLA_ARMOR, VANILLA_ATTACK, VANILLA_CRITICAL, /*VANILLA_SWEEPING,*/
            VANILLA_EQUIPMENT, VANILLA_BLOCK, VANILLA_ATTACK_COOLDOWN, VANILLA_ITEM_COOLDOWN,
            VANILLA_DAMAGE, VANILLA_EFFECT, VANILLA_ENCHANTMENT, VANILLA_EXPLOSION,
            VANILLA_EXPLOSIVE, VANILLA_FALL, VANILLA_FOOD, LEGACY_VANILLA_BLOCK,
            VANILLA_REGENERATION, VANILLA_KNOCKBACK, VANILLA_POTION, //VANILLA_BOW,
            VANILLA_CROSSBOW, VANILLA_FISHING_ROD, VANILLA_MISC_PROJECTILE,
            VANILLA_PROJECTILE_ITEM, VANILLA_TRIDENT, VANILLA_SPECTATE,
            VANILLA_PLAYER_STATE, VANILLA_TOTEM//, VANILLA_DEATH_MESSAGE
    )).build();

	@Override
	public ServerType getType() {
		return ServerType.BEDWARS_GAME;
	}

	@Override
	public void onInitialize(MinecraftServer server) {
		gson = new GsonBuilder().create();
		instanceManager = MinecraftServer.getInstanceManager();
		fullbrightDimension = MinecraftServer.getDimensionTypeRegistry().register("fullbright", DimensionType.builder().ambientLight(4f).build());
        MinecraftServer.getGlobalEventHandler().addChild(combatFeatures.createNode());

		Path mapsPath = Path.of("./configuration/bedwars/maps.json");
		if (!Files.exists(mapsPath)) {
			//logger.error("maps.json not found at {}", mapsPath.toAbsolutePath());
			return;
		}
		try (InputStream in = Files.newInputStream(mapsPath)) {
			byte[] bytes = in.readAllBytes();
			String json = new String(bytes, StandardCharsets.UTF_8);
			mapsConfig = gson.fromJson(json, MapsConfig.class);
			//logger.info("Loaded {} maps from maps.json", mapsConfig.getMaps() != null ? mapsConfig.getMaps().size() : 0);
		} catch (Exception e) {
			//logger.error("Failed to load maps.json", e);
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

	public static Game getGameById(@NotNull String gameId) {
		return games.stream()
				.filter(game -> game.getGameId().equals(gameId))
				.findFirst()
				.orElse(null);
	}

	public static Game getPlayerGame(@NotNull Player player) {
		String gameId = player.getTag(Tag.String("gameId"));
		if (gameId == null) return null;
		return getGameById(gameId);
	}

	@Override
	public void afterInitialize(MinecraftServer server) {
		HypixelGenericLoader.loopThroughPackage("net.swofty.type.bedwarsgame.commands", HypixelCommand.class).forEach(command -> {
			try {
				MinecraftServer.getCommandManager().register(command.getCommand());
			} catch (Exception e) {
				Logger.error("Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
				e.printStackTrace();
			}
		});
		MinestomPvP.init();
	}

	@Override
	public List<ServiceType> getRequiredServices() {
		return List.of();
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
				(type) -> new Pos(-39.5, 72, 0, -90, 0), // Spawn position
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
	public List<HypixelVillagerNPC> getVillagerNPCs() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarsgame.villagers",
				HypixelVillagerNPC.class
		).toList();
	}

	@Override
	public List<HypixelAnimalNPC> getAnimalNPCs() {
		return HypixelGenericLoader.loopThroughPackage(
				"net.swofty.type.bedwarsgame.animalnpcs",
				HypixelAnimalNPC.class
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
		return List.of();
	}

	@Override
	public @Nullable CustomWorlds getMainInstance() {
		return null;
	}

	@SneakyThrows
	public static Game createGame(MapsConfig.MapEntry entry) {
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

}
