package net.swofty.type.generic.gui.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.i18n.I18n;

import java.util.List;

public class TranslatableItemStackCreator {

    public static ItemStack.Builder getStack(String nameKey, Material mat, int amt, String loreKey) {
        return ItemStackCreator.getStack(I18n.t(nameKey), mat, amt, I18n.t(loreKey));
    }

    public static ItemStack.Builder getStack(String nameKey, Material mat, int amt, String loreKey, Component... loreArgs) {
        return ItemStackCreator.getStack(I18n.t(nameKey), mat, amt, I18n.t(loreKey, loreArgs));
    }

    public static ItemStack.Builder getStack(String nameKey, Material mat, int amt, List<String> lore) {
        return ItemStackCreator.getStack(I18n.t(nameKey), mat, amt, ItemStackCreator.literalLoreComponents(lore));
    }

    public static ItemStack.Builder getStack(String nameKey, Material mat, int amt) {
        return ItemStackCreator.getStack(I18n.t(nameKey), mat, amt, List.of());
    }

    public static ItemStack.Builder getStackHead(String nameKey, String texture, int amt, String loreKey) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), texture, amt, I18n.t(loreKey));
    }

    public static ItemStack.Builder getStackHead(String nameKey, String texture, int amt, String loreKey, Component... loreArgs) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), texture, amt, I18n.t(loreKey, loreArgs));
    }

    public static ItemStack.Builder getStackHead(String nameKey, String texture, int amt, List<String> lore) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), texture, amt, ItemStackCreator.literalLoreComponents(lore));
    }

    public static ItemStack.Builder getStackHead(String nameKey, PlayerSkin skin, int amt, List<String> lore) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), skin, amt, ItemStackCreator.literalLoreComponents(lore));
    }
}
