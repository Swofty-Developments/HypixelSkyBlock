package net.swofty.type.generic.gui.inventory.item;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.user.HypixelPlayer;

public abstract class GUIItem {
    public final int itemSlot;
    public abstract ItemStack.Builder getItem(HypixelPlayer player);

    public GUIItem(int slot) {
        this.itemSlot = slot;
    }

    public boolean canPickup() {
        return false;
    }
}
