package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.VillagerProfession;
import net.minestom.server.entity.VillagerType;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class NPCVillagerEntityImpl extends EntityCreature implements NPCViewable {
    private final List<HypixelPlayer> inRangeOf = Collections.synchronizedList(new ArrayList<>());
    private final HypixelPlayer viewer;
    private final PlayerHolograms.ExternalPlayerHologram holo;
    private final VillagerConfiguration config;
    private String[] holograms;

    public NPCVillagerEntityImpl(@NotNull HypixelPlayer viewer, Pos pos, @NotNull String bottomDisplay, VillagerProfession profession, VillagerConfiguration config, String[] holograms, boolean overflowing) {
        super(EntityType.VILLAGER);
        this.viewer = viewer;
        this.config = config;
        this.holograms = holograms;

        this.setCustomNameVisible(true);
        this.set(DataComponents.CUSTOM_NAME, Component.text(bottomDisplay));

        VillagerMeta meta = (VillagerMeta) this.entityMeta;
        meta.setVillagerData(new VillagerMeta.VillagerData(
                VillagerType.PLAINS, profession, VillagerMeta.Level.EXPERT)
        );

        setAutoViewable(false);
        setNoGravity(true);

        PlayerHolograms.ExternalPlayerHologram holo = PlayerHolograms.ExternalPlayerHologram.builder()
            .pos(pos.add(0, getEyeHeight() + 0.5f + (overflowing ? -0.2f : 0f), 0))
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
     * Clears the cache for a player, is only run on quit
     * @param player The player to clear the cache for
     */
    public void clearCache(HypixelPlayer player) {
        inRangeOf.remove(player);
    }

    @Override
    public void tick(long time) {
        Instance instance = getInstance();
        Pos position = getPosition();

        if (instance == null) {
            return;
        }

        if (!instance.isChunkLoaded(position)) {
            instance.loadChunk(position).join();
        }

        try {
            super.tick(time);
        } catch (Exception e) {
            // Suppress odd warnings
        }
    }

    @Override
    public void updateNPC() {
        Pos npcPosition = config.position(viewer);
        if (!getPosition().samePoint(npcPosition)) {
            Pos nextPosition = stepTowards(getPosition(), npcPosition, 0.85D);
            teleport(nextPosition);
            String[] holograms = config.holograms(viewer);

            boolean overflowing = holograms[holograms.length - 1].length() > 16;
            float yOffset = overflowing ? -0.2f : 0.0f;
            PlayerHolograms.relocateExternalPlayerHologram(holo, nextPosition.add(0, getEyeHeight() + 0.5f + yOffset, 0));
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

    private static Pos stepTowards(Pos current, Pos target, double maxDistance) {
        double dx = target.x() - current.x();
        double dy = target.y() - current.y();
        double dz = target.z() - current.z();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance <= maxDistance || distance == 0D) {
            return target;
        }

        double multiplier = maxDistance / distance;
        return new Pos(
            current.x() + dx * multiplier,
            current.y() + dy * multiplier,
            current.z() + dz * multiplier,
            target.yaw(),
            target.pitch()
        );
    }
}
