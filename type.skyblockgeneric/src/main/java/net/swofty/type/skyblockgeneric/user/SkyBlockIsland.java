package net.swofty.type.skyblockgeneric.user;

import lombok.Getter;
import lombok.Setter;
import net.hollowcube.polar.*;
import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.CustomWorlds;
import net.swofty.type.generic.HypixelConst;
import HypixelPlayer;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.data.monogdb.IslandDatabase;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.skyblockgeneric.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.type.skyblockgeneric.event.custom.IslandFirstCreatedEvent;
import net.swofty.type.skyblockgeneric.event.custom.IslandSavedIntoDatabaseEvent;
import net.swofty.type.skyblockgeneric.minion.IslandMinionData;
import net.swofty.type.skyblockgeneric.utility.JerryInformation;
import net.swofty.type.generic.utility.MathUtility;
import org.bson.types.Binary;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class SkyBlockIsland {
    private static final String ISLAND_TEMPLATE_NAME = CustomWorlds.ISLANDS_TEMPLATE.getFolderName();
    private static final Map<UUID, SkyBlockIsland> loadedIslands = new HashMap<>();

    // Internal Island Data
    private final IslandDatabase database;
    private final CoopDatabase.Coop coop;
    private final UUID islandID;
    private Boolean created = false;
    private SharedInstance islandInstance;
    private PolarWorld world;

    // External Island Data
    @Setter
    private JerryInformation jerryInformation = null;
    @Setter
    private IslandMinionData minionData = null;
    @Setter
    private long lastSaved = 0;
    @Setter
    private Integer islandVersion;

    public SkyBlockIsland(UUID islandID, UUID profileID) {
        this.islandID = islandID;
        this.database = new IslandDatabase(islandID.toString());
        this.coop = CoopDatabase.getFromMemberProfile(profileID);

        loadedIslands.put(islandID, this);
    }

    public CompletableFuture<SharedInstance> getSharedInstance() {
        InstanceManager manager = MinecraftServer.getInstanceManager();
        CompletableFuture<SharedInstance> future = new CompletableFuture<>();

        new Thread(() -> {
            if (created) {
                future.complete(islandInstance);
                return;
            }
            DynamicRegistry.Key<DimensionType> dimensionTypeKey = MinecraftServer.getDimensionTypeRegistry().getKey(
                    MinecraftServer.getDimensionTypeRegistry().getId(Key.key("skyblock:island"))
            );
            InstanceContainer temporaryInstance = manager.createInstanceContainer(dimensionTypeKey);
            islandInstance = manager.createSharedInstance(temporaryInstance);

            List<SkyBlockPlayer> onlinePlayers;
            if (coop != null) {
                onlinePlayers = coop.getOnlineMembers();
            } else {
                // Island ID will be the same as the profile ID if the island is not a coop
                try {
                    onlinePlayers = List.of(SkyBlockGenericLoader.getPlayerFromProfileUUID(islandID));
                } catch (NullPointerException e) {
                    // Player doesn't have their data loaded yet
                    onlinePlayers = List.of();
                }
            }

            if (!database.exists()) {
                islandVersion = HypixelConst.getCurrentIslandVersion();
                try {
                    world = AnvilPolar.anvilToPolar(Path.of(ISLAND_TEMPLATE_NAME), ChunkSelector.radius(3));
                } catch (IOException e) {
                    // TODO: Proper error handling
                    throw new RuntimeException(e);
                }

                HypixelEventHandler.callCustomEvent(new IslandFirstCreatedEvent(
                        this, coop != null, coop != null ? coop.memberProfiles() : List.of(islandID)
                ));
            } else {
                if (database.has("version"))
                    islandVersion = (int) database.get("version", Integer.class);
                else islandVersion = 0;

                switch (islandVersion) {
                    case 0:
                        lastSaved = System.currentTimeMillis();
                        try {
                            world = AnvilPolar.anvilToPolar(Path.of(ISLAND_TEMPLATE_NAME), ChunkSelector.radius(3));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 1:
                        world = PolarReader.read(((Binary) database.get("data", Binary.class)).getData());
                        lastSaved = (long) database.get("lastSaved", Long.class);
                        break;
                }

                int oldVersion = islandVersion;
                if (islandVersion < HypixelConst.getCurrentIslandVersion()) {
                    MathUtility.delay(() -> {
                        SkyBlockGenericLoader.getLoadedPlayers().stream().filter(player -> player.getSkyBlockIsland().getIslandID() == islandID).forEach(player -> {
                            player.getLogHandler().debug("Your island was migrated from version §c" + oldVersion + " §fto §a" + HypixelConst.getCurrentIslandVersion() + "§f!");
                        });
                    }, 20);
                    islandVersion = HypixelConst.getCurrentIslandVersion();
                }
            }
            temporaryInstance.setChunkLoader(new PolarLoader(world));

            this.created = true;

            HypixelEventHandler.callCustomEvent(new IslandFetchedFromDatabaseEvent(
                    this, coop != null, onlinePlayers, coop != null ? coop.memberProfiles() : List.of(islandID))
            );

            future.complete(islandInstance);
            onlinePlayers.forEach(HypixelPlayer::setReadyForEvents);
        }).start();

        return future;
    }

    public void runVacantCheck() {
        if (islandInstance == null) return;

        if (islandInstance.getPlayers().isEmpty()) {
            HypixelEventHandler.callCustomEvent(new IslandSavedIntoDatabaseEvent(
                    this, coop != null, coop != null ? coop.memberProfiles() : List.of(islandID)
            ));

            save();
            this.created = false;
            islandInstance.getChunks().forEach(chunk -> {
                islandInstance.unloadChunk(chunk);
            });
            this.islandInstance = null;
            this.world = null;
        }
    }

    private void save() {
        new PolarLoader(world).saveInstance(islandInstance);
        database.insertOrUpdate("data", new Binary(PolarWriter.write(world)));
        database.insertOrUpdate("lastSaved", System.currentTimeMillis());
        database.insertOrUpdate("version", islandVersion);
    }

    public static boolean hasIsland(UUID islandID) {
        return loadedIslands.containsKey(islandID);
    }

    public static @Nullable SkyBlockIsland getIsland(UUID islandID) {
        if (!loadedIslands.containsKey(islandID)) return null;
        return loadedIslands.get(islandID);
    }

    public static void runVacantLoop(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                if (player.isOnIsland())
                    player.getSkyBlockIsland().runVacantCheck();
            });
            return TaskSchedule.tick(4);
        }, ExecutionType.TICK_END);
    }
}