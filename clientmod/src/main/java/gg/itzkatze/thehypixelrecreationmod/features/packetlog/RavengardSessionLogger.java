package gg.itzkatze.thehypixelrecreationmod.features.packetlog;

import gg.itzkatze.thehypixelrecreationmod.utils.ChatUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
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
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class RavengardSessionLogger {
    private static final Path LOG_DIR = FabricLoader.getInstance().getGameDir().resolve("packet-logs");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String RIG_MODEL_PREFIX = "hypixel_ravengard:entity/";
    private static final double CLUSTER_RADIUS = 1.5;
    private static final int MAX_REFLECTION_DEPTH = 4;

    private static final Set<Integer> entityIds = new LinkedHashSet<>();
    private static final Set<UUID> entityUuids = new LinkedHashSet<>();

    private static boolean active;
    private static BufferedWriter writer;
    private static BufferedWriter trackWriter;
    private static Path logPath;
    private static Path trackPath;
    private static int trackTick;
    private static int packetCount;
    private static int rigCount;

    private RavengardSessionLogger() {
    }

    public static boolean isActive() {
        return active;
    }

    public static Path start() throws IOException {
        if (active) {
            throw new IllegalStateException("A ravengard session log is already active.");
        }

        Files.createDirectories(LOG_DIR);
        String stamp = LocalDateTime.now().format(FILE_FORMAT);
        logPath = LOG_DIR.resolve("ravengard_session_" + stamp + ".log");
        trackPath = LOG_DIR.resolve("ravengard_session_" + stamp + ".track.jsonl");
        writer = Files.newBufferedWriter(logPath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        trackWriter = Files.newBufferedWriter(trackPath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        trackTick = 0;
        packetCount = 0;
        rigCount = 0;
        entityIds.clear();
        entityUuids.clear();
        active = true;

        writeLine("# ravengard session log");
        writeLine("# started: " + LocalDateTime.now());
        writeLine("# per-tick rig track: " + trackPath.getFileName());
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            addEntity(client.player, "local player");
        }
        writeLine("");
        flush();
        return logPath;
    }

    public static StopResult stop() {
        if (!active) {
            throw new IllegalStateException("No ravengard session log is active.");
        }

        Path path = logPath;
        int count = packetCount;
        int rigs = rigCount;

        writeLine("");
        writeLine("# stopped: " + LocalDateTime.now() + " after " + count + " packets, " + rigs + " rigs");
        closeWriters();

        active = false;
        logPath = null;
        trackPath = null;
        entityIds.clear();
        entityUuids.clear();

        return new StopResult(count, rigs, path);
    }

    public static void tick() {
        if (!active) {
            return;
        }
        try {
            tickInternal();
        } catch (RuntimeException exception) {
            ChatUtils.error("Ravengard session logger tick failed: " + exception);
        }
    }

    private static void tickInternal() {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) {
            StopResult result = stop();
            ChatUtils.warn("Left the world, stopped ravengard session log after "
                    + result.packetCount() + " packets: " + result.path().getFileName());
            return;
        }

        if (client.player != null && !entityIds.contains(client.player.getId())) {
            addEntity(client.player, "local player");
        }

        discoverRigs(client);
        sampleTrack(client);
    }

    private static void discoverRigs(Minecraft client) {
        for (Entity entity : client.level.entitiesForRendering()) {
            if (entityIds.contains(entity.getId()) || !(entity instanceof Display)) {
                continue;
            }
            String model = rigModel(entity);
            if (model == null) {
                continue;
            }
            rigCount++;
            writeLine("# --- rig discovered: " + model + " at " + LocalTime.now().format(TIME_FORMAT) + " ---");
            addEntity(entity, model);
            AABB bounds = entity.getBoundingBox().inflate(CLUSTER_RADIUS);
            for (Entity nearby : client.level.getEntities(entity, bounds)) {
                if (entityIds.contains(nearby.getId())) {
                    continue;
                }
                if (nearby.distanceToSqr(entity) <= CLUSTER_RADIUS * CLUSTER_RADIUS) {
                    addEntity(nearby, "near " + model);
                }
            }
            flush();
        }
    }

    private static String rigModel(Entity entity) {
        List<SynchedEntityData.DataValue<?>> values = entity.getEntityData().getNonDefaultValues();
        if (values == null) {
            return null;
        }
        for (SynchedEntityData.DataValue<?> value : values) {
            if (value.value() instanceof ItemStack stack && !stack.isEmpty()) {
                var model = stack.get(DataComponents.ITEM_MODEL);
                if (model != null && model.toString().startsWith(RIG_MODEL_PREFIX)) {
                    return model.toString();
                }
            }
        }
        return null;
    }

    private static void addEntity(Entity entity, String reason) {
        if (!entityIds.add(entity.getId())) {
            return;
        }
        entityUuids.add(entity.getUUID());

        writeLine("# rig " + entity.getId()
                + " type=" + entity.getType().toShortString()
                + " class=" + entity.getClass().getSimpleName()
                + " uuid=" + entity.getUUID()
                + " pos=vec3(" + entity.getX() + "," + entity.getY() + "," + entity.getZ() + ")");
        writeLine("#   reason: " + reason);
        List<SynchedEntityData.DataValue<?>> values = entity.getEntityData().getNonDefaultValues();
        if (values != null) {
            for (SynchedEntityData.DataValue<?> value : values) {
                writeLine("#   data " + value.id() + " = " + formatValue(value.value()));
            }
        }

        for (Entity passenger : entity.getPassengers()) {
            addEntity(passenger, "passenger of " + entity.getId());
        }
        if (entity.getVehicle() != null) {
            addEntity(entity.getVehicle(), "vehicle of " + entity.getId());
        }
    }

    private static void sampleTrack(Minecraft client) {
        if (trackWriter == null || client.level == null) {
            return;
        }

        StringBuilder json = new StringBuilder();
        json.append("{\"tick\":").append(trackTick++);

        var self = client.player;
        if (self != null) {
            json.append(",\"player\":{\"x\":").append(fmt(self.getX()))
                    .append(",\"y\":").append(fmt(self.getY()))
                    .append(",\"z\":").append(fmt(self.getZ()))
                    .append(",\"yaw\":").append(fmt(self.getYRot())).append('}');
        }

        json.append(",\"entities\":[");
        boolean first = true;
        for (int id : entityIds) {
            Entity entity = client.level.getEntity(id);
            if (entity == null) {
                continue;
            }
            if (!first) {
                json.append(',');
            }
            first = false;
            json.append("{\"id\":").append(id)
                    .append(",\"type\":\"").append(entity.getType().toShortString()).append('"')
                    .append(",\"x\":").append(fmt(entity.getX()))
                    .append(",\"y\":").append(fmt(entity.getY()))
                    .append(",\"z\":").append(fmt(entity.getZ()))
                    .append(",\"yaw\":").append(fmt(entity.getYRot()));

            List<SynchedEntityData.DataValue<?>> displayValues =
                    entity instanceof Display display ? display.getEntityData().getNonDefaultValues() : null;
            if (displayValues != null) {
                for (SynchedEntityData.DataValue<?> value : displayValues) {
                    Object raw = value.value();
                    if (raw instanceof Vector3fc vector) {
                        json.append(",\"d").append(value.id()).append("\":[")
                                .append(fmt(vector.x())).append(',').append(fmt(vector.y()))
                                .append(',').append(fmt(vector.z())).append(']');
                    } else if (raw instanceof Quaternionfc quaternion) {
                        json.append(",\"d").append(value.id()).append("\":[")
                                .append(fmt(quaternion.x())).append(',').append(fmt(quaternion.y()))
                                .append(',').append(fmt(quaternion.z()))
                                .append(',').append(fmt(quaternion.w())).append(']');
                    } else if (raw instanceof Number number) {
                        json.append(",\"d").append(value.id()).append("\":").append(number);
                    }
                }
            }
            json.append('}');
        }
        json.append("]}");

        try {
            trackWriter.write(json.toString());
            trackWriter.newLine();
        } catch (IOException ignored) {
        }
    }

    public static void record(Packet<?> packet) {
        if (!active || !Minecraft.getInstance().isSameThread()) {
            return;
        }
        if (writer == null || !referencesTracked(packet)) {
            return;
        }

        packetCount++;
        writeLine("[" + LocalTime.now().format(TIME_FORMAT) + "] " + formatPacket(packet));
        flush();
    }

    private static String formatPacket(Packet<?> packet) {
        if (packet instanceof BundlePacket<?> bundle) {
            int count = 0;
            for (Packet<?> ignored : bundle.subPackets()) {
                count++;
            }
            return "~bundle count=" + count;
        }

        if (packet instanceof ClientboundSetEntityDataPacket data) {
            StringBuilder builder = new StringBuilder("SetEntityData id=").append(data.id());
            for (SynchedEntityData.DataValue<?> value : data.packedItems()) {
                builder.append(" | ").append(value.id()).append('=').append(formatValue(value.value()));
            }
            return builder.toString();
        }

        return packet.getClass().getSimpleName() + " | " + packet;
    }

    private static String formatValue(Object value) {
        return switch (value) {
            case null -> "null";
            case Optional<?> optional -> optional.map(RavengardSessionLogger::formatValue).orElse("empty");
            case Vector3fc vector -> "vec3(" + vector.x() + "," + vector.y() + "," + vector.z() + ")";
            case Quaternionfc quaternion -> "quat(" + quaternion.x() + "," + quaternion.y()
                    + "," + quaternion.z() + "," + quaternion.w() + ")";
            case ItemStack stack -> "item(" + BuiltInRegistries.ITEM.getKey(stack.getItem())
                    + " x" + stack.getCount()
                    + " model=" + stack.get(DataComponents.ITEM_MODEL)
                    + " color=" + stack.get(DataComponents.DYED_COLOR) + ")";
            default -> String.valueOf(value);
        };
    }

    private static String fmt(double value) {
        return String.format("%.5f", value);
    }

    private static void writeLine(String line) {
        if (writer == null) {
            return;
        }

        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException exception) {
            ChatUtils.error("Failed writing to ravengard session log: " + exception.getMessage());
            closeWriters();
        }
    }

    private static void flush() {
        if (writer == null) {
            return;
        }

        try {
            writer.flush();
        } catch (IOException exception) {
            ChatUtils.error("Failed flushing ravengard session log: " + exception.getMessage());
            closeWriters();
        }
    }

    private static void closeWriters() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {
            }
            writer = null;
        }
        if (trackWriter != null) {
            try {
                trackWriter.flush();
                trackWriter.close();
            } catch (IOException ignored) {
            }
            trackWriter = null;
        }
    }

    private static boolean referencesTracked(Object value) {
        return referencesTracked(value, Collections.newSetFromMap(new IdentityHashMap<>()), 0);
    }

    private static boolean referencesTracked(Object value, Set<Object> visited, int depth) {
        if (value == null || depth > MAX_REFLECTION_DEPTH || isLeaf(value.getClass())) {
            return value instanceof Integer integer && entityIds.contains(integer)
                    || value instanceof UUID uuid && entityUuids.contains(uuid);
        }
        if (!visited.add(value)) {
            return false;
        }

        Class<?> type = value.getClass();
        if (type.isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                if (referencesTracked(Array.get(value, i), visited, depth + 1)) {
                    return true;
                }
            }
            return false;
        }
        if (value instanceof Iterable<?> iterable) {
            for (Object element : iterable) {
                if (referencesTracked(element, visited, depth + 1)) {
                    return true;
                }
            }
            return false;
        }

        for (Field field : allFields(type)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                field.setAccessible(true);
                if (referencesTracked(field.get(value), visited, depth + 1)) {
                    return true;
                }
            } catch (ReflectiveOperationException | RuntimeException ignored) {
            }
        }
        return false;
    }

    private static List<Field> allFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> current = type; current != null && current != Object.class; current = current.getSuperclass()) {
            Collections.addAll(fields, current.getDeclaredFields());
        }
        return fields;
    }

    private static boolean isLeaf(Class<?> type) {
        return type.isPrimitive()
                || type.isEnum()
                || Number.class.isAssignableFrom(type)
                || CharSequence.class.isAssignableFrom(type)
                || type == Boolean.class
                || type == Character.class
                || type == UUID.class
                || type.getName().startsWith("java.time.");
    }

    public record StopResult(int packetCount, int rigCount, Path path) {
    }
}
