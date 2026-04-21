package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.MetadataDef;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.avatar.MannequinMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.player.ResolvableProfile;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
public class NPCEntityImpl extends Entity implements NPCViewable {
    private final HypixelPlayer viewer;
    private final PlayerHolograms.ExternalPlayerHologram holo;
    private final HumanConfiguration config;

    @Getter
    private List<HypixelPlayer> inRangeOf = Collections.synchronizedList(new ArrayList<>());
    private final String username;

    private final String skinTexture;
    private final String skinSignature;
    private String[] holograms;

    public NPCEntityImpl(@NotNull HypixelPlayer viewer, @NotNull Pos pos, @NotNull String bottomDisplay, @NotNull String skinTexture, @NotNull String skinSignature, @NotNull String[] holograms, HumanConfiguration config) {
        super(EntityType.MANNEQUIN, UUID.randomUUID());
        this.username = bottomDisplay;
        this.viewer = viewer;
        this.config = config;

        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;
        this.holograms = holograms;

        if (holograms == null) {
            throw new IllegalArgumentException("Holograms cannot be null");
        }

        setNoGravity(true);
        setAutoViewable(false);

        PlayerHolograms.ExternalPlayerHologram holo = PlayerHolograms.ExternalPlayerHologram.builder()
            .pos(pos.add(0, getBoundingBox().height() - 0.1f, 0))
            .text(holograms)
            .player(viewer)
            .instance(config.instance())
            .build();

        this.holo = holo;
        if (config.shouldDisplayHolograms(viewer)) {
            PlayerHolograms.addExternalPlayerHologram(holo);
        }

        setInstance(config.instance(), pos);
        addViewer(viewer);
        setCustomNameVisible(false);

        editEntityMeta(MannequinMeta.class, meta -> {
            meta.setImmovable(true); // doesn't matter we're in Minestom anyway
            meta.setProfile(
                new ResolvableProfile(
                    new PlayerSkin(skinTexture, skinSignature)
                )
            );
        });

        setPose(config.pose(viewer));
    }

    @Override
    public void remove() {
        super.remove();
        PlayerHolograms.removeExternalPlayerHologram(holo);
    }

    @Override
    public void updateNewViewer(@NotNull Player player) {
        super.updateNewViewer(player);

        if (player.getUuid() != viewer.getUuid()) {
            Logger.warn("Player {} is viewing NPC {} but is not the intended viewer", player.getUsername(), getUuid());
        }
    }

    @Override
    public void updateOldViewer(@NotNull Player player) {
        super.updateOldViewer(player);
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

        super.tick(time);
    }

    @Override
    public void updateNPC() {
        Pos npcPosition = config.position(viewer);
        if (!getPosition().asVec().equals(npcPosition.asVec()) && config.shouldDisplayHolograms(viewer)) {
            PlayerHolograms.relocateExternalPlayerHologram(holo, npcPosition.add(0, getBoundingBox().height() - 0.1f, 0));
        }

        if (!getPose().equals(config.pose(viewer))) {
            setPose(config.pose(viewer));
            viewer.sendPacket(new EntityMetaDataPacket(
                getEntityId(),
                Map.of(
                    MetadataDef.POSE.index(),
                    Metadata.Pose(config.pose(viewer))
                )
            ));
        }

        if (config.shouldDisplayHolograms(viewer)) {
            if (!PlayerHolograms.externalPlayerHolograms.containsKey(holo)) {
                PlayerHolograms.addExternalPlayerHologram(holo);
            }
            String[] newHolograms = config.holograms(viewer);
            if (!Arrays.equals(newHolograms, holograms)) {
                PlayerHolograms.updateExternalPlayerHologramText(holo, newHolograms);
                this.holograms = newHolograms;
            }
        } else {
            PlayerHolograms.removeExternalPlayerHologram(holo);
        }

        String actualSkinTexture = config.texture(viewer);
        String actualSkinSignature = config.signature(viewer);
        if (!Objects.equals(getSkinSignature(), actualSkinSignature) || !Objects.equals(getSkinTexture(), actualSkinTexture)) {
            editEntityMeta(MannequinMeta.class, meta -> {
                meta.setProfile(
                    new ResolvableProfile(
                        new PlayerSkin(actualSkinTexture, actualSkinSignature)
                    )
                );
            });
        }
    }
}