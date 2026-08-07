package gg.itzkatze.thehypixelrecreationmod.features.hudcapture;

import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import gg.itzkatze.thehypixelrecreationmod.utils.StringUtility;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class HudCaptureRecorder {
    private static final Path CAPTURE_DIR = FabricLoader.getInstance().getGameDir().resolve("hud-captures");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private static BufferedWriter writer;
    private static Path sessionDir;
    private static Path logPath;
    private static int eventCount;
    private static int mapDumpCount;
    private static final Set<String> seenLines = new LinkedHashSet<>();
    private static boolean dedupe = true;

    private HudCaptureRecorder() {
    }

    public static boolean isActive() {
        return writer != null;
    }

    public static StartResult start(boolean deduplicate) throws IOException {
        if (isActive()) {
            throw new IllegalStateException("A HUD capture session is already active.");
        }

        GlyphAtlas.reload();
        dedupe = deduplicate;
        eventCount = 0;
        mapDumpCount = 0;
        seenLines.clear();

        sessionDir = CAPTURE_DIR.resolve("hud_" + LocalDateTime.now().format(FILE_FORMAT));
        Files.createDirectories(sessionDir);
        logPath = sessionDir.resolve("hud.log");
        writer = Files.newBufferedWriter(logPath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

        line("# hud capture started " + LocalDateTime.now());
        line("# glyph atlas entries: " + GlyphAtlas.size());
        line("# dedupe identical payloads: " + dedupe);
        line("");
        flush();

        return new StartResult(GlyphAtlas.size(), logPath);
    }

    public static StopResult stop() {
        if (!isActive()) {
            throw new IllegalStateException("No HUD capture session is active.");
        }

        line("");
        line("# stopped " + LocalDateTime.now() + " after " + eventCount + " events, " + mapDumpCount + " map dumps");
        try {
            writer.close();
        } catch (IOException ignored) {
        }
        writer = null;

        StopResult result = new StopResult(eventCount, mapDumpCount, sessionDir);
        sessionDir = null;
        logPath = null;
        seenLines.clear();
        return result;
    }

    public static void record(Packet<?> packet) {
        if (writer == null) {
            return;
        }

        String timestamp = LocalTime.now().format(TIME_FORMAT);
        Minecraft.getInstance().execute(() -> {
            if (writer == null) {
                return;
            }
            try {
                handle(timestamp, packet);
            } catch (Exception exception) {
                ChatUtils.error("HUD capture failed on " + packet.getClass().getSimpleName()
                        + ": " + exception.getMessage());
            }
        });
    }

    private static final Set<String> SKIP = Set.of(
            "ClientboundLevelChunkWithLightPacket", "ClientboundLightUpdatePacket",
            "ClientboundForgetLevelChunkPacket", "ClientboundBlockUpdatePacket",
            "ClientboundSectionBlocksUpdatePacket", "ClientboundSetEntityDataPacket",
            "ClientboundMoveEntityPacket", "ClientboundTeleportEntityPacket",
            "ClientboundEntityPositionSyncPacket", "ClientboundSetEntityMotionPacket",
            "ClientboundRotateHeadPacket", "ClientboundSoundPacket", "ClientboundSoundEntityPacket",
            "ClientboundSetTimePacket", "ClientboundKeepAlivePacket", "ClientboundBundlePacket",
                        "ClientboundLevelParticlesPacket", "ClientboundBlockEntityDataPacket",
            "ClientboundUpdateAttributesPacket",
            "ClientboundSetEquipmentPacket", "ClientboundAnimatePacket",
            "ClientboundSetPassengersPacket", "ClientboundUpdateAdvancementsPacket");

    private static void handle(String timestamp, Packet<?> packet) {
        if (packet instanceof ClientboundMapItemDataPacket map) {
            dumpMap(timestamp, map);
            return;
        }

        String name = packet.getClass().getSimpleName();
        if (SKIP.contains(name)) {
            return;
        }
        emitRaw(timestamp, name.replace("Clientbound", "").replace("Packet", ""), describeValue(packet, 0));
    }

    private static String describeValue(Object value, int depth) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Component component) {
            return "\"" + StringUtility.toLegacyString(component) + "\" json=" + componentJson(component);
        }
        if (value instanceof Optional<?> optional) {
            return optional.isEmpty() ? "-" : describeValue(optional.get(), depth);
        }
        if (value instanceof String || value instanceof Number || value instanceof Boolean
                || value instanceof UUID || value.getClass().isEnum()) {
            return String.valueOf(value);
        }
        if (depth > 3) {
            return String.valueOf(value);
        }
        if (value instanceof Iterable<?> iterable) {
            StringBuilder builder = new StringBuilder("[");
            for (Object element : iterable) {
                if (builder.length() > 1) {
                    builder.append(", ");
                }
                builder.append(describeValue(element, depth + 1));
            }
            return builder.append(']').toString();
        }

        StringBuilder builder = new StringBuilder();
        for (Field field : allFields(value.getClass())) {
            Object read = read(field, value);
            if (read == null) {
                continue;
            }
            String rendered = describeValue(read, depth + 1);
            if (rendered.equals("-") || rendered.isEmpty()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(field.getName()).append('=').append(rendered);
        }
        return builder.isEmpty() ? value.getClass().getSimpleName() : builder.toString();
    }

    private static String describeBossEvent(ClientboundBossEventPacket packet) {
        StringBuilder builder = new StringBuilder();
        for (Field field : allFields(packet.getClass())) {
            Object value = read(field, packet);
            if (value instanceof UUID uuid) {
                builder.append("id=").append(uuid).append(' ');
            } else if (value != null && !(value instanceof String)) {
                builder.append(describeOperation(value));
            }
        }
        return builder.toString().trim();
    }

    private static String describeOperation(Object operation) {
        StringBuilder builder = new StringBuilder();
        builder.append("op=").append(operation.getClass().getSimpleName()).append(' ');
        for (Field field : allFields(operation.getClass())) {
            Object value = read(field, operation);
            if (value == null) {
                continue;
            }
            if (value instanceof Component component) {
                builder.append("name=\"").append(StringUtility.toLegacyString(component)).append("\" ");
            } else if (value instanceof Float || value instanceof Boolean || value instanceof Integer) {
                builder.append(field.getName()).append('=').append(value).append(' ');
            } else if (value.getClass().isEnum()) {
                builder.append(field.getName()).append('=').append(value).append(' ');
            }
        }
        return builder.toString();
    }

    private static List<Field> allFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> current = type; current != null && current != Object.class; current = current.getSuperclass()) {
            Collections.addAll(fields, current.getDeclaredFields());
        }
        return fields;
    }

    private static Object read(Field field, Object owner) {
        if (Modifier.isStatic(field.getModifiers())) {
            return null;
        }
        try {
            field.setAccessible(true);
            return field.get(owner);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            return null;
        }
    }

    private static void emit(String timestamp, String channel, Component component) {
        if (component == null) {
            return;
        }
        emitRaw(timestamp, channel, StringUtility.toLegacyString(component));
    }

    private static void emitRaw(String timestamp, String channel, String payload) {
        if (payload == null || payload.isEmpty()) {
            return;
        }
        String key = channel + ' ' + payload;
        if (dedupe && !seenLines.add(key)) {
            return;
        }

        eventCount++;
        line("[" + timestamp + "] " + channel + " @" + playerPosition() + " | \"" + escape(payload) + "\"");
        for (int codePoint : payload.codePoints().toArray()) {
            if (codePoint < 0x2000) {
                continue;
            }
            GlyphAtlas.Entry entry = GlyphAtlas.lookup(codePoint);
            if (entry != null) {
                line(String.format("    \\u%04X = %s (ascent %d, h %d)",
                        codePoint, entry.file(), entry.ascent(), entry.height()));
            } else {
                line(String.format("    \\u%04X = <no bitmap provider>", codePoint));
            }
        }
        flush();
    }

    private static void dumpMap(String timestamp, ClientboundMapItemDataPacket packet) {
        if (packet.colorPatch().isEmpty()) {
            line("[" + timestamp + "] MapData | id=" + packet.mapId().id() + " (no colour patch)");
            flush();
            return;
        }

        MapItemSavedData.MapPatch patch = packet.colorPatch().get();
        eventCount++;
        mapDumpCount++;
        String name = "map_" + packet.mapId().id() + "_" + mapDumpCount + ".png";
        line("[" + timestamp + "] MapData | id=" + packet.mapId().id()
                + " patch=" + patch.width() + "x" + patch.height()
                + " at " + patch.startX() + "," + patch.startY()
                + " scale=" + packet.scale() + " -> " + name);

        try {
            BufferedImage image = new BufferedImage(patch.width(), patch.height(), BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < patch.width(); x++) {
                for (int y = 0; y < patch.height(); y++) {
                    byte colorId = patch.mapColors()[x + y * patch.width()];
                    image.setRGB(x, y, net.minecraft.world.level.material.MapColor
                            .getColorFromPackedId(colorId & 0xFF));
                }
            }
            ImageIO.write(image, "png", sessionDir.resolve(name).toFile());
        } catch (Exception exception) {
            line("    <failed to write png: " + exception.getMessage() + ">");
        }
        flush();
    }

    private static String componentJson(Component component) {
        try {
            var level = Minecraft.getInstance().level;
            if (level == null) {
                return "<no level>";
            }
            return net.minecraft.network.chat.ComponentSerialization.CODEC
                    .encodeStart(net.minecraft.resources.RegistryOps.create(
                            com.mojang.serialization.JsonOps.INSTANCE, level.registryAccess()), component)
                    .result()
                    .map(Object::toString)
                    .orElse("<unencodable>");
        } catch (Exception exception) {
            return "<error:" + exception.getMessage() + ">";
        }
    }

    private static String playerPosition() {
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return "?";
        }
        return String.format("%.2f,%.2f,%.2f,y%.1f",
                player.getX(), player.getY(), player.getZ(), player.getYRot());
    }

    private static String escape(String value) {
        StringBuilder builder = new StringBuilder();
        value.codePoints().forEach(codePoint -> {
            if (codePoint == '\n') {
                builder.append("\\n");
            } else if (codePoint < 0x20 || codePoint >= 0x2000) {
                builder.append(codePoint > 0xFFFF
                        ? String.format("\\U%06X", codePoint)
                        : String.format("\\u%04X", codePoint));
            } else {
                builder.appendCodePoint(codePoint);
            }
        });
        return builder.toString();
    }

    private static void line(String text) {
        if (writer == null) {
            return;
        }
        try {
            writer.write(text);
            writer.newLine();
        } catch (IOException ignored) {
        }
    }

    private static void flush() {
        if (writer == null) {
            return;
        }
        try {
            writer.flush();
        } catch (IOException ignored) {
        }
    }

    public record StartResult(int glyphCount, Path path) {
    }

    public record StopResult(int eventCount, int mapDumpCount, Path directory) {
    }
}
