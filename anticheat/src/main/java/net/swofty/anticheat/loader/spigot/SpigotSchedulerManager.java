package net.swofty.anticheat.loader.spigot;

import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class SpigotSchedulerManager extends SwoftySchedulerManager {
    private final Plugin plugin;

    public SpigotSchedulerManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void scheduleRepeating(Runnable runnable, long delay, long period) {
        Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }

    @Override
    public void scheduleOnce(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    @Override
    public void scheduleAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }
}
