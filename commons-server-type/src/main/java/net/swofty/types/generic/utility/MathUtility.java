package net.swofty.types.generic.utility;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.util.concurrent.CompletableFuture;

public class MathUtility {
    public static double normalizeAngle(double angle, double maximum) {
        return (angle % maximum + maximum) % maximum - (maximum / 2);
    }

    public static void delay(Runnable runnable, int ticks) {
        MinecraftServer.getSchedulerManager().scheduleTask(runnable, TaskSchedule.tick(ticks), TaskSchedule.stop());
    }

    public static <T> T arrayDValue(Object[] array, int index, T defaultValue) {
        try {
            return (T) array[index];
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Float fromDouble(double value) {
        return (float) value;
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static int random(int min, int max) {
        return (int) Math.round(Math.random() * (max - min) + min);
    }
}
