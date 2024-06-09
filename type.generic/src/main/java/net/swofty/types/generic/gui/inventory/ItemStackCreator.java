package net.swofty.types.generic.gui.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.*;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.item.component.HeadProfile;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.Unit;
import net.swofty.types.generic.utility.StringUtility;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class ItemStackCreator {

    public static ItemStack.Builder createNamedItemStack(Material material, String name) {
        return ItemStack.builder(material)
                .set(ItemComponent.CUSTOM_NAME, Component.text(name).decoration(TextDecoration.ITALIC, false))
                .set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
    }

    public static ItemStack.Builder createNamedItemStack(Material material) {
        return createNamedItemStack(material, "");
    }

    public static ItemStack.Builder getSingleLoreStack(String name, String color, Material material, short data, int amount, String lore) {
        List<String> l = new ArrayList<>();
        for (String line : StringUtility.splitByWordAndLength(lore, 30))
            l.add(color + line);
        return getStack(name, material, amount, l.toArray(new String[]{}));
    }

    public static ItemStack.Builder getStack(String name, Material material, int amount, String... lore) {
        return getStack(name, material, amount, Arrays.asList(lore));
    }

    public static ItemStack.Builder updateLore(ItemStack.Builder builder, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(color(s));
        }

        return builder.set(ItemComponent.LORE, copiedLore.stream()
                .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList()))
                .set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
    }

    public static ItemStack.Builder setNotEditable(ItemStack.Builder builder) {
        return builder.set(Tag.Boolean("uneditable"), true);
    }

    public static ItemStack.Builder enchant(ItemStack.Builder builder) {
        return builder.set(ItemComponent.ENCHANTMENT_GLINT_OVERRIDE, true)
                .set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
    }

    public static ItemStack.Builder getFromStack(ItemStack stack) {
        return ItemStack.builder(stack.material())
                .amount(stack.amount())
                .set(ItemComponent.LORE, stack.get(ItemComponent.LORE))
                .set(ItemComponent.CUSTOM_NAME, stack.get(ItemComponent.CUSTOM_NAME))
                .set(ItemComponent.CUSTOM_DATA, stack.get(ItemComponent.CUSTOM_DATA));
    }

    public static ItemStack.Builder getStack(String name, Material material, int amount, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(color(s));
        }

        return ItemStack.builder(material).amount(amount).set(ItemComponent.LORE, copiedLore.stream()
                .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                .collect(Collectors.toList()))
                .set(ItemComponent.CUSTOM_NAME, Component.text(name).decoration(TextDecoration.ITALIC, false))
                .set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
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

        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(ItemComponent.LORE, copiedLore.stream()
                        .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()))
                .set(ItemComponent.CUSTOM_NAME, Component.text(name).decoration(TextDecoration.ITALIC, false))
                .set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
                .set(ItemComponent.PROFILE, new HeadProfile(new PlayerSkin(texturesEncoded, null)))
                .amount(amount);
    }


    public static ItemStack.Builder getStackHead(String name, PlayerSkin skin, int amount, List<String> lore) {
        List<String> copiedLore = new ArrayList<>();
        for (String s : lore) {
            copiedLore.add(color(s));
        }

        return ItemStack.builder(Material.PLAYER_HEAD)
                .set(ItemComponent.LORE, copiedLore.stream()
                        .map(line -> Component.text(line).decoration(TextDecoration.ITALIC, false))
                        .collect(Collectors.toList()))
                .set(ItemComponent.CUSTOM_NAME, Component.text(name).decoration(TextDecoration.ITALIC, false))
                .set(ItemComponent.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
                .set(ItemComponent.PROFILE, new HeadProfile(skin))
                .amount(amount);
    }

    public static String color(String string) {
        return string.replace("&", "§");
    }
}
