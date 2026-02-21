package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.MetadataDef;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityHeadLookPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private final ArrayList<Player> packetsSent = new ArrayList<>();
    private final String username;

    private final String skinTexture;
    private final String skinSignature;
    private String[] holograms;

    public NPCEntityImpl(@NotNull HypixelPlayer viewer, @NotNull Pos pos, @NotNull String bottomDisplay, @Nullable String skinTexture, @Nullable String skinSignature, @NotNull String[] holograms, HumanConfiguration config, boolean overflowing) {
        super(EntityType.PLAYER, UUID.randomUUID());
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
            .pos(pos.add(0, getEyeHeight() + 0.5f + (overflowing ? -0.2f : 0f), 0))
            .text(Arrays.copyOfRange(holograms, 0, holograms.length - (overflowing ? 0 : 1)))
            .player(viewer)
            .instance(config.instance())
            .build();

        this.holo = holo;
        PlayerHolograms.addExternalPlayerHologram(holo);

        setInstance(config.instance(), pos);
        addViewer(viewer);
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

        List<PlayerInfoUpdatePacket.Property> properties = new ArrayList<>();
        if (skinTexture != null && skinSignature != null) {
            properties.add(new PlayerInfoUpdatePacket.Property("textures", skinTexture, skinSignature));
        }

        player.sendPackets(
            new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                new PlayerInfoUpdatePacket.Entry(
                    getUuid(),
                    username,
                    properties,
                    false,
                    0,
                    GameMode.CREATIVE,
                    Component.text("§8[NPC] " + getUuid().toString().substring(0, 8)),
                    null,
                    1, true)),
            new SpawnEntityPacket(this.getEntityId(), this.getUuid(), EntityType.PLAYER,
                getPosition(),
                (float) 0,
                0,
                Vec.ZERO),
            new EntityHeadLookPacket(getEntityId(), getPosition().yaw()),
            new EntityMetaDataPacket(getEntityId(), Map.of(
                MetadataDef.Avatar.DISPLAYED_MODEL_PARTS_FLAGS.index(),
                Metadata.Byte((byte) 127) // 127 is all parts
            ))
        );


        packetsSent.add(player);
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (packetsSent.contains(player)) {
                player.sendPacket(new PlayerInfoRemovePacket(getUuid()));
            }
        }, TaskSchedule.tick(2), TaskSchedule.stop());
    }

    @Override
    public void updateOldViewer(@NotNull Player player) {
        super.updateOldViewer(player);

        player.sendPacket(new PlayerInfoRemovePacket(getUuid()));

        packetsSent.remove(player);
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
        if (!getPosition().asVec().equals(config.position(viewer).asVec())) {
            String[] holograms = config.holograms(viewer);
            Pos npcPosition = config.position(viewer);

            boolean overflowing = holograms[holograms.length - 1].length() > 16;
            float yOffset = overflowing ? -0.2f : 0.0f;
            PlayerHolograms.relocateExternalPlayerHologram(holo, npcPosition.add(0, 1.1f + yOffset, 0));
        }

        String[] newHolograms = config.holograms(viewer);
        boolean isOverflowing = newHolograms[newHolograms.length - 1].length() > 16;
        String[] finalHolograms = Arrays.copyOfRange(newHolograms, 0, newHolograms.length - (isOverflowing ? 0 : 1));
        if (!Arrays.equals(finalHolograms, holograms)) {
            PlayerHolograms.updateExternalPlayerHologramText(holo, finalHolograms);
            this.holograms = finalHolograms;
        }

        String actualSkinTexture = config.texture(viewer);
        String actualSkinSignature = config.signature(viewer);
        if (!Objects.equals(getSkinSignature(), actualSkinSignature) || !Objects.equals(getSkinTexture(), actualSkinTexture)) {
            viewer.sendPacket(
                new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                    new PlayerInfoUpdatePacket.Entry(
                        getUuid(),
                        username,
                        List.of(new PlayerInfoUpdatePacket.Property("textures", actualSkinTexture, actualSkinSignature)),
                        false,
                        0,
                        GameMode.CREATIVE,
                        Component.text("§8[NPC] " + getUuid().toString().substring(0, 8)),
                        null,
                        1, true))
            );
        }
    }
}