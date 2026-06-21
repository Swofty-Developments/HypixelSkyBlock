package net.swofty.type.generic.utility;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

public class ScheduleUtility {

    public static Task delay(Runnable runnable, int ticks) {
        return MinecraftServer.getSchedulerManager().scheduleTask(runnable, TaskSchedule.tick(ticks), TaskSchedule.stop());
    }

    public static Task delay(Runnable runnable, TaskSchedule schedule) {
        return MinecraftServer.getSchedulerManager().scheduleTask(runnable, schedule, TaskSchedule.stop());
    }

    public static Task nextTick(Runnable runnable) {
        return MinecraftServer.getSchedulerManager().scheduleNextTick(runnable);
    }

}
