package net.swofty.type.generic.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for game-specific data handlers.
 * Handlers must be registered during server initialization before players join.
 */
public class GameDataHandlerRegistry {
    private static final Map<Class<? extends GameDataHandler>, GameDataHandler> handlers = new HashMap<>();

    /**
     * Register a game data handler instance.
     * Call this during server initialization.
     */
    public static void register(GameDataHandler handler) {
        handlers.put(handler.getClass(), handler);
    }

    /**
     * Get a handler instance by its class.
     */
    @SuppressWarnings("unchecked")
    public static <T extends GameDataHandler> T get(Class<T> handlerClass) {
        return (T) handlers.get(handlerClass);
    }

    /**
     * Get all registered handlers.
     */
    public static Collection<GameDataHandler> getAll() {
        return handlers.values();
    }

    /**
     * Check if a handler is registered.
     */
    public static boolean isRegistered(Class<? extends GameDataHandler> handlerClass) {
        return handlers.containsKey(handlerClass);
    }
}
