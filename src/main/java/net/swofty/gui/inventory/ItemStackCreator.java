package net.swofty.gui.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemStackCreator {

    public static ItemStack.Builder createNamedItemStack(Material material, String name) {
        return ItemStack.builder(material).displayName(Component.text(name)).meta(meta -> {
            meta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false));
            meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
            meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            meta.hideFlag(ItemHideFlag.HIDE_POTION_EFFECTS);
            meta.hideFlag(ItemHideFlag.HIDE_UNBREAKABLE);
        });
    }
    public static ItemStack.Builder createNamedItemStack(Material material) {
        return createNamedItemStack(material, "");
    }

    public static ItemStack.Builder getSingleLoreStack(String name, String color, Material material, short data, int amount, String lore) {
        List<String> l = new ArrayList<>();
        for (String line : StringUtility.splitByWordAndLength(lore, 30, "\\s"))
            l.add(color + line);
        return getStack(name, material, data, amount, l.toArray(new String[]{}));
    }

    public static ItemStack.Builder getStack(String name, Material material, short data, int amount, String... lore) {
        return getStack(name, material, data, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder getStack(String name, Material material, int data, int amount, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(color(s));
        }

        return ItemStack.builder(material).meta(meta -> {
            meta.damage(data);
            meta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false));
            meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
            meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            meta.hideFlag(ItemHideFlag.HIDE_POTION_EFFECTS);
            meta.hideFlag(ItemHideFlag.HIDE_UNBREAKABLE);
        }).amount(amount).lore(copiedLore.stream()
                .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList()));
    }

    public static String color(String string) {
        return string.replace("&", "§");
    }
}
