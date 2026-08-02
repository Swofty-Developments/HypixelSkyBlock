package net.swofty.type.ravengardgeneric.hud;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.tinylog.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class RavengardHudComposer {
    private static final Gson GSON = new Gson();
    private static final Map<String, String> TEMPLATES = new HashMap<>();

    static {
        try (InputStream stream = RavengardHudComposer.class.getResourceAsStream("/hud/lines.json")) {
            if (stream == null) {
                throw new IllegalStateException("Missing /hud/lines.json");
            }
            JsonObject root = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
            for (String team : root.keySet()) {
                TEMPLATES.put(team, GSON.toJson(root.get(team)));
            }
            Logger.info("Loaded {} Ravengard HUD line templates", TEMPLATES.size());
        } catch (Exception exception) {
            Logger.error(exception, "Failed to load Ravengard HUD line templates");
        }
    }

    private RavengardHudComposer() {
    }

    public static Map<RavengardHudLayer, Component> compose(RavengardHudState state) {
        Map<RavengardHudLayer, Component> layers = new EnumMap<>(RavengardHudLayer.class);
        for (RavengardHudLayer layer : RavengardHudLayer.values()) {
            if ("team_5".equals(layer.getTeamName())) {
                layers.put(layer, spellSlots(state));
                continue;
            }
            String template = TEMPLATES.get(layer.getTeamName());
            if (template == null) {
                continue;
            }
            layers.put(layer, render(template, state));
        }
        return layers;
    }

    // structure taken from the captured team_5 line: per slot a spell background glyph whose tint
    // id addresses the slot, with the ability's ui/spell glyph as a child
    private static final int SPELL_BACKGROUND = 0xE020;
    private static final int SLOT_ONE_BACKGROUND = 0xFEFBFF;
    private static final int SLOT_ONE_ICON = 0xFEFB00;
    private static final int SLOT_TWO_BACKGROUND = 0xFEFCFF;
    private static final int SLOT_TWO_ICON = 0xFEFC00;

    private static Component spellSlots(RavengardHudState state) {
        if (state.getAbilityOne() == 0 && state.getAbilityTwo() == 0) {
            return Component.empty();
        }
        Component line = Component.empty();
        if (state.getAbilityOne() != 0) {
            line = line.append(Component.text(new String(Character.toChars(SPELL_BACKGROUND)))
                    .color(net.kyori.adventure.text.format.TextColor.color(SLOT_ONE_BACKGROUND))
                    .append(Component.text(new String(Character.toChars(state.getAbilityOne())))
                            .color(net.kyori.adventure.text.format.TextColor.color(SLOT_ONE_ICON))));
        }
        if (state.getAbilityTwo() != 0) {
            line = line.append(Component.text(new String(Character.toChars(SPELL_BACKGROUND)))
                    .color(net.kyori.adventure.text.format.TextColor.color(SLOT_TWO_BACKGROUND))
                    .append(Component.text(new String(Character.toChars(state.getAbilityTwo())))
                            .color(net.kyori.adventure.text.format.TextColor.color(SLOT_TWO_ICON))));
        }
        return line;
    }

    private static final double MAP_SCALE = 4.0;
    private static final double MAP_ORIGIN_X = 1931.56;
    private static final double MAP_ORIGIN_Z = 1894.40;
    private static final int MARKER_ICON = 255;
    private static final double YAW_PER_STEP = 360.0 / 64.0;

    public static int mapTint(RavengardHudState state) {
        int x = clamp12((int) Math.round(state.getWorldX() * MAP_SCALE + MAP_ORIGIN_X));
        int z = clamp12((int) Math.round(state.getWorldZ() * MAP_SCALE + MAP_ORIGIN_Z));
        return (x << 12) | z;
    }

    public static int markerTint(RavengardHudState state) {
        float yaw = state.getYaw();
        while (yaw < 0) {
            yaw += 360f;
        }
        int rotation = ((int) Math.round(yaw / YAW_PER_STEP)) & 0x3F;
        return (MARKER_ICON << 15) | (MARKER_ICON << 6) | rotation;
    }

    private static int clamp12(int value) {
        return Math.max(0, Math.min(0xFFF, value));
    }

    private static Component render(String template, RavengardHudState state) {
        String filled = template
                .replace("${health}", state.healthText())
                .replace("${stamina}", state.staminaText())
                .replace("${clock}", state.getClock())
                .replace("${location}", state.getLocation())
                .replace("${date}", state.getDate())
                .replace("${server}", state.getServerId())
                .replace("#860794", String.format("#%06X", mapTint(state)))
                .replace("#7FBFE0", String.format("#%06X", markerTint(state)));

        JsonElement tree = GSON.fromJson(filled, JsonElement.class);
        return GsonComponentSerializer.gson().deserializeFromTree(tree);
    }
}
