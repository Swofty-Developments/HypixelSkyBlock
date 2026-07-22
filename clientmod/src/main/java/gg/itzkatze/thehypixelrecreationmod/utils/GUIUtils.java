package gg.itzkatze.thehypixelrecreationmod.utils;

import gg.itzkatze.thehypixelrecreationmod.mixin.HandledScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class GUIUtils {
    private GUIUtils() {
    }

    public static ItemStack getHoveredItem(Minecraft client) {
        if (!(client.gui.screen() instanceof AbstractContainerScreen<?> screen)) {
            return ItemStack.EMPTY;
        }

        Slot slot = ((HandledScreenAccessor) screen).getHoveredSlot();
        return (slot != null && slot.hasItem()) ? slot.getItem() : ItemStack.EMPTY;
    }
}
