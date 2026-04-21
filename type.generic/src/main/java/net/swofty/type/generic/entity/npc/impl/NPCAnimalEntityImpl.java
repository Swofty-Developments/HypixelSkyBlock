package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.configuration.AnimalConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class NPCAnimalEntityImpl extends LivingEntity implements NPCViewable {
    private final List<HypixelPlayer> inRangeOf = Collections.synchronizedList(new ArrayList<>());
    private final HypixelPlayer viewer;
    private final PlayerHolograms.ExternalPlayerHologram holo;
    private final AnimalConfiguration config;
    private String[] holograms;
    private Entity seatMount;

    public NPCAnimalEntityImpl(@NotNull HypixelPlayer viewer, @NotNull Pos pos, @NotNull String bottomDisplay, @NotNull EntityType entityType, @NotNull AnimalConfiguration config, String[] holograms) {
        super(entityType);

        this.viewer = viewer;
        this.config = config;

        this.setCustomNameVisible(false);
        setNoGravity(true);
        setAutoViewable(false);

        PlayerHolograms.ExternalPlayerHologram holo = PlayerHolograms.ExternalPlayerHologram.builder()
            .pos(pos.add(0, getBoundingBox().height() - 0.1f, 0))
            .text(holograms)
            .player(viewer)
            .instance(config.instance())
            .build();

        this.holo = holo;

        PlayerHolograms.addExternalPlayerHologram(holo);
        setInstance(config.instance(), pos);
        addViewer(viewer);
        setPose(config.pose(viewer));
    }

    @Override
    public void remove() {
        super.remove();
        PlayerHolograms.removeExternalPlayerHologram(holo);
    }

    @Override
    public void updateNPC() {
        Pos npcPosition = config.position(viewer);
        if (!getPosition().asVec().equals(npcPosition.asVec())) {
            PlayerHolograms.relocateExternalPlayerHologram(holo, npcPosition.add(0, getBoundingBox().height() - 0.1f, 0));
        }

        if (!getPose().equals(config.pose(viewer))) {
            setPose(config.pose(viewer));
        }

        String[] newHolograms = config.holograms(viewer);
        if (!Arrays.equals(newHolograms, holograms)) {
            PlayerHolograms.updateExternalPlayerHologramText(holo, newHolograms);
            this.holograms = newHolograms;
        }
    }
}
