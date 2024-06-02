package net.swofty.types.generic.gui.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.*;
import net.minestom.server.tag.Tag;
import net.swofty.types.generic.utility.ExtraItemTags;
import net.swofty.types.generic.utility.StringUtility;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
        for (String line : StringUtility.splitByWordAndLength(lore, 30))
            l.add(color + line);
        return getStack(name, material, data, amount, l.toArray(new String[]{}));
    }

    public static ItemStack.Builder getStack(String name, Material material, short data, int amount, String... lore) {
        return getStack(name, material, data, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder updateLore(ItemStack.Builder builder, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(color(s));
        }

        return builder.meta(meta -> {
            meta.lore(copiedLore.stream()
                    .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                    .collect(Collectors.toList()));

            meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
            meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            meta.hideFlag(ItemHideFlag.HIDE_POTION_EFFECTS);
            meta.hideFlag(ItemHideFlag.HIDE_UNBREAKABLE);
        });
    }

    public static ItemStack.Builder setNotEditable(ItemStack.Builder builder) {
        return builder.meta(meta -> {
            meta.setTag(Tag.Boolean("Uneditable"), true);
        });
    }

    public static ItemStack.Builder getStack(String name, Material material, int amount, String... lore) {
        return getStack(name, material, (short) 0, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder enchant(ItemStack.Builder builder) {
        ItemMeta metaToSet = builder.meta(meta -> {
            meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            meta.enchantment(Enchantment.EFFICIENCY, (short) 1);
        }).build().getMeta();

        return builder.meta(metaToSet);
    }

    public static ItemStack.Builder getFromStack(ItemStack stack) {
        return ItemStack.builder(stack.material()).meta(stack.meta()).amount(stack.amount())
                .displayName(stack.getDisplayName()).lore(stack.getLore());
    }

    public static ItemStack.Builder getStack(String name, Material material, int amount, List<String> lore) {
        return getStack(name, material, (short) 0, amount, lore);
    }

    public static ItemStack.Builder getStack(String name, Material material, int data, int amount, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(color(s));
        }

        return ItemStack.builder(material).amount(amount).lore(copiedLore.stream()
                .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList())).meta(meta -> {
            meta.damage(data);
            meta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false));
            meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
            meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            meta.hideFlag(ItemHideFlag.HIDE_POTION_EFFECTS);
            meta.hideFlag(ItemHideFlag.HIDE_UNBREAKABLE);
        });
    }

    public static ItemStack.Builder getStackHead(String name, String texture, int amount, String... lore) {
        return getStackHead(name, texture, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder getStackHead(String name, String texture) {
        return getStackHead(name, texture, 1, new ArrayList<>());
    }

    public static ItemStack.Builder getStackHead(String texture) {
        return getStackHead("", texture, 1, new ArrayList<>());
    }

    public static ItemStack.Builder getStackHead(String name, PlayerSkin skin, int amount, String... lore) {
        return getStackHead(name, skin, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder getStackHead(String name, String texture, int amount, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(color(s));
        }

        JSONObject json = new JSONObject();
        json.put("isPublic", true);
        json.put("signatureRequired", false);
        json.put("textures", new JSONObject().put("SKIN",
                new JSONObject().put("url", "http://textures.minecraft.net/texture/" + texture).put("metadata", new JSONObject().put("model", "slim"))));

        String texturesEncoded = Base64.getEncoder().encodeToString(json.toString().getBytes());

        return ItemStack.builder(Material.PLAYER_HEAD).meta(meta -> {
            meta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false));
            meta.damage(3);
            meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
            meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            meta.hideFlag(ItemHideFlag.HIDE_POTION_EFFECTS);
            meta.hideFlag(ItemHideFlag.HIDE_UNBREAKABLE);
            meta.set(ExtraItemTags.SKULL_OWNER, new ExtraItemTags.SkullOwner(null,
                    "25", new PlayerSkin(texturesEncoded, null)));
        }).amount(amount).lore(copiedLore.stream()
                .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList()));
    }


    public static ItemStack.Builder getStackHead(String name, PlayerSkin skin, int amount, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(color(s));
        }

        return ItemStack.builder(Material.PLAYER_HEAD).meta(meta -> {
            meta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false));
            meta.damage(3);
            meta.hideFlag(ItemHideFlag.HIDE_ATTRIBUTES);
            meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            meta.hideFlag(ItemHideFlag.HIDE_POTION_EFFECTS);
            meta.hideFlag(ItemHideFlag.HIDE_UNBREAKABLE);
            meta.set(ExtraItemTags.SKULL_OWNER, new ExtraItemTags.SkullOwner(null,
                    "25", skin));
        }).amount(amount).lore(copiedLore.stream()
                .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList()));
    }

    public static String color(String string) {
        return string.replace("&", "§");
    }
}
