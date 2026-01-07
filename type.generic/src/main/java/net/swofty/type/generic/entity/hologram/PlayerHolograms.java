package net.swofty.type.generic.entity.hologram;

import lombok.Builder;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public enum PlayerHolograms {
    ;

    private static final HashMap<HypixelPlayer, List<Map.Entry<PlayerHolograms, HologramEntity>>> entities = new HashMap<>();
    public static final ConcurrentHashMap<ExternalPlayerHologram, List<HologramEntity>> externalPlayerHolograms = new ConcurrentHashMap<>();

    private final Pos pos;
    private final Function<HypixelPlayer, String[]> displayFunction;

    PlayerHolograms(Pos pos, Function<HypixelPlayer, String[]> displayFunction) {
        this.pos = pos;
        this.displayFunction = displayFunction;
    }

    private static void addHologram(HypixelPlayer skyBlockPlayer, PlayerHolograms hologramType, String line, double heightOffset) {
        HologramEntity entity = new HologramEntity(line);
        entity.setInstance(HypixelConst.getInstanceContainer(), hologramType.pos.add(0, -heightOffset, 0));
        entity.addViewer(skyBlockPlayer);

        entities.computeIfAbsent(skyBlockPlayer, k -> new ArrayList<>())
                .add(new AbstractMap.SimpleEntry<>(hologramType, entity));
    }

    public static void spawnAll(HypixelPlayer skyBlockPlayer) {
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
            List<HypixelPlayer> toRemove = new ArrayList<>();
            for (HypixelPlayer skyBlockPlayer : entities.keySet()) {
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
                            existingEntity.setInstance(existingEntity.getInstance(), hologram.pos.add(0, startY - (i * 0.3), 0));
                        } else {
                            // Add new hologram
                            HologramEntity entity = new HologramEntity(lines[i]);
                            // Set hologram entity at the correct position considering new spacing and bottom alignment
                            entity.setInstance(HypixelConst.getInstanceContainer(), hologram.pos.add(0, startY - (i * 0.3), 0));
                            entity.addViewer(skyBlockPlayer);
                            currentEntities.add(Map.entry(hologram, entity));
                        }
                    }

                    // Remove excess holograms if there are too many for this hologram type
                    while (perTypeCurrentEntities.size() > lines.length) {
                        HologramEntity entity = perTypeCurrentEntities.removeLast();
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
        double spacing = hologram.getSpacing();
        double startY = hologram.text.length * spacing - spacing;
        for (int i = 0; i < hologram.text.length; i++) {
            HologramEntity entity = new HologramEntity(hologram.text[i]);
            entity.setInstance(hologram.getInstance() != null ? hologram.getInstance() : HypixelConst.getInstanceContainer(), hologram.pos.add(0, startY - (i * spacing), 0));
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

    public static void relocateExternalPlayerHologram(ExternalPlayerHologram hologram, Pos newPosition) {
        List<HologramEntity> entities = externalPlayerHolograms.get(hologram);
        if (entities == null) return;

        double spacing = hologram.getSpacing();
        double startY = hologram.text.length * spacing - spacing;
        for (int i = 0; i < entities.size(); i++) {
            HologramEntity entity = entities.get(i);
            entity.setInstance(
                    hologram.getInstance() != null ? hologram.getInstance() : HypixelConst.getInstanceContainer(),
                    newPosition.add(0, startY - (i * spacing), 0)
            );
        }
    }

    public static void removeExternalPlayerHologramsAt(HypixelPlayer player, Pos position) {
        List<ExternalPlayerHologram> toRemove = new ArrayList<>();
        for (ExternalPlayerHologram hologram : externalPlayerHolograms.keySet()) {
            if (hologram.player.equals(player) && hologram.pos.sameBlock(position)) {
                toRemove.add(hologram);
            }
        }
        for (ExternalPlayerHologram hologram : toRemove) {
            removeExternalPlayerHologram(hologram);
        }
    }

    public static void updateExternalHolograms() {
        List<ExternalPlayerHologram> toRemove = new ArrayList<>();

        for (Map.Entry<ExternalPlayerHologram, List<HologramEntity>> entry : externalPlayerHolograms.entrySet()) {
            ExternalPlayerHologram hologram = entry.getKey();
            List<HologramEntity> hologramEntities = entry.getValue();

            if (!hologram.player.isOnline()) {
                toRemove.add(hologram);
                continue;
            }

            // If hologram has a display function, update it
            if (hologram.displayFunction != null) {
                String[] newLines = hologram.displayFunction.apply(hologram.player);
                if (newLines != null) {
                    double spacing = hologram.getSpacing();
                    double startY = newLines.length * spacing - spacing;

                    // Update existing lines
                    for (int i = 0; i < newLines.length && i < hologramEntities.size(); i++) {
                        hologramEntities.get(i).setText(newLines[i]);
                    }

                    // Add new lines if needed
                    while (hologramEntities.size() < newLines.length) {
                        int i = hologramEntities.size();
                        HologramEntity entity = new HologramEntity(newLines[i]);
                        entity.setInstance(
                                hologram.getInstance() != null ? hologram.getInstance() : HypixelConst.getInstanceContainer(),
                                hologram.pos.add(0, startY - (i * spacing), 0)
                        );
                        entity.addViewer(hologram.player);
                        hologramEntities.add(entity);
                    }

                    // Remove excess lines if needed
                    while (hologramEntities.size() > newLines.length) {
                        HologramEntity entity = hologramEntities.removeLast();
                        entity.removeViewer(hologram.player);
                        entity.remove();
                    }
                }
            }
        }

        for (ExternalPlayerHologram hologram : toRemove) {
            removeExternalPlayerHologram(hologram);
        }
    }

    public static void remove(HypixelPlayer skyBlockPlayer) {
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
        private final HypixelPlayer player;
        private final Pos pos;
        private final String[] text;
        private final Instance instance;
        private final Function<HypixelPlayer, String[]> displayFunction;
        @Builder.Default
        private final double spacing = 0.3;
    }
}
