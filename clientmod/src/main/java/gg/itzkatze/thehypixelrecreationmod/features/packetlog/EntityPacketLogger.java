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

public final class EntityPacketLogger {
    private static final Path LOG_DIR = FabricLoader.getInstance().getGameDir().resolve("packet-logs");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final double ASSOCIATION_RADIUS = 1.5;
    private static final int MAX_REFLECTION_DEPTH = 4;

    private static final Set<Integer> entityIds = new LinkedHashSet<>();
    private static final Set<UUID> entityUuids = new LinkedHashSet<>();

    private static Entity selectedEntity;
    private static BufferedWriter writer;
    private static BufferedWriter trackWriter;
    private static Path trackPath;
    private static int trackTick;
    private static Path logPath;
    private static int packetCount;

    private EntityPacketLogger() {
    }

    public static boolean isActive() {
        return selectedEntity != null;
    }

    public static boolean isTracking(Entity entity) {
        return entity != null && entityIds.contains(entity.getId());
    }

    public static Entity selectedEntity() {
        return selectedEntity;
    }

    public static StartResult start(Entity entity) throws IOException {
        if (isActive()) {
            throw new IllegalStateException("A packet log session is already active.");
        }

        selectedEntity = entity;
        packetCount = 0;
        refreshAssociatedEntities(Minecraft.getInstance());

        Files.createDirectories(LOG_DIR);
        String entityName = describe(entity);
        logPath = LOG_DIR.resolve(sanitize(entityName) + "_" + LocalDateTime.now().format(FILE_FORMAT) + ".log");
        writer = Files.newBufferedWriter(
                logPath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );

        trackPath = LOG_DIR.resolve(sanitize(entityName) + "_" + LocalDateTime.now().format(FILE_FORMAT) + ".track.jsonl");
        trackWriter = Files.newBufferedWriter(trackPath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        trackTick = 0;

        writeLine("# entity: " + entityName);
        writeLine("# id: " + entity.getId() + "  uuid: " + entity.getUUID());
        writeLine("# type: " + entity.getType().toShortString());
        writeLine("# tracked entity ids: " + entityIds);
        writeLine("# started: " + LocalDateTime.now());
        writeLine("");
        writeRigSnapshot(Minecraft.getInstance());
        writeLine("");
        flush();

        return new StartResult(entityName, entity.getId(), entityIds.size(), logPath);
    }

    public static StopResult stop() {
        if (!isActive()) {
            throw new IllegalStateException("No packet log session is active.");
        }

        String entityName = describe(selectedEntity);
        int entityId = selectedEntity.getId();
        Path path = logPath;
        int count = packetCount;

        writeLine("");
        writeLine("# stopped: " + LocalDateTime.now() + " after " + count + " packets");
        closeWriter();
        if (trackWriter != null) {
            try {
                trackWriter.flush();
                trackWriter.close();
            } catch (IOException ignored) {
            }
            trackWriter = null;
        }

        selectedEntity = null;
        logPath = null;
        packetCount = 0;
        entityIds.clear();
        entityUuids.clear();

        return new StopResult(entityName, entityId, count, path);
    }

    public static void tick() {
        if (selectedEntity == null) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        if (client.level == null || selectedEntity.isRemoved()) {
            StopResult result = stop();
            ChatUtils.warn("Tracked entity vanished, stopped packet log after "
                    + result.packetCount() + " packets: " + result.path().getFileName());
            return;
        }

        refreshAssociatedEntities(client);
        sampleTrack(client);
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

            if (entity instanceof Display display) {
                for (SynchedEntityData.DataValue<?> value : display.getEntityData().getNonDefaultValues()) {
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

    private static String fmt(double value) {
        return String.format("%.5f", value);
    }

    public static void record(Packet<?> packet) {
        if (selectedEntity == null) {
            return;
        }

        String timestamp = LocalTime.now().format(TIME_FORMAT);
        Minecraft.getInstance().execute(() -> {
            if (selectedEntity == null || writer == null || !referencesSelection(packet)) {
                return;
            }

            packetCount++;
            writeLine("[" + timestamp + "] " + formatPacket(packet));
            flush();
        });
    }

    private static void writeRigSnapshot(Minecraft client) {
        if (client.level == null) {
            return;
        }

        writeLine("# per-tick rig track: " + trackPath.getFileName());
        writeLine("# --- rig snapshot at session start ---");
        for (int id : entityIds) {
            Entity entity = client.level.getEntity(id);
            if (entity == null) {
                writeLine("# rig " + id + " <not resolvable>");
                continue;
            }

            writeLine("# rig " + id
                    + " type=" + entity.getType().toShortString()
                    + " class=" + entity.getClass().getSimpleName()
                    + " uuid=" + entity.getUUID()
                    + " pos=" + formatVector(entity.getX(), entity.getY(), entity.getZ()));
            for (SynchedEntityData.DataValue<?> value : entity.getEntityData().getNonDefaultValues()) {
                writeLine("#   data " + value.id() + " = " + formatValue(value.value()));
            }
        }
        writeLine("# --- end rig snapshot ---");
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
            case Optional<?> optional -> optional.map(EntityPacketLogger::formatValue).orElse("empty");
            case Vector3fc vector -> formatVector(vector.x(), vector.y(), vector.z());
            case Quaternionfc quaternion -> "quat(" + quaternion.x() + "," + quaternion.y()
                    + "," + quaternion.z() + "," + quaternion.w() + ")";
            case ItemStack stack -> "item(" + BuiltInRegistries.ITEM.getKey(stack.getItem())
                    + " x" + stack.getCount()
                    + " model=" + stack.get(DataComponents.ITEM_MODEL)
                    + " color=" + stack.get(DataComponents.DYED_COLOR) + ")";
            default -> String.valueOf(value);
        };
    }

    private static String formatVector(double x, double y, double z) {
        return "vec3(" + x + "," + y + "," + z + ")";
    }

    private static void writeLine(String line) {
        if (writer == null) {
            return;
        }

        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException exception) {
            ChatUtils.error("Failed writing to packet log: " + exception.getMessage());
            closeWriter();
        }
    }

    private static void flush() {
        if (writer == null) {
            return;
        }

        try {
            writer.flush();
        } catch (IOException exception) {
            ChatUtils.error("Failed flushing packet log: " + exception.getMessage());
            closeWriter();
        }
    }

    private static void closeWriter() {
        if (writer == null) {
            return;
        }

        try {
            writer.close();
        } catch (IOException ignored) {
        }
        writer = null;
    }

    private static String describe(Entity entity) {
        String name = entity.getName().getString();
        if (name.isBlank()) {
            return entity.getType().toShortString();
        }
        return name;
    }

    private static String sanitize(String name) {
        String cleaned = name.replaceAll("§.", "").replaceAll("[^a-zA-Z0-9._-]", "_");
        if (cleaned.isBlank()) {
            return "entity";
        }
        return cleaned.length() > 40 ? cleaned.substring(0, 40) : cleaned;
    }

    private static void refreshAssociatedEntities(Minecraft client) {
        if (client.level == null || selectedEntity == null) {
            return;
        }

        entityIds.clear();
        entityUuids.clear();
        addEntityTree(selectedEntity);

        var bounds = selectedEntity.getBoundingBox().inflate(ASSOCIATION_RADIUS);
        for (Entity entity : client.level.getEntities(selectedEntity, bounds)) {
            if (entity.distanceToSqr(selectedEntity) <= ASSOCIATION_RADIUS * ASSOCIATION_RADIUS) {
                addEntityTree(entity);
            }
        }
    }

    private static void addEntityTree(Entity entity) {
        if (!entityIds.add(entity.getId())) {
            return;
        }

        entityUuids.add(entity.getUUID());
        for (Entity passenger : entity.getPassengers()) {
            addEntityTree(passenger);
        }
        if (entity.getVehicle() != null) {
            addEntityTree(entity.getVehicle());
        }
    }

    private static boolean referencesSelection(Object value) {
        return referencesSelection(value, Collections.newSetFromMap(new IdentityHashMap<>()), 0);
    }

    private static boolean referencesSelection(Object value, Set<Object> visited, int depth) {
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
                if (referencesSelection(Array.get(value, i), visited, depth + 1)) {
                    return true;
                }
            }
            return false;
        }
        if (value instanceof Iterable<?> iterable) {
            for (Object element : iterable) {
                if (referencesSelection(element, visited, depth + 1)) {
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
                if (referencesSelection(field.get(value), visited, depth + 1)) {
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

    public record StartResult(String entityName, int entityId, int trackedEntityCount, Path path) {
    }

    public record StopResult(String entityName, int entityId, int packetCount, Path path) {
    }
}
