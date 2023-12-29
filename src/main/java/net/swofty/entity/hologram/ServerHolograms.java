package net.swofty.entity.hologram;

import lombok.Builder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.Arrays;

public enum ServerHolograms {
    TO_ISLAND(new Pos(5.5, 33.8, -104.5), "§bTravel to:", "§aYour Island"),
    ;

    private static boolean spawned = false;
    private static ArrayList<ExternalHologram> externalHolograms = new ArrayList<>();

    private Pos pos;
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
                entity.setInstance(instance, hologram.pos.add(0, startY - (i * 0.3), 0));
                entity.setAutoViewable(true);
                entity.spawn();
            }
        }

        for (ExternalHologram hologram : externalHolograms) {
            // Calculate the starting Y position based on the text length
            double startY = hologram.text.length * 0.3 - 0.3;
            for (int i = 0; i < hologram.text.length; i++) {
                HologramEntity entity = new HologramEntity(hologram.text[i]);
                entity.setInstance(hologram.instance, hologram.pos.add(0, startY - (i * 0.3), 0));
                entity.setAutoViewable(true);
                entity.spawn();
            }
        }
        spawned = true;
    }

    public static void addExternalHologram(ExternalHologram hologram) {
        externalHolograms.add(hologram);

        if (spawned) {
            // Calculate the starting Y position based on the text length
            double startY = hologram.text.length * 0.3 - 0.3;
            for (int i = 0; i < hologram.text.length; i++) {
                HologramEntity entity = new HologramEntity(hologram.text[i]);
                entity.setInstance(hologram.instance, hologram.pos.add(0, startY - (i * 0.3), 0));
                entity.setAutoViewable(true);
                entity.spawn();
            }
        }
    }

    @Builder
    public static class ExternalHologram {
        private final Instance instance;
        private final Pos pos;
        private final String[] text;
    }
}
