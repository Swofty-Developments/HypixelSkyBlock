package net.swofty.user;

import lombok.Getter;
import net.hollowcube.polar.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;
import net.minestom.server.world.DimensionTypeManager;
import net.swofty.SkyBlock;
import net.swofty.data.mongodb.IslandDatabase;
import net.swofty.utility.PasterService;
import org.bson.Document;
import org.bson.types.Binary;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class SkyBlockIsland {
    private static String ISLAND_TEMPLATE_NAME = "hypixel_island_template";
    private IslandDatabase database;
    @Getter
    private Boolean created = false;
    private SharedInstance islandInstance;
    private PolarWorld world;

    public SkyBlockIsland(SkyBlockPlayer player) {
        this.database = new IslandDatabase(player.getUuid().toString());
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
                    throw new RuntimeException(e);
                }
            } else {
                world = PolarReader.read(((Binary) database.get("data", Binary.class)).getData());
            }

            islandInstance = manager.createSharedInstance(temporaryInstance);
            temporaryInstance.setChunkLoader(new PolarLoader(world));

            this.created = true;

            PasterService.fill(
                    new Pos(0, 104, 35),
                    new Pos(-2, 100, 35),
                    Block.NETHER_PORTAL,
                    islandInstance
            );

            future.complete(islandInstance);
        }).start();

        return future;
    }

    public void runVacantCheck() {
        if (islandInstance == null) return;

        if (islandInstance.getPlayers().isEmpty()) {
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
