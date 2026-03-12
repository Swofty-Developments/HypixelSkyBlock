package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
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

    public NPCAnimalEntityImpl(@NotNull HypixelPlayer viewer, @NotNull Pos pos, @NotNull String bottomDisplay, @NotNull EntityType entityType, @NotNull AnimalConfiguration config, String[] holograms, boolean overflowing) {
        super(entityType);

        this.viewer = viewer;
        this.config = config;
        this.setCustomNameVisible(true);
        this.set(DataComponents.CUSTOM_NAME, Component.text(bottomDisplay));
        setNoGravity(true);
        setAutoViewable(false);

        PlayerHolograms.ExternalPlayerHologram holo = PlayerHolograms.ExternalPlayerHologram.builder()
            .pos(pos.add(0, getEyeHeight() + config.hologramYOffset() + (overflowing ? -0.2f : 0f), 0))
            .text(Arrays.copyOfRange(holograms, 0, holograms.length - (overflowing ? 0 : 1)))
            .player(viewer)
            .instance(config.instance(viewer))
            .build();

        this.holo = holo;

        PlayerHolograms.addExternalPlayerHologram(holo);
        setInstance(config.instance(viewer), pos);
        addViewer(viewer);
        setPose(config.pose(viewer));
    }

    @Override
    public void remove() {
        super.remove();
        PlayerHolograms.removeExternalPlayerHologram(holo);
    }

    /**
     * Clears the cache for a player, is only run on quit, {@see QuitAction.java}
     * @param player The player to clear the cache for
     */
    public void clearCache(HypixelPlayer player) {
        inRangeOf.remove(player);
    }

    @Override
    public void updateNPC() {
        Pos npcPosition = config.position(viewer);
        if (!getPosition().asVec().equals(npcPosition.asVec())) {
            String[] holograms = config.holograms(viewer);

            boolean overflowing = holograms[holograms.length - 1].length() > 16;
            float yOffset = overflowing ? -0.2f : 0.0f;
            yOffset += config.hologramYOffset();
            PlayerHolograms.relocateExternalPlayerHologram(holo, npcPosition.add(0, getEyeHeight() + yOffset, 0));
        }

        if (!getPose().equals(config.pose(viewer))) {
            setPose(config.pose(viewer));
        }

        String[] newHolograms = config.holograms(viewer);
        boolean isOverflowing = newHolograms[newHolograms.length - 1].length() > 16;
        String[] finalHolograms = Arrays.copyOfRange(newHolograms, 0, newHolograms.length - (isOverflowing ? 0 : 1));
        if (!Arrays.equals(finalHolograms, holograms)) {
            PlayerHolograms.updateExternalPlayerHologramText(holo, finalHolograms);
            this.holograms = finalHolograms;
        }
    }
}
