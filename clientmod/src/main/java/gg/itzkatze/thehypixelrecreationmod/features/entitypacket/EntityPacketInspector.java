package gg.itzkatze.thehypixelrecreationmod.features.entitypacket;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class EntityPacketInspector {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final int MAX_ENTRIES = 500;
    private static final double ASSOCIATION_RADIUS = 1.5;
    private static final ArrayDeque<PacketEntry> entries = new ArrayDeque<>();
    private static final Set<Integer> entityIds = new LinkedHashSet<>();
    private static final Set<UUID> entityUuids = new LinkedHashSet<>();
    private static Entity selectedEntity;
    private static boolean recording = true;

    private EntityPacketInspector() {
    }

    public static boolean selectLookedAtEntity(Minecraft client) {
        if (!(client.hitResult instanceof EntityHitResult hitResult)) {
            return false;
        }

        selectedEntity = hitResult.getEntity();
        entries.clear();
        refreshAssociatedEntities(client);
        return true;
    }

    public static void clearSelection() {
        selectedEntity = null;
        entityIds.clear();
        entityUuids.clear();
        entries.clear();
    }

    public static void tick(Minecraft client) {
        if (selectedEntity == null || selectedEntity.isRemoved() || client.level == null) {
            if (selectedEntity != null) {
                clearSelection();
            }
            return;
        }
        refreshAssociatedEntities(client);
    }

    public static void inspect(Packet<?> packet) {
        Minecraft client = Minecraft.getInstance();
        if (!recording || selectedEntity == null) {
            return;
        }

        client.execute(() -> {
            if (selectedEntity == null || !referencesSelection(packet)) {
                return;
            }

            entries.addFirst(new PacketEntry(
                    LocalTime.now().format(TIME_FORMAT),
                    packet.getClass().getSimpleName(),
                    packet.toString()
            ));
            while (entries.size() > MAX_ENTRIES) {
                entries.removeLast();
            }
        });
    }

    public static Entity selectedEntity() {
        return selectedEntity;
    }

    public static Set<Integer> entityIds() {
        return Collections.unmodifiableSet(entityIds);
    }

    public static List<PacketEntry> entries() {
        return List.copyOf(entries);
    }

    public static boolean recording() {
        return recording;
    }

    public static void toggleRecording() {
        recording = !recording;
    }

    public static void clearEntries() {
        entries.clear();
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
        entityIds.add(entity.getId());
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
        if (value == null || depth > 4 || isLeaf(value.getClass())) {
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

    public record PacketEntry(String time, String type, String details) {
    }
}
