package net.swofty.type.generic.gui.v2;

import net.minestom.server.item.ItemStack;

@FunctionalInterface
public interface SlotChangeHandler<S> {
    void onChange(int slot, ItemStack oldItem, ItemStack newItem, S state);
}
