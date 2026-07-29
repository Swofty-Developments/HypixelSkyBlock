package net.swofty.type.generic.gui.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.i18n.I18n;

import java.util.List;

public class TranslatableItemStackCreator {

    private static Component[] resolveLore(String loreKey, ComponentLike... loreArgs) {
        try {
            return loreArgs.length == 0
                ? I18n.iterable(loreKey)
                : I18n.iterable(loreKey, loreArgs);
        } catch (IllegalStateException exception) {
            if (!isMissingIterableKey(exception)) {
                throw exception;
            }

            return new Component[]{
                loreArgs.length == 0
                    ? I18n.t(loreKey)
                    : I18n.t(loreKey, loreArgs)
            };
        }
    }

    private static boolean isMissingIterableKey(IllegalStateException exception) {
        String message = exception.getMessage();
        return message != null && message.startsWith("Missing dialogue translation key in en_US:");
    }

    public static ItemStack.Builder getStack(String nameKey, Material mat, int amt, String loreKey) {
        return ItemStackCreator.getStack(I18n.t(nameKey), mat, amt, resolveLore(loreKey));
    }

    public static ItemStack.Builder getStack(String nameKey, Material mat, int amt, String loreKey, ComponentLike... loreArgs) {
        return ItemStackCreator.getStack(I18n.t(nameKey), mat, amt, resolveLore(loreKey, loreArgs));
    }

    public static ItemStack.Builder getStack(String nameKey, Material mat, int amt, List<?> lore) {
        return ItemStackCreator.getStack(I18n.t(nameKey), mat, amt, ItemStackCreator.literalLoreComponents(lore));
    }

    public static ItemStack.Builder getStack(String nameKey, Material mat, int amt) {
        return ItemStackCreator.getStack(I18n.t(nameKey), mat, amt, List.of());
    }

    public static ItemStack.Builder getStackHead(String nameKey, String texture, int amt, String loreKey) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), texture, amt, resolveLore(loreKey));
    }

    public static ItemStack.Builder getStackHead(String nameKey, String texture, int amt, String loreKey, Component... loreArgs) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), texture, amt, resolveLore(loreKey, loreArgs));
    }

    public static ItemStack.Builder getStackHead(String nameKey, String texture, int amt, List<?> lore) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), texture, amt, ItemStackCreator.literalLoreComponents(lore));
    }

    public static ItemStack.Builder getStackHead(String nameKey, PlayerSkin skin, int amt, List<?> lore) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), skin, amt, ItemStackCreator.literalLoreComponents(lore));
    }

    public static ItemStack.Builder getStackHead(String nameKey, PlayerSkin skin, int amt, Component... lore) {
        return ItemStackCreator.getStackHead(I18n.t(nameKey), skin, amt, lore);
    }
}
