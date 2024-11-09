package net.swofty.types.generic.item.handlers.interactable;

import java.util.HashMap;
import java.util.Map;

public class InteractableRegistry {
    private static final Map<String, InteractableItemConfig> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("RIGHT_CLICK", InteractableItemConfig.builder().build());
    }

    public static void register(String id, InteractableItemConfig handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static InteractableItemConfig getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}