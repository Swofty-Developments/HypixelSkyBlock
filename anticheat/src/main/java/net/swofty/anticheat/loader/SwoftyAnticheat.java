package net.swofty.anticheat.loader;

import net.swofty.anticheat.engine.SwoftyEngine;
import net.swofty.anticheat.event.AntiCheatListener;
import net.swofty.anticheat.event.SwoftyEventHandler;

public class SwoftyAnticheat {
    private static Loader loader = null;
    private static SwoftyValues values = null;

    public static void loader(Loader loader) {
        SwoftyAnticheat.loader = loader;
    }

    public static void values(SwoftyValues values) {
        SwoftyAnticheat.values = values;
    }

    public static void registerEvent(AntiCheatListener listener) {
        SwoftyEventHandler.registerEventMethods(listener);
    }

    public static void start() {
        if (loader == null) {
            throw new IllegalStateException("Loader not set, use SwoftyAnticheat#loader(Loader) to set the loader");
        }

        if (values == null) {
            throw new IllegalStateException("Values not set, use SwoftyAnticheat#values(SwoftyValues) to set the values");
        }

        SwoftyEngine.registerModules();
        SwoftyEngine.startSchedulers(loader);
    }
}
