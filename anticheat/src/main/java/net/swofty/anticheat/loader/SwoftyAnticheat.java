package net.swofty.anticheat.loader;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyEngine;
import net.swofty.anticheat.event.AntiCheatListener;
import net.swofty.anticheat.event.SwoftyEventHandler;
import net.swofty.anticheat.flag.FlagType;

import java.util.UUID;

public class SwoftyAnticheat {
    @Getter
    private static Loader loader = null;
    @Getter
    private static SwoftyValues values = null;
    @Getter
    private static PunishmentHandler punishmentHandler = new PunishmentHandler() {
        @Override
        public void onFlag(UUID uuid, FlagType flagType) {}
    };

    public static void loader(Loader loader) {
        SwoftyAnticheat.loader = loader;
    }

    public static void values(SwoftyValues values) {
        SwoftyAnticheat.values = values;
    }

    public static void punishmentHandler(PunishmentHandler punishmentHandler) {
        SwoftyAnticheat.punishmentHandler = punishmentHandler;
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

        loader.onInitialize();
        SwoftyEngine.registerEvents();
        SwoftyEngine.startSchedulers(loader);
    }
}
