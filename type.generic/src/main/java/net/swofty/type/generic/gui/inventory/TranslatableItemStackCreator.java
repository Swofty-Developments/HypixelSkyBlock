package net.swofty.type.generic.gui.inventory;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TranslatableItemStackCreator {

    public static ItemStack.Builder getStack(HypixelPlayer p, String nameKey, Material mat, int amt, String loreKey) {
        Locale l = p.getLocale();
        return ItemStackCreator.getStack(I18n.string(nameKey, l), mat, amt, I18n.lore(loreKey, l));
    }

    public static ItemStack.Builder getStack(HypixelPlayer p, String nameKey, Material mat, int amt, String loreKey, Map<String, String> ph) {
        Locale l = p.getLocale();
        return ItemStackCreator.getStack(I18n.string(nameKey, l), mat, amt, I18n.lore(loreKey, l, ph));
    }

    public static ItemStack.Builder getStack(HypixelPlayer p, String nameKey, Material mat, int amt, List<String> lore) {
        Locale l = p.getLocale();
        return ItemStackCreator.getStack(I18n.string(nameKey, l), mat, amt, lore);
    }

    public static ItemStack.Builder getStack(HypixelPlayer p, String nameKey, Material mat, int amt) {
        Locale l = p.getLocale();
        return ItemStackCreator.getStack(I18n.string(nameKey, l), mat, amt);
    }

    public static ItemStack.Builder getStackHead(HypixelPlayer p, String nameKey, String texture, int amt, String loreKey) {
        Locale l = p.getLocale();
        return ItemStackCreator.getStackHead(I18n.string(nameKey, l), texture, amt, I18n.lore(loreKey, l));
    }

    public static ItemStack.Builder getStackHead(HypixelPlayer p, String nameKey, String texture, int amt, String loreKey, Map<String, String> ph) {
        Locale l = p.getLocale();
        return ItemStackCreator.getStackHead(I18n.string(nameKey, l), texture, amt, I18n.lore(loreKey, l, ph));
    }

    public static ItemStack.Builder getStackHead(HypixelPlayer p, String nameKey, String texture, int amt, List<String> lore) {
        Locale l = p.getLocale();
        return ItemStackCreator.getStackHead(I18n.string(nameKey, l), texture, amt, lore);
    }

    public static ItemStack.Builder getStackHead(HypixelPlayer p, String nameKey, PlayerSkin skin, int amt, List<String> lore) {
        Locale l = p.getLocale();
        return ItemStackCreator.getStackHead(I18n.string(nameKey, l), skin, amt, lore);
    }
}
