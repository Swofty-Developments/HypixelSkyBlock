package net.swofty.anticheat.loader.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinestomSchedulerManager extends SwoftySchedulerManager {
    private final Map<Runnable, Integer> activeTasks = new HashMap<>();

    @Override
    public int scheduleDelayedTask(Runnable runnable, int delay) {
        int id = activeTasks.size();

        activeTasks.put(runnable, id);
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            runnable.run();
            activeTasks.remove(runnable);
            return TaskSchedule.stop();
        }, TaskSchedule.tick(delay));

        return id;
    }

    @Override
    public int scheduleRepeatingTask(Runnable runnable, int delay, int period) {
        int id = activeTasks.size();

        activeTasks.put(runnable, id);
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (!activeTasks.containsKey(runnable)) {
                return TaskSchedule.stop();
            }
            runnable.run();
            return TaskSchedule.tick(period);
        }, TaskSchedule.tick(delay));

        return id;
    }

    @Override
    public void cancelTask(int taskId) {
        List<Runnable> toRemove = new ArrayList<>();
        activeTasks.forEach((runnable, id) -> {
            if (id == taskId) {
                toRemove.add(runnable);
            }
        });

        toRemove.forEach(activeTasks::remove);
    }

    @Override
    public void cancelAllTasks() {
        activeTasks.clear();
    }
}
