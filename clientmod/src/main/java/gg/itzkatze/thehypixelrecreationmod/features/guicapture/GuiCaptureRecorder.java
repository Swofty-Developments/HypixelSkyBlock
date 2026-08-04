package gg.itzkatze.thehypixelrecreationmod.features.guicapture;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GuiCaptureRecorder {
    private static final Path CAPTURE_DIR = FabricLoader.getInstance().getGameDir().resolve("gui-captures");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static boolean active;
    private static long startedAt;
    private static String currentScreenKey;
    private static final Map<Integer, ItemStack> lastSnapshot = new HashMap<>();
    private static JsonArray screens;
    private static JsonArray changes;
    private static int changeCount;
    private static int screenCount;

    private GuiCaptureRecorder() {
    }

    public static boolean isActive() {
        return active;
    }

    public static void toggle(Minecraft client) {
        if (active) {
            stop();
        } else {
            start(client);
        }
    }

    private static void start(Minecraft client) {
        active = true;
        startedAt = System.currentTimeMillis();
        currentScreenKey = null;
        lastSnapshot.clear();
        screens = new JsonArray();
        changes = new JsonArray();
        changeCount = 0;
        screenCount = 0;

        ChatUtils.message("§aGUI capture started — press B again to stop. Navigate menus freely.");
        tick(client);
    }

    private static void stop() {
        active = false;

        JsonObject root = new JsonObject();
        root.addProperty("startedAt", startedAt);
        root.addProperty("durationMs", System.currentTimeMillis() - startedAt);
        root.add("screens", screens);
        root.add("changes", changes);

        try {
            Files.createDirectories(CAPTURE_DIR);
            Path path = CAPTURE_DIR.resolve("gui_" + LocalDateTime.now().format(FILE_FORMAT) + ".json");
            Files.writeString(path, GSON.toJson(root), StandardCharsets.UTF_8);
            ChatUtils.message("§aGUI capture stopped: " + screenCount + " screens, "
                    + changeCount + " slot changes → §f" + path);
        } catch (IOException exception) {
            ChatUtils.error("Failed to write GUI capture: " + exception.getMessage());
        }

        screens = null;
        changes = null;
        lastSnapshot.clear();
    }

    public static void tick(Minecraft client) {
        if (!active || client.player == null) {
            return;
        }
        if (!(client.gui.screen() instanceof AbstractContainerScreen<?> screen)) {
            return;
        }

        List<Slot> slots = containerSlots(screen);
        String title = StringUtility.toLegacyString(screen.getTitle());
        String key = screen.getMenu().containerId + "|" + title + "|" + slots.size();

        if (!key.equals(currentScreenKey)) {
            currentScreenKey = key;
            screenCount++;
            lastSnapshot.clear();

            JsonObject entry = new JsonObject();
            entry.addProperty("atMs", System.currentTimeMillis() - startedAt);
            entry.addProperty("containerId", screen.getMenu().containerId);
            entry.addProperty("title", title);
            entry.add("titleJson", componentJson(screen.getTitle()));
            entry.addProperty("slotCount", slots.size());
            entry.addProperty("inventoryType", inventoryType(slots.size()));

            JsonObject initial = new JsonObject();
            for (Slot slot : slots) {
                ItemStack stack = slot.getItem();
                lastSnapshot.put(slot.index, stack.copy());
                if (!stack.isEmpty()) {
                    initial.add(String.valueOf(slot.index), describe(stack));
                }
            }
            entry.add("slots", initial);
            screens.add(entry);
            return;
        }

        for (Slot slot : slots) {
            ItemStack now = slot.getItem();
            ItemStack before = lastSnapshot.get(slot.index);
            if (before != null && ItemStack.matches(before, now)) {
                continue;
            }

            lastSnapshot.put(slot.index, now.copy());
            changeCount++;

            JsonObject change = new JsonObject();
            change.addProperty("atMs", System.currentTimeMillis() - startedAt);
            change.addProperty("containerId", screen.getMenu().containerId);
            change.addProperty("slot", slot.index);
            change.add("from", before == null || before.isEmpty() ? JsonNull.INSTANCE : describe(before));
            change.add("to", now.isEmpty() ? JsonNull.INSTANCE : describe(now));
            changes.add(change);
        }
    }

    private static JsonObject describe(ItemStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());
        json.addProperty("name", StringUtility.toLegacyString(stack.getHoverName()));

        JsonObject components = new JsonObject();
        for (TypedDataComponent<?> component : stack.getComponents()) {
            String key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(component.type()) == null
                    ? component.type().toString()
                    : BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(component.type()).toString();
            components.add(key, encode(component));
        }
        json.add("components", components);
        return json;
    }

    private static JsonElement encode(TypedDataComponent<?> component) {
        try {
            return component.encodeValue(JsonOps.INSTANCE)
                    .result()
                    .orElseGet(() -> new JsonPrimitive(String.valueOf(component.value())));
        } catch (Exception exception) {
            return new JsonPrimitive(String.valueOf(component.value()));
        }
    }

    private static JsonElement componentJson(Component component) {
        try {
            return ComponentSerialization.CODEC
                    .encodeStart(RegistryOps.create(JsonOps.INSTANCE,
                            Minecraft.getInstance().level.registryAccess()), component)
                    .result()
                    .orElseGet(() -> new JsonPrimitive(StringUtility.toLegacyString(component)));
        } catch (Exception exception) {
            return new JsonPrimitive(StringUtility.toLegacyString(component));
        }
    }

    private static List<Slot> containerSlots(AbstractContainerScreen<?> screen) {
        var slots = screen.getMenu().slots;
        int containerSize = Math.max(0, slots.size() - 36);
        if (containerSize == 0) {
            containerSize = slots.size();
        }
        return slots.subList(0, Math.min(containerSize, slots.size()));
    }

    private static String inventoryType(int slotCount) {
        return switch (slotCount) {
            case 9 -> "CHEST_1_ROW";
            case 18 -> "CHEST_2_ROW";
            case 27 -> "CHEST_3_ROW";
            case 36 -> "CHEST_4_ROW";
            case 45 -> "CHEST_5_ROW";
            case 54 -> "CHEST_6_ROW";
            default -> "UNKNOWN_" + slotCount;
        };
    }
}
