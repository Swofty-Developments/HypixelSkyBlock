package net.swofty.user;

import lombok.Getter;
import net.hollowcube.polar.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.*;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.NamespaceID;
import net.swofty.SkyBlock;
import net.swofty.data.mongodb.IslandDatabase;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.IslandCreatedEvent;
import net.swofty.event.custom.IslandLoadedEvent;
import net.swofty.event.custom.IslandUnloadEvent;
import org.bson.types.Binary;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class SkyBlockIsland {
    private static final String ISLAND_TEMPLATE_NAME = "hypixel_island_template";

    @Getter
    private final IslandDatabase database;
    private final SkyBlockPlayer owner;
    @Getter
    private Boolean created = false;
    @Getter
    private SharedInstance islandInstance;
    private PolarWorld world;

    public SkyBlockIsland(SkyBlockPlayer player) {
        this.database = new IslandDatabase(player.getUuid().toString());
        this.owner = player;
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

            if (!database.exists()) {
                try {
                    world = AnvilPolar.anvilToPolar(Path.of(ISLAND_TEMPLATE_NAME), ChunkSelector.radius(3));
                } catch (IOException e) {
                    // TODO: Proper error handling
                    throw new RuntimeException(e);
                }

                MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                    SkyBlockEvent.callSkyBlockEvent(new IslandCreatedEvent(this.owner, this));
                }, TaskSchedule.tick(1), TaskSchedule.stop());
            } else {
                world = PolarReader.read(((Binary) database.get("data", Binary.class)).getData());
            }

            islandInstance = manager.createSharedInstance(temporaryInstance);
            temporaryInstance.setChunkLoader(new PolarLoader(world));

            this.created = true;

            SkyBlockEvent.callSkyBlockEvent(new IslandLoadedEvent(this.owner, this));

            future.complete(islandInstance);
        }).start();

        return future;
    }

    public void runVacantCheck() {
        if (islandInstance == null) return;

        if (islandInstance.getPlayers().isEmpty()) {
            SkyBlockEvent.callSkyBlockEvent(new IslandUnloadEvent(this.owner, this));

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

    public static void runVacantLoop(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            SkyBlock.getLoadedPlayers().forEach(player -> {
                player.getSkyBlockIsland().runVacantCheck();
            });
            return TaskSchedule.tick(10);
        }, ExecutionType.ASYNC);
    }
}
