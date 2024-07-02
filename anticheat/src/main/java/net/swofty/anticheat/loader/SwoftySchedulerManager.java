package net.swofty.anticheat.loader;

public abstract class SwoftySchedulerManager {
    public abstract int scheduleDelayedTask(Runnable runnable, int delay);
    public abstract int scheduleRepeatingTask(Runnable runnable, int delay, int period);
    public abstract void cancelTask(int taskId);
    public abstract void cancelAllTasks();
}
