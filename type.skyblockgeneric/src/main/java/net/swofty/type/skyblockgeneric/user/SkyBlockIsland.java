package net.swofty.type.skyblockgeneric.user;

import lombok.Getter;
import lombok.Setter;
import net.hollowcube.polar.AnvilPolar;
import net.hollowcube.polar.ChunkSelector;
import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarReader;
import net.hollowcube.polar.PolarWorld;
import net.hollowcube.polar.PolarWriter;
import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.world.DimensionType;
import net.swofty.commons.CustomWorlds;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.data.monogdb.IslandDatabase;
import net.swofty.type.skyblockgeneric.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.type.skyblockgeneric.event.custom.IslandFirstCreatedEvent;
import net.swofty.type.skyblockgeneric.event.custom.IslandSavedIntoDatabaseEvent;
import net.swofty.type.skyblockgeneric.minion.IslandMinionData;
import net.swofty.type.skyblockgeneric.utility.JerryInformation;
import org.bson.types.Binary;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class SkyBlockIsland {
    private static final String ISLAND_TEMPLATE_NAME = CustomWorlds.SKYBLOCK_ISLAND_TEMPLATE.getFolderName();
    private static final Map<UUID, SkyBlockIsland> loadedIslands = new ConcurrentHashMap<>();

    // Internal Island Data
    private final Object lifecycleLock = new Object();
    private final IslandDatabase database;
    private final CoopDatabase.Coop coop;
    private final UUID islandID;
    private volatile boolean created = false;
    private volatile CompletableFuture<SharedInstance> loadingFuture;
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
        synchronized (lifecycleLock) {
            if (created && islandInstance != null) {
                return CompletableFuture.completedFuture(islandInstance);
            }
            if (loadingFuture != null && !loadingFuture.isDone()) {
                return loadingFuture;
            }

            loadingFuture = new CompletableFuture<>();
        }

        CompletableFuture<SharedInstance> future = loadingFuture;
        Thread.startVirtualThread(() -> loadSharedInstance(future));
        return future;
    }

    public void runVacantCheck() {
        SharedInstance instanceSnapshot;
        PolarWorld worldSnapshot;

        synchronized (lifecycleLock) {
            if (islandInstance == null || !created) return;
            if (loadingFuture != null && !loadingFuture.isDone()) return;
            if (!islandInstance.getPlayers().isEmpty()) return;

            instanceSnapshot = islandInstance;
            worldSnapshot = world;
        }

        HypixelEventHandler.callCustomEvent(new IslandSavedIntoDatabaseEvent(
            this, coop != null, coop != null ? coop.memberProfiles() : List.of(islandID)
        ));

        save(worldSnapshot, instanceSnapshot);

        synchronized (lifecycleLock) {
            if (islandInstance != instanceSnapshot || !instanceSnapshot.getPlayers().isEmpty()) {
                return;
            }

            this.created = false;
            this.islandInstance = null;
            this.world = null;
            this.loadingFuture = null;
        }

        instanceSnapshot.getChunks().forEach(instanceSnapshot::unloadChunk);
        loadedIslands.remove(islandID, this);
    }

    private void save(PolarWorld worldToSave, SharedInstance instanceToSave) {
        if (worldToSave == null || instanceToSave == null) return;

        new PolarLoader(worldToSave).saveInstance(instanceToSave);
        database.insertOrUpdate("data", new Binary(PolarWriter.write(worldToSave)));
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
            new ArrayList<>(loadedIslands.values()).forEach(SkyBlockIsland::runVacantCheck);
            return TaskSchedule.tick(4);
        }, ExecutionType.TICK_END);
    }

    private void loadSharedInstance(CompletableFuture<SharedInstance> future) {
        try {
            InstanceManager manager = MinecraftServer.getInstanceManager();
            RegistryKey<DimensionType> dimensionTypeKey = MinecraftServer.getDimensionTypeRegistry().getKey(
                    Key.key("skyblock:island")
            );
            InstanceContainer temporaryInstance = manager.createInstanceContainer(dimensionTypeKey);
            SharedInstance sharedInstance = manager.createSharedInstance(temporaryInstance);

            List<SkyBlockPlayer> onlinePlayers;
            if (coop != null) {
                onlinePlayers = coop.getOnlineMembers();
            } else {
                SkyBlockPlayer islandOwner = SkyBlockGenericLoader.getPlayerFromProfileUUID(islandID);
                onlinePlayers = islandOwner == null ? List.of() : List.of(islandOwner);
            }

            if (!database.exists()) {
                islandVersion = HypixelConst.getCurrentIslandVersion();
                try {
                    world = AnvilPolar.anvilToPolar(Path.of(ISLAND_TEMPLATE_NAME), ChunkSelector.radius(3));
                } catch (IOException e) {
                    Logger.error("Failed to create island world", e);
                    throw new RuntimeException("Failed to create island world", e);
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
                    default:
                        throw new IllegalStateException("Unsupported island version " + islandVersion + " for island " + islandID);
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

            synchronized (lifecycleLock) {
                this.islandInstance = sharedInstance;
                this.created = true;
            }

            HypixelEventHandler.callCustomEvent(new IslandFetchedFromDatabaseEvent(
                    this, coop != null, onlinePlayers, coop != null ? coop.memberProfiles() : List.of(islandID))
            );

            future.complete(sharedInstance);
        } catch (Throwable throwable) {
            Logger.error(throwable, "Failed to load island {}", islandID);
            future.completeExceptionally(throwable);
        } finally {
            synchronized (lifecycleLock) {
                if (loadingFuture == future) {
                    loadingFuture = null;
                }
            }
        }
    }
}
