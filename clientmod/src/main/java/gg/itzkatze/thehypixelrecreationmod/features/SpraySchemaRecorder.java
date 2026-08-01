package gg.itzkatze.thehypixelrecreationmod.features;

import gg.itzkatze.thehypixelrecreationmod.mixin.HandledScreenAccessor;
import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.ItemStackUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

public final class SpraySchemaRecorder {
    private static final int PREVIEW_CAPTURE_TICKS = 200;
    private static final int PREVIEW_CAPTURE_DELAY_TICKS = 20;
    private static final double ITEM_FRAME_SEARCH_RADIUS = 32.0;

    private static boolean listening = false;
    private static boolean armedInSpraysMenu = false;
    private static final List<SprayEntry> entries = new ArrayList<>();
    private static PendingPreview pendingPreview;

    private SpraySchemaRecorder() {
    }

    public static void start() {
        entries.clear();
        entries.addAll(readExistingEntries(outputPath()));
        listening = true;
        armedInSpraysMenu = false;
        pendingPreview = null;
        writeFile();
        ChatUtils.message("Listening for sprays. Open a menu with 'Sprays' in the title, then right-click spray items.");
    }

    public static void stop() {
        listening = false;
        armedInSpraysMenu = false;
        pendingPreview = null;
        writeFile();
        ChatUtils.message("Stopped listening for sprays. Wrote " + entries.size() + " entries to sprays.yml.");
    }

    public static boolean isListening() {
        return listening;
    }

    public static void tick() {
        if (!listening || pendingPreview == null) return;

        if (pendingPreview.delayTicks > 0) {
            pendingPreview.delayTicks--;
            return;
        }

        pendingPreview.ticksRemaining--;
        Optional<String> mapData = captureNearestMapData();
        if (mapData.isPresent()) {
            pendingPreview.entry.item = mapData.get();
            writeFile();
            ChatUtils.message("Captured preview map data for " + pendingPreview.entry.name + ".");
            pendingPreview = null;
            return;
        }

        if (pendingPreview.ticksRemaining <= 0) {
            writeFile();
            ChatUtils.warn("No preview map item frame found for " + pendingPreview.entry.name + "; entry was saved without item data.");
            pendingPreview = null;
        }
    }

    public static void handleContainerRightClick(Screen screen) {
        Minecraft client = Minecraft.getInstance();
        if (!listening || client.player == null) return;
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return;

        String title = StringUtility.stripColor(StringUtility.toLegacyString(containerScreen.getTitle()));
        if (!armedInSpraysMenu) {
            if (!title.toLowerCase(Locale.ROOT).contains("sprays")) return;
            armedInSpraysMenu = true;
            ChatUtils.message("Sprays menu detected. Right-clicked spray items will be added to sprays.yml.");
        }

        Slot hoveredSlot = ((HandledScreenAccessor) containerScreen).getHoveredSlot();
        if (hoveredSlot == null || !hoveredSlot.hasItem()) return;

        SprayEntry entry = fromStack(hoveredSlot.getItem());
        if (entry == null) return;

        int existingIndex = findEntryIndex(entry.id);
        if (existingIndex >= 0) {
            entry.item = entries.get(existingIndex).item;
            entries.set(existingIndex, entry);
        } else {
            entries.add(entry);
        }

        pendingPreview = new PendingPreview(entry, PREVIEW_CAPTURE_DELAY_TICKS, PREVIEW_CAPTURE_TICKS);
        writeFile();
        ChatUtils.message("Added spray schema entry for " + entry.name + ". Waiting for preview map data.");
    }

    private static SprayEntry fromStack(ItemStack stack) {
        String displayName = ItemStackUtils.getDisplayNameLegacy(stack);
        String cleanName = StringUtility.stripColor(displayName).trim();
        if (cleanName.isEmpty()) return null;

        SprayEntry entry = new SprayEntry();
        entry.id = toId(cleanName);
        entry.name = cleanName;
        entry.material = ItemStackUtils.toMinestomMaterial(stack);
        entry.texture = stack.is(Items.PLAYER_HEAD) ? ItemStackUtils.getPlayerHeadTexture(stack) : "";
        entry.currency = "TOKEN";
        entry.cost = 0;
        entry.rarity = "COMMON";
        entry.item = "";

        for (String loreLine : ItemStackUtils.getLoreAsStrings(stack)) {
            String cleanLore = StringUtility.stripColor(loreLine).trim();
            if (cleanLore.toUpperCase(Locale.ROOT).startsWith("RARITY:")) {
                entry.rarity = cleanLore.substring(cleanLore.indexOf(':') + 1).trim().toUpperCase(Locale.ROOT);
            } else if (cleanLore.toUpperCase(Locale.ROOT).startsWith("COST:")) {
                entry.cost = parseCost(cleanLore);
            }
        }

        return entry;
    }

    private static Optional<String> captureNearestMapData() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return Optional.empty();

        AABB searchBox = client.player.getBoundingBox().inflate(ITEM_FRAME_SEARCH_RADIUS);
        List<ItemFrame> frames = client.level.getEntitiesOfClass(ItemFrame.class, searchBox);
        for (ItemFrame frame : frames) {
            ItemStack stack = frame.getItem();
            if (!(stack.getItem() instanceof MapItem)) continue;

            MapId id = stack.get(DataComponents.MAP_ID);
            if (id == null) continue;

            MapItemSavedData data = client.level.getMapData(id);
            if (data == null || data.colors.length != 128 * 128) continue;

            try {
                return Optional.of(compressAndEncode(data.colors));
            } catch (IOException ignored) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private static int findEntryIndex(String id) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).id.equals(id)) return i;
        }
        return -1;
    }

    private static int parseCost(String cleanLore) {
        String digits = cleanLore.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private static String toId(String name) {
        String id = name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
        return id.isEmpty() ? "spray" : id;
    }

    private static Path outputPath() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve("sprays.yml");
    }

    private static void writeFile() {
        Path path = outputPath();
        try {
            Files.writeString(path, toYaml());
        } catch (IOException e) {
            ChatUtils.error("Failed to write sprays.yml: " + e.getMessage());
        }
    }

    private static String toYaml() {
        StringBuilder yaml = new StringBuilder();
        yaml.append("SPRAYS:\n");
        yaml.append("  items:\n");

        for (SprayEntry entry : entries) {
            yaml.append("    - id: ").append(quote(entry.id)).append('\n');
            yaml.append("      name: ").append(quote(entry.name)).append('\n');
            yaml.append("      material: ").append(entry.material).append('\n');
            if (entry.texture != null && !entry.texture.isEmpty()) {
                yaml.append("      texture: ").append(quote(entry.texture)).append('\n');
            }
            yaml.append("      currency: ").append(entry.currency).append('\n');
            yaml.append("      cost: ").append(entry.cost).append('\n');
            yaml.append("      rarity: ").append(entry.rarity).append('\n');
            yaml.append("      item: ").append(quote(entry.item == null ? "" : entry.item)).append('\n');
        }

        return yaml.toString();
    }

    private static List<SprayEntry> readExistingEntries(Path path) {
        List<SprayEntry> loaded = new ArrayList<>();
        if (!Files.exists(path)) return loaded;

        try {
            SprayEntry current = null;
            for (String rawLine : Files.readAllLines(path)) {
                String line = rawLine.trim();
                if (line.startsWith("- id:")) {
                    if (current != null && current.id != null) loaded.add(current);
                    current = new SprayEntry();
                    current.id = unquote(value(line));
                } else if (current != null && line.contains(":")) {
                    String key = line.substring(0, line.indexOf(':')).trim();
                    String rawValue = value(line);
                    applyYamlValue(current, key, unquote(rawValue));
                }
            }
            if (current != null && current.id != null) loaded.add(current);
        } catch (IOException e) {
            ChatUtils.warn("Could not read existing sprays.yml: " + e.getMessage());
        }

        return loaded;
    }

    private static void applyYamlValue(SprayEntry entry, String key, String value) {
        switch (key) {
            case "name" -> entry.name = value;
            case "material" -> entry.material = value;
            case "texture" -> entry.texture = value;
            case "currency" -> entry.currency = value;
            case "cost" -> entry.cost = parseCost(value);
            case "rarity" -> entry.rarity = value;
            case "item" -> entry.item = value;
            default -> {
            }
        }
    }

    private static String value(String line) {
        return line.substring(line.indexOf(':') + 1).trim();
    }

    private static String quote(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static String unquote(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static String compressAndEncode(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzos = new GZIPOutputStream(baos)) {
            gzos.write(data);
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static class PendingPreview {
        private final SprayEntry entry;
        private int delayTicks;
        private int ticksRemaining;

        private PendingPreview(SprayEntry entry, int delayTicks, int ticksRemaining) {
            this.entry = entry;
            this.delayTicks = delayTicks;
            this.ticksRemaining = ticksRemaining;
        }
    }

    private static class SprayEntry {
        private String id = "";
        private String name = "";
        private String material = "STONE";
        private String texture = "";
        private String currency = "TOKEN";
        private int cost = 0;
        private String rarity = "COMMON";
        private String item = "";
    }
}
