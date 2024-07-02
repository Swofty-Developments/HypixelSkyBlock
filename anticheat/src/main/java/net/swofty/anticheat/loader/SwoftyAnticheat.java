package net.swofty.anticheat.loader;

public class SwoftyAnticheat {
    private static Loader loader = null;
    private static SwoftyValues values = null;

    public static void loader(Loader loader) {
        SwoftyAnticheat.loader = loader;
    }

    public static void values(SwoftyValues values) {
        SwoftyAnticheat.values = values;
    }
}
