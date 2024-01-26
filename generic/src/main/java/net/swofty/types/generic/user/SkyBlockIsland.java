package net.swofty.types.generic.user;

import lombok.Getter;
import lombok.Setter;
import net.hollowcube.polar.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.NamespaceID;
import net.swofty.commons.CustomWorlds;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.data.mongodb.IslandDatabase;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.types.generic.event.custom.IslandFirstCreatedEvent;
import net.swofty.types.generic.event.custom.IslandSavedIntoDatabaseEvent;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.utility.JerryInformation;
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

            InstanceContainer temporaryInstance = manager.createInstanceContainer(MinecraftServer.getDimensionTypeManager().getDimension(
                    NamespaceID.from("skyblock:island")
            ));

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
                try {
                    world = AnvilPolar.anvilToPolar(Path.of(ISLAND_TEMPLATE_NAME), ChunkSelector.radius(3));
                } catch (IOException e) {
                    // TODO: Proper error handling
                    throw new RuntimeException(e);
                }

                SkyBlockEvent.callSkyBlockEvent(new IslandFirstCreatedEvent(
                        this, coop != null, coop != null ? coop.memberProfiles() : List.of(islandID)
                ));
            } else {
                world = PolarReader.read(((Binary) database.get("data", Binary.class)).getData());
            }

            islandInstance = manager.createSharedInstance(temporaryInstance);
            temporaryInstance.setChunkLoader(new PolarLoader(world));

            this.created = true;

            SkyBlockEvent.callSkyBlockEvent(new IslandFetchedFromDatabaseEvent(
                    this, coop != null, onlinePlayers, coop != null ? coop.memberProfiles() : List.of(islandID))
            );

            future.complete(islandInstance);
        }).start();

        return future;
    }

    public void runVacantCheck() {
        if (islandInstance == null) return;

        if (islandInstance.getPlayers().isEmpty()) {
            SkyBlockEvent.callSkyBlockEvent(new IslandSavedIntoDatabaseEvent(
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
                player.getSkyBlockIsland().runVacantCheck();
            });
            return TaskSchedule.tick(4);
        }, ExecutionType.ASYNC);
    }
}
