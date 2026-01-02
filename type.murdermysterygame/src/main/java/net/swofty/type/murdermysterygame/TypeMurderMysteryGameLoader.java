package net.swofty.type.murdermysterygame;

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
import net.swofty.commons.murdermystery.MurderMysteryGameType;
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
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.item.SimpleInteractableItem;
import net.swofty.type.murdermysterygame.item.SimpleInteractableItemHandler;
import net.swofty.type.murdermysterygame.maphandler.MapHandler;
import net.swofty.type.murdermysterygame.maphandler.MapHandlerRegistry;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
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

public class TypeMurderMysteryGameLoader implements HypixelTypeLoader {

    public static final int MAX_GAMES = 8;

    @Getter
    public static final List<Game> games = new ArrayList<>();

    @Getter
    public static final SimpleInteractableItemHandler itemHandler = new SimpleInteractableItemHandler();

    @Getter
    private static MurderMysteryMapsConfig mapsConfig;
    private static InstanceManager instanceManager;
    private static RegistryKey<@NotNull DimensionType> fullbrightDimension;
    private Gson gson;

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

    @SneakyThrows
    public static Game createGame(MurderMysteryMapsConfig.MapEntry entry, MurderMysteryGameType gameType) {
        if (games.size() >= MAX_GAMES) {
            return null;
        }
        InstanceContainer mapInstance = instanceManager.createInstanceContainer(fullbrightDimension);
        mapInstance.setChunkLoader(new PolarLoader(new File("./configuration/murdermystery/" + entry.getId() + ".polar").toPath()));
        Game game = new Game(entry, mapInstance, gameType);
        games.add(game);
        return game;
    }

    private static Component header() {
        return MiniMessage.miniMessage().deserialize("<aqua>You are playing on <bold><yellow>MC.HYPIXEL.NET</yellow></bold>");
    }

    private static Component footer(HypixelPlayer player) {
        Component start = Component.empty();
        if (TypeMurderMysteryGameLoader.getPlayerGame(player) != null) {
            start = start.append(MiniMessage.miniMessage().deserialize("<red>Role: <yellow>??? <red>Kills: <yellow>0")).appendNewline();
        }
        return start
                .append(Component.text("§aRanks, Boosters & MORE! §c§lSTORE.HYPIXEL.NET"));
    }

    @Override
    public ServerType getType() {
        return ServerType.MURDER_MYSTERY_GAME;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        gson = new GsonBuilder().create();
        instanceManager = MinecraftServer.getInstanceManager();
        fullbrightDimension = MinecraftServer.getDimensionTypeRegistry().register("fullbright", DimensionType.builder().ambientLight(0.9f).build());

        Path mapsPath = Path.of("./configuration/murdermystery/maps.json");
        if (!Files.exists(mapsPath)) {
            Logger.warn("maps.json not found at {}, Murder Mystery games will not be created automatically", mapsPath.toAbsolutePath());
        } else {
            try (InputStream in = Files.newInputStream(mapsPath)) {
                byte[] bytes = in.readAllBytes();
                String json = new String(bytes, StandardCharsets.UTF_8);
                mapsConfig = gson.fromJson(json, MurderMysteryMapsConfig.class);
                if (mapsConfig != null && mapsConfig.getMaps() != null) {
                    for (MurderMysteryMapsConfig.MapEntry e : mapsConfig.getMaps()) {
                        createGame(e, MurderMysteryGameType.CLASSIC);
                    }
                }
            } catch (Exception e) {
                Logger.error("Failed to load maps.json", e);
            }
        }

        MinecraftServer.getConnectionManager().setPlayerProvider((playerConnection, gameProfile) -> {
            MurderMysteryPlayer player = new MurderMysteryPlayer(playerConnection, gameProfile);

            UUID uuid = gameProfile.uuid();
            String username = gameProfile.name();

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
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.murdermysterygame.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });
        MinestomPvP.init();
        // Attach the PvP event node for bow shooting, arrow consumption, etc.
        MinecraftServer.getGlobalEventHandler().addChild(MinestomPvP.events());
        MurderMysteryGameScoreboard.start();
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.murdermysterygame.item.impl", SimpleInteractableItem.class).forEach(itemHandler::add);

        // Register map handlers
        HypixelGenericLoader.loopThroughPackage("net.swofty.type.murdermysterygame.maphandler.impl", MapHandler.class).forEach(MapHandlerRegistry::register);

        // heartbeat to orchestrator with supported maps and current load
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            UUID uuid = HypixelConst.getServerUUID();
            String shortName = HypixelConst.getShortenedServerName();
            int maxPlayers = HypixelConst.getMaxPlayers();
            int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();

            List<net.swofty.commons.game.Game> commonsGames = new ArrayList<>();
            for (Game internalGame : TypeMurderMysteryGameLoader.getGames()) {
                net.swofty.commons.game.Game commonsGame = new net.swofty.commons.game.Game();
                commonsGame.setGameId(UUID.fromString(internalGame.getGameId()));
                commonsGame.setType(ServerType.MURDER_MYSTERY_GAME);
                commonsGame.setMap(internalGame.getMapEntry().getName());
                commonsGame.setGameTypeName(internalGame.getGameType().name());

                List<UUID> playerUuids = new ArrayList<>();
                for (MurderMysteryPlayer player : internalGame.getPlayers()) {
                    playerUuids.add(player.getUuid());
                }
                commonsGame.setInvolvedPlayers(playerUuids);
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
                (_) -> new Pos(0, 72, 0, 0, 0),
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterygame.events",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterygame.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterygame.npcs",
                HypixelNPC.class
        ).toList();
    }

    @Override
    public List<ServiceToClient> getServiceRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterygame.redis.service",
                ServiceToClient.class
        ).toList();
    }

    @Override
    public List<ProxyToClient> getProxyRedisListeners() {
        return HypixelGenericLoader.loopThroughPackage(
                "net.swofty.type.murdermysterygame.redis",
                ProxyToClient.class
        ).toList();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(MurderMysteryDataHandler.class);
    }
}
