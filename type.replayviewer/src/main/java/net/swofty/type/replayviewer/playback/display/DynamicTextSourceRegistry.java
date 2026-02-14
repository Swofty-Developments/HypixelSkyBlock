package net.swofty.type.replayviewer.playback.display;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DynamicTextSourceRegistry {

    private static final Map<String, Function<DynamicTextConfig, DynamicTextSource>> FACTORIES = new HashMap<>();

    static {
        // Register built-in sources
        registerFactory("generator", GeneratorTextSource::new);
        registerFactory("countdown", CountdownTextSource::new);
        registerFactory("static", StaticTextSource::new);
    }

    /**
     * Registers a factory for a display type.
     *
     * @param displayType the display type identifier
     * @param factory     the factory function
     */
    public static void registerFactory(String displayType, Function<DynamicTextConfig, DynamicTextSource> factory) {
        FACTORIES.put(displayType.toLowerCase(), factory);
    }

    /**
     * Creates a dynamic text source from configuration.
     *
     * @param config the configuration for the text source
     * @return the created text source
     */
    public static DynamicTextSource create(DynamicTextConfig config) {
        Function<DynamicTextConfig, DynamicTextSource> factory = FACTORIES.get(config.displayType().toLowerCase());
        if (factory != null) {
            return factory.apply(config);
        }
        // Default to static text
        return new StaticTextSource(config);
    }

    /**
     * Checks if a factory exists for a display type.
     *
     * @param displayType the display type to check
     * @return true if a factory exists
     */
    public static boolean hasFactory(String displayType) {
        return FACTORIES.containsKey(displayType.toLowerCase());
    }
}
