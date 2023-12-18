package net.swofty.utility;

public class MathUtility {
    public static double normalizeAngle(double angle, double maximum) {
        return (angle % maximum + maximum) % maximum - (maximum / 2);
    }

    public static <T> T arrayDValue(Object[] array, int index, T defaultValue) {
        try {
            return (T) array[index];
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
