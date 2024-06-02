package net.swofty.types.generic.entity.hologram;

import lombok.Builder;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ServerHolograms {
    TO_ISLAND(new Pos(-2.5, 71, -62.5), "§bTravel to:", "§aYour Island"),
    ;

    private static final Map<ExternalHologram, List<HologramEntity>> externalHolograms = new HashMap<>();

    private final Pos pos;
    private final String[] text;

    ServerHolograms(Pos pos, String... text) {
        this.pos = pos;
        this.text = text;
    }

    public static void spawnAll(Instance instance) {
        for (ServerHolograms hologram : values()) {
            // Calculate the starting Y position based on the text length
            double startY = hologram.text.length * 0.3 - 0.3;
            for (int i = 0; i < hologram.text.length; i++) {
                HologramEntity entity = new HologramEntity(hologram.text[i]);
                entity.setAutoViewable(true);
                entity.setInstance(instance, hologram.pos.add(0, startY - (i * 0.3), 0));
            }
        }
    }

    public static void removeExternalHologram(ExternalHologram hologram) {
        List<HologramEntity> entities = externalHolograms.get(hologram);
        if (entities == null) return;

        for (HologramEntity entity : entities) {
            entity.remove();
        }

        externalHolograms.remove(hologram);
    }

    public static void addExternalHologram(ExternalHologram hologram) {
        List<HologramEntity> entities = new ArrayList<>();
        double startY = hologram.text.length * 0.3 - 0.3;
        for (int i = 0; i < hologram.text.length; i++) {
            HologramEntity entity = new HologramEntity(hologram.text[i]);
            entity.setInstance(hologram.instance, hologram.pos.add(0, startY - (i * 0.3), 0));
            entity.setAutoViewable(true);
            entities.add(entity);
        }

        externalHolograms.put(hologram, entities);
    }

    @Builder
    @Getter
    public static class ExternalHologram {
        private final Instance instance;
        private final Pos pos;
        private final String[] text;
    }
}
