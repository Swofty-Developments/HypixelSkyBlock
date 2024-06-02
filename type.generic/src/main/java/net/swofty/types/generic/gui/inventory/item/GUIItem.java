package net.swofty.types.generic.gui.inventory.item;

import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.user.SkyBlockPlayer;

public abstract class GUIItem {
    public final int itemSlot;
    public abstract ItemStack.Builder getItem(SkyBlockPlayer player);

    public GUIItem(int slot) {
        this.itemSlot = slot;
    }

    public boolean canPickup() {
        return false;
    }
}
