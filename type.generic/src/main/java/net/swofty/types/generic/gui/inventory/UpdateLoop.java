package net.swofty.types.generic.gui.inventory;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.TaskSchedule;

public class UpdateLoop {
    @Getter
    private final String id;
    private final int tickInterval;
    private final Runnable task;
    @Getter
    private boolean running;

    public UpdateLoop(String id, int tickInterval, Runnable task) {
        this.id = id;
        this.tickInterval = tickInterval;
        this.task = task;
    }

    public void start() {
        if (running) return;
        running = true;

        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            if (!running) {
                return TaskSchedule.stop();
            }

            Thread.startVirtualThread(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return TaskSchedule.tick(tickInterval);
        });
    }

    public void stop() {
        running = false;
    }
}