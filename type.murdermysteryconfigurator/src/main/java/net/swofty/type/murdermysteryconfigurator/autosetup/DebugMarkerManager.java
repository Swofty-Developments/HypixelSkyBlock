package net.swofty.type.murdermysteryconfigurator.autosetup;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.murdermystery.map.MurderMysteryMapsConfig.Position;

import java.util.*;

public class DebugMarkerManager {

    private static final Map<UUID, List<Entity>> playerMarkers = new HashMap<>();

    public static void showMarkers(UUID playerUuid, MurderMysterySetupSession session, Instance instance) {
        hideMarkers(playerUuid);

        List<Entity> markers = new ArrayList<>();

        // Gold spawns
        int goldIndex = 1;
        for (Position pos : session.getGoldSpawns()) {
            markers.add(createMarker(instance, pos, "§6Gold Spawn #" + goldIndex++, Material.GOLD_INGOT));
        }

        // Player spawns
        int spawnIndex = 1;
        for (Position pos : session.getPlayerSpawns()) {
            markers.add(createMarker(instance, pos, "§aPlayer Spawn #" + spawnIndex++, Material.PLAYER_HEAD));
        }

        // Waiting location
        if (session.getWaitingLocation() != null) {
            Position pos = new Position(session.getWaitingLocation().x(), session.getWaitingLocation().y(), session.getWaitingLocation().z());
            markers.add(createMarker(instance, pos, "§eWaiting Spawn", Material.CLOCK));
        }

        // Kill zones (show min/max corners for each)
        for (var entry : session.getKillRegions().entrySet()) {
            var region = entry.getValue();
            String name = entry.getKey();
            if (region.getMinPos() != null) {
                markers.add(createMarker(instance, region.getMinPos(), "§c" + name + " Min", Material.BARRIER));
            }
            if (region.getMaxPos() != null) {
                markers.add(createMarker(instance, region.getMaxPos(), "§c" + name + " Max", Material.BARRIER));
            }
        }

        playerMarkers.put(playerUuid, markers);
    }

    public static void hideMarkers(UUID playerUuid) {
        List<Entity> markers = playerMarkers.remove(playerUuid);
        if (markers != null) {
            for (Entity marker : markers) {
                marker.remove();
            }
        }
    }

    private static Entity createMarker(Instance instance, Position pos, String label, Material headItem) {
        Entity armorStand = new Entity(EntityType.ARMOR_STAND);

        ArmorStandMeta meta = (ArmorStandMeta) armorStand.getEntityMeta();
        meta.setMarker(true);
        meta.setInvisible(true);
        meta.setHasNoGravity(true);
        meta.setSmall(true);
        meta.setCustomNameVisible(true);

        armorStand.set(DataComponents.CUSTOM_NAME, Component.text(label));

        armorStand.setInstance(instance, new Pos(pos.x(), pos.y() + 1.5, pos.z()));

        // floating animation
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (armorStand.isRemoved()) return TaskSchedule.stop();

            double time = System.currentTimeMillis() / 500.0;
            double yOffset = Math.sin(time) * 0.1;
            Pos currentPos = armorStand.getPosition();
            armorStand.teleport(currentPos.withY(pos.y() + 1.5 + yOffset));

            return TaskSchedule.tick(2);
        }, TaskSchedule.immediate());

        return armorStand;
    }

    public static Entity createSingleMarker(Instance instance, double x, double y, double z, String label) {
        return createMarker(instance, new Position(x, y, z), label, Material.ARMOR_STAND);
    }

    public static void refreshMarkers(UUID playerUuid, MurderMysterySetupSession session, Instance instance) {
        if (playerMarkers.containsKey(playerUuid)) {
            showMarkers(playerUuid, session, instance);
        }
    }

    public static boolean areMarkersShown(UUID playerUuid) {
        return playerMarkers.containsKey(playerUuid) && !playerMarkers.get(playerUuid).isEmpty();
    }
}
