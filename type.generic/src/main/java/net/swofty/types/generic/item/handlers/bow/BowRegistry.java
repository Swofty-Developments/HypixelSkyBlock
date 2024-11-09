package net.swofty.types.generic.item.handlers.bow;

import java.util.HashMap;
import java.util.Map;

public class BowRegistry {
    private static final Map<String, BowHandler> REGISTERED_HANDLERS = new HashMap<>();

    static {

    }

    public static void register(String id, BowHandler handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static BowHandler getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}