package gg.itzkatze.thehypixelrecreationmod.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ItemLore;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ItemStackUtils {
    private static final String TEXTURES_PROPERTY = "textures";

    private ItemStackUtils() {
    }

    /**
     * Raw textures property from a player head.
     *
     * @param valueBase64 the base64-encoded texture JSON (Property#value)
     * @param signatureBase64 optional Mojang signature (Property#signature)
     */
    public record HeadTextureProperty(String valueBase64, @Nullable String signatureBase64) {
    }

    /**
     * Extracts the raw "textures" property from a player head.
     * This is what you want if you need the base64 texture JSON to paste into NBT/JSON.
     */
    public static @Nullable HeadTextureProperty getPlayerHeadTextureProperty(ItemStack stack) {
        if (!stack.is(Items.PLAYER_HEAD)) {
            return null;
        }

        var profileComponent = stack.get(DataComponents.PROFILE);
        if (profileComponent == null) {
            return null;
        }

        GameProfile profile = profileComponent.partialProfile();
        if (profile == null) {
            return null;
        }

        Property textureProp = profile.properties()
                .get(TEXTURES_PROPERTY)
                .stream()
                .findFirst()
                .orElse(null);

        if (textureProp == null || textureProp.value() == null || textureProp.value().isEmpty()) {
            return null;
        }

        String signature = textureProp.signature();
        if (signature != null && signature.isBlank()) {
            signature = null;
        }

        return new HeadTextureProperty(textureProp.value(), signature);
    }

    /**
     * Builds a JSON snippet for the "properties" shape commonly used by head-creation APIs.
     * Result looks like:
     * {
     *   "properties": [ { "name":"textures","value":"...","signature":"..." } ]
     * }
     */
    public static String buildTexturesPropertiesJson(HeadTextureProperty prop, boolean includeSignatureIfPresent) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"properties\":[{\"name\":\"textures\",\"value\":\"")
                .append(StringUtility.escapeJson(prop.valueBase64()))
                .append("\"");

        if (includeSignatureIfPresent && prop.signatureBase64() != null && !prop.signatureBase64().isEmpty()) {
            sb.append(",\"signature\":\"")
                    .append(StringUtility.escapeJson(prop.signatureBase64()))
                    .append("\"");
        }

        sb.append("}]}");
        return sb.toString();
    }

    public static String getPlayerHeadTexture(ItemStack stack) {
        String textureID = "";

        if (!stack.is(Items.PLAYER_HEAD)) {
            return textureID;
        }

        var prop = getPlayerHeadTextureProperty(stack);
        if (prop == null) {
            ChatUtils.message("No texture property found.");
            return textureID;
        }

        try {
            String json = new String(Base64.getDecoder().decode(prop.valueBase64()), StandardCharsets.UTF_8);
            int start = json.indexOf("http");
            String url = (start >= 0) ? json.substring(start, json.indexOf('"', start)) : "<no URL>";

            if (url.equals("<no URL>")) {
                ChatUtils.message("No URL found in decoded texture.");
                return textureID;
            }

            String[] parts = url.split("/");
            textureID = parts[parts.length - 1];

            return textureID;

        } catch (Exception ex) {
            ChatUtils.message("Error decoding texture: " + ex.getMessage());
        }
        return textureID;
    }

    /**
     * Gets the lore from an ItemStack as a list of Text components.
     * Returns an empty list if no lore is present.
     */
    public static List<Component> getLore(ItemStack stack) {
        ItemLore loreComponent = stack.get(DataComponents.LORE);
        if (loreComponent == null) {
            return List.of();
        }
        return loreComponent.lines();
    }

    /**
     * Gets the lore from an ItemStack as a list of plain strings.
     * Returns an empty list if no lore is present.
     */
    public static List<String> getLoreAsStrings(ItemStack stack) {
        return getLore(stack).stream()
                .map(StringUtility::toLegacyString)
                .toList();
    }

    public static String getDisplayNameLegacy(ItemStack stack) {
        Component customName = stack.get(DataComponents.CUSTOM_NAME);
        if (customName != null) {
            return StringUtility.toLegacyString(customName);
        }
        // fallback
        return stack.getHoverName().getString();
    }

    public static String toMinestomMaterial(ItemStack stack) {
        String id = stack.getItem().builtInRegistryHolder().unwrapKey()
                .map(key -> key.identifier().getPath())
                .orElse("stone");
        return id.toUpperCase();
    }
}
