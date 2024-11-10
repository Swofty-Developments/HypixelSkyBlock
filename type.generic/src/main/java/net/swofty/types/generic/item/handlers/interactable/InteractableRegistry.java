package net.swofty.types.generic.item.handlers.interactable;

import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;

import java.util.HashMap;
import java.util.Map;

public class InteractableRegistry {
    private static final Map<String, InteractableItemConfig> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("SKYBLOCK_MENU_INTERACT", InteractableItemConfig.builder().rightClickHandler(
                ((skyBlockPlayer, skyBlockItem) -> new GUISkyBlockMenu().open(skyBlockPlayer))
        ).leftClickHandler(
                ((skyBlockPlayer, skyBlockItem) -> new GUISkyBlockMenu().open(skyBlockPlayer))
        ).inventoryInteractHandler(
                ((skyBlockPlayer, skyBlockItem) -> {
                    new GUISkyBlockMenu().open(skyBlockPlayer);
                    return false;
                })
        ).build());
    }

    public static void register(String id, InteractableItemConfig handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static InteractableItemConfig getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}