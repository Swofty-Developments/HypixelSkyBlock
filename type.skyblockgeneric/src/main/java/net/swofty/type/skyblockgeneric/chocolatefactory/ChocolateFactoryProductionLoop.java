package net.swofty.type.skyblockgeneric.chocolatefactory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ChocolateFactoryProductionLoop {

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        scheduler.submitTask(() -> {
            for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
                if (player.getSkyblockDataHandler() == null) continue;

                ChocolateFactoryHelper.updateProduction(player);
            }
            return TaskSchedule.tick(20); // Every second (20 ticks)
        });
    }
}
