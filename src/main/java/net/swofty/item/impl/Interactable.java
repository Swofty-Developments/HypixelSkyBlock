package net.swofty.item.impl;

import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;

public interface Interactable {
    void onRightInteract(SkyBlockPlayer player, SkyBlockItem item);
    void onLeftInteract(SkyBlockPlayer player, SkyBlockItem item);

    /**
     * @param player The player who interacted with the item
     * @param item The item that was interacted with
     * @return Whether or not to cancel the event
     */
    boolean onInventoryInteract(SkyBlockPlayer player, SkyBlockItem item);
}
