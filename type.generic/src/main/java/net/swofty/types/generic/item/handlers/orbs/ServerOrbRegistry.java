package net.swofty.types.generic.item.handlers.orbs;

import java.util.HashMap;
import java.util.Map;

public class ServerOrbRegistry {
    private static final Map<String, ServerOrbHandler> HANDLERS = new HashMap<>();

    public static void registerHandler(String id, ServerOrbHandler handler) {
        HANDLERS.put(id, handler);
    }

    public static ServerOrbHandler getHandler(String id) {
        return HANDLERS.get(id);
    }
}