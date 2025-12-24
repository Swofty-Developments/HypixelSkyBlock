package net.swofty.type.bedwarsconfigurator.autosetup;

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
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.Position;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;

import java.util.*;

public class DebugMarkerManager {

    private static final Map<UUID, List<Entity>> playerMarkers = new HashMap<>();

    public static void showMarkers(UUID playerUuid, AutoSetupSession session, Instance instance) {
        hideMarkers(playerUuid);

        List<Entity> markers = new ArrayList<>();

        for (Map.Entry<TeamKey, AutoSetupSession.TeamConfig> entry : session.getTeams().entrySet()) {
            TeamKey team = entry.getKey();
            AutoSetupSession.TeamConfig config = entry.getValue();
            String teamColor = team.chatColor();

            // Bed markers
            if (config.getBedFeet() != null) {
                markers.add(createMarker(instance, config.getBedFeet(), teamColor + team.getName() + " Bed (Feet)", Material.RED_BED));
            }
            if (config.getBedHead() != null) {
                markers.add(createMarker(instance, config.getBedHead(), teamColor + team.getName() + " Bed (Head)", Material.RED_BED));
            }

            // Spawn marker
            if (config.getSpawn() != null) {
                Position pos = new Position(config.getSpawn().x(), config.getSpawn().y(), config.getSpawn().z());
                markers.add(createMarker(instance, pos, teamColor + team.getName() + " Spawn", Material.PLAYER_HEAD));
            }

            // Generator marker
            if (config.getGenerator() != null) {
                markers.add(createMarker(instance, config.getGenerator(), teamColor + team.getName() + " Generator", Material.IRON_INGOT));
            }

            // Shop markers
            if (config.getItemShop() != null) {
                Position pos = new Position(config.getItemShop().x(), config.getItemShop().y(), config.getItemShop().z());
                markers.add(createMarker(instance, pos, teamColor + team.getName() + " Item Shop", Material.EMERALD));
            }
            if (config.getTeamShop() != null) {
                Position pos = new Position(config.getTeamShop().x(), config.getTeamShop().y(), config.getTeamShop().z());
                markers.add(createMarker(instance, pos, teamColor + team.getName() + " Team Shop", Material.NETHER_STAR));
            }
        }

        // Diamond generators
        int diamondIndex = 1;
        for (Position pos : session.getDiamondGenerators()) {
            markers.add(createMarker(instance, pos, "§bDiamond Gen #" + diamondIndex++, Material.DIAMOND_BLOCK));
        }

        // Emerald generators
        int emeraldIndex = 1;
        for (Position pos : session.getEmeraldGenerators()) {
            markers.add(createMarker(instance, pos, "§aEmerald Gen #" + emeraldIndex++, Material.EMERALD_BLOCK));
        }

        // Waiting location
        if (session.getWaitingLocation() != null) {
            Position pos = new Position(session.getWaitingLocation().x(), session.getWaitingLocation().y(), session.getWaitingLocation().z());
            markers.add(createMarker(instance, pos, "§eWaiting Spawn", Material.CLOCK));
        }

        // Spectator location
        if (session.getSpectatorLocation() != null) {
            Position pos = new Position(session.getSpectatorLocation().x(), session.getSpectatorLocation().y(), session.getSpectatorLocation().z());
            markers.add(createMarker(instance, pos, "§7Spectator Spawn", Material.ENDER_EYE));
        }

        // Bounds markers (corners)
        if (session.hasBounds()) {
            markers.add(createMarker(instance, new Position(session.getMinX(), session.getMinY(), session.getMinZ()), "§8Bounds Min", Material.BARRIER));
            markers.add(createMarker(instance, new Position(session.getMaxX(), session.getMaxY(), session.getMaxZ()), "§8Bounds Max", Material.BARRIER));
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

    public static void refreshMarkers(UUID playerUuid, AutoSetupSession session, Instance instance) {
        if (playerMarkers.containsKey(playerUuid)) {
            showMarkers(playerUuid, session, instance);
        }
    }

    public static boolean areMarkersShown(UUID playerUuid) {
        return playerMarkers.containsKey(playerUuid) && !playerMarkers.get(playerUuid).isEmpty();
    }
}

