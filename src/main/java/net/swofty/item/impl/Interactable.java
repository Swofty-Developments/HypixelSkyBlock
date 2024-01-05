package net.swofty.item.impl;

import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;

public interface Interactable {
    default void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
    }

    default void onLeftInteract(SkyBlockPlayer player, SkyBlockItem item) {
    }

    /**
     * @param player The player who interacted with the item
     * @param item   The item that was interacted with
     * @return Whether or not to cancel the event
     */
    default boolean onInventoryInteract(SkyBlockPlayer player, SkyBlockItem item) {
        return false;
    }
}
