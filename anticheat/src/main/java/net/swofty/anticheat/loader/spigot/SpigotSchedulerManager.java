package net.swofty.anticheat.loader.spigot;

import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class SpigotSchedulerManager extends SwoftySchedulerManager {
    private final Plugin plugin;
    private final Map<Integer, BukkitTask> tasks = new HashMap<>();
    private int nextId = 0;

    public SpigotSchedulerManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public int scheduleDelayedTask(Runnable runnable, int delay) {
        int id = nextId++;

        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        tasks.put(id, task);

        return id;
    }

    @Override
    public int scheduleRepeatingTask(Runnable runnable, int delay, int period) {
        int id = nextId++;

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
        tasks.put(id, task);

        return id;
    }

    @Override
    public void cancelTask(int taskId) {
        BukkitTask task = tasks.remove(taskId);
        if (task != null) task.cancel();
    }

    @Override
    public void cancelAllTasks() {
        tasks.values().forEach(BukkitTask::cancel);
        tasks.clear();
    }
}