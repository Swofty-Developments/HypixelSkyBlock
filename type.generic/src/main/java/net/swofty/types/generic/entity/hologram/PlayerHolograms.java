package net.swofty.types.generic.entity.hologram;

import lombok.Builder;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public enum PlayerHolograms {
    ;

    private static final HashMap<SkyBlockPlayer, List<Map.Entry<PlayerHolograms, HologramEntity>>> entities = new HashMap<>();
    public static final ConcurrentHashMap<ExternalPlayerHologram, List<HologramEntity>> externalPlayerHolograms = new ConcurrentHashMap<>();

    private final Pos pos;
    private final Function<SkyBlockPlayer, String[]> displayFunction;

    PlayerHolograms(Pos pos, Function<SkyBlockPlayer, String[]> displayFunction) {
        this.pos = pos;
        this.displayFunction = displayFunction;
    }

    private static void addHologram(SkyBlockPlayer skyBlockPlayer, PlayerHolograms hologramType, String line, double heightOffset) {
        HologramEntity entity = new HologramEntity(line);
        entity.setInstance(SkyBlockConst.getInstanceContainer(), hologramType.pos.add(0, -heightOffset, 0));
        entity.addViewer(skyBlockPlayer);

        entities.computeIfAbsent(skyBlockPlayer, k -> new ArrayList<>())
                .add(new AbstractMap.SimpleEntry<>(hologramType, entity));
    }

    public static void spawnAll(SkyBlockPlayer skyBlockPlayer) {
        for (PlayerHolograms hologram : values()) {
            String[] lines = hologram.displayFunction.apply(skyBlockPlayer);
            if (lines != null) {
                for (int i = 0; i < lines.length; i++) {
                    addHologram(skyBlockPlayer, hologram, lines[i], i * 0.25);
                }
            }
        }
    }

    public static void updateAll(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            List<SkyBlockPlayer> toRemove = new ArrayList<>();
            for (SkyBlockPlayer skyBlockPlayer : entities.keySet()) {
                if (!skyBlockPlayer.isOnline()) {
                    toRemove.add(skyBlockPlayer);
                    continue;
                }

                List<Map.Entry<PlayerHolograms, HologramEntity>> currentEntities = entities.get(skyBlockPlayer);
                Map<PlayerHolograms, List<HologramEntity>> perTypeEntities = currentEntities.stream().collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toList())));

                for (PlayerHolograms hologram : PlayerHolograms.values()) {
                    String[] lines = hologram.displayFunction.apply(skyBlockPlayer);
                    if (lines == null) {
                        // If null, we'll not display any hologram
                        continue;
                    }

                    // Calculate the starting Y position based on the text length
                    double startY = lines.length * 0.3 - 0.3;

                    // Update existing or add new holograms
                    List<HologramEntity> perTypeCurrentEntities = perTypeEntities.getOrDefault(hologram, new ArrayList<>());
                    for (int i = 0; i < lines.length; i++) {
                        if (i < perTypeCurrentEntities.size()) {
                            // Update existing hologram text
                            HologramEntity existingEntity = perTypeCurrentEntities.get(i);
                            existingEntity.setText(lines[i]);
                            // Update existing hologram position
                            existingEntity.setInstance(SkyBlockConst.getInstanceContainer(), hologram.pos.add(0, startY - (i * 0.3), 0));
                        } else {
                            // Add new hologram
                            HologramEntity entity = new HologramEntity(lines[i]);
                            // Set hologram entity at the correct position considering new spacing and bottom alignment
                            entity.setInstance(SkyBlockConst.getInstanceContainer(), hologram.pos.add(0, startY - (i * 0.3), 0));
                            entity.addViewer(skyBlockPlayer);
                            currentEntities.add(Map.entry(hologram, entity));
                        }
                    }

                    // Remove excess holograms if there are too many for this hologram type
                    while (perTypeCurrentEntities.size() > lines.length) {
                        HologramEntity entity = perTypeCurrentEntities.remove(perTypeCurrentEntities.size() - 1);
                        currentEntities.remove(Map.entry(hologram, entity));
                        entity.removeViewer(skyBlockPlayer);
                        entity.remove();
                    }
                }
            }
            toRemove.forEach(PlayerHolograms::remove);
            return TaskSchedule.tick(10);
        });
    }

    public static void addExternalPlayerHologram(ExternalPlayerHologram hologram) {
        List<HologramEntity> entities = new ArrayList<>();
        double startY = hologram.text.length * 0.3 - 0.3;
        for (int i = 0; i < hologram.text.length; i++) {
            HologramEntity entity = new HologramEntity(hologram.text[i]);
            entity.setInstance(SkyBlockConst.getInstanceContainer(), hologram.pos.add(0, startY - (i * 0.3), 0));
            entity.addViewer(hologram.player);
            entities.add(entity);
        }

        externalPlayerHolograms.put(hologram, entities);
    }

    public static void removeExternalPlayerHologram(ExternalPlayerHologram hologram) {
        List<HologramEntity> entities = externalPlayerHolograms.get(hologram);
        if (entities == null) return;

        for (HologramEntity entity : entities) {
            entity.removeViewer(hologram.player);
            entity.remove();
        }

        externalPlayerHolograms.remove(hologram);
    }

    public static void remove(SkyBlockPlayer skyBlockPlayer) {
        List<Map.Entry<PlayerHolograms, HologramEntity>> hologramEntries = entities.remove(skyBlockPlayer);
        if (hologramEntries != null) {
            for (Map.Entry<PlayerHolograms, HologramEntity> entry : hologramEntries) {
                entry.getValue().removeViewer(skyBlockPlayer);
                entry.getValue().remove();
            }
        }
    }

    @Builder
    @Getter
    public static class ExternalPlayerHologram {
        private final SkyBlockPlayer player;
        private final Pos pos;
        private final String[] text;
    }
}
