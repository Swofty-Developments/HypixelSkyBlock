package net.swofty.type.skyblockgeneric.chocolatefactory;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;

/**
 * Handles periodic chocolate production updates for all online players.
 * This runs every second to update chocolate amounts based on production rates.
 */
public class ChocolateFactoryProductionLoop {

    /**
     * Starts the chocolate factory production loop.
     * Should be called during server initialization.
     */
    public static void start() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            // Update chocolate production for all online players
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                try {
                    ChocolateFactoryHelper.updateProduction(player);
                } catch (Exception e) {
                    // Silently ignore errors to prevent loop from stopping
                }
            });
            return TaskSchedule.seconds(1);
        });
    }
}
