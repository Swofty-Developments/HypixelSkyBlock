package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public class NPCEntityImpl extends Entity {
    @Getter
    private List<HypixelPlayer> inRangeOf = Collections.synchronizedList(new ArrayList<>());
    private final ArrayList<Player> packetsSent = new ArrayList<>();
    private final String username;

    private final String skinTexture;
    private final String skinSignature;
    private final String[] holograms;

    public NPCEntityImpl(@NotNull String bottomDisplay, @Nullable String skinTexture, @Nullable String skinSignature, @NotNull String[] holograms) {
        super(EntityType.PLAYER, UUID.randomUUID());
        this.username = bottomDisplay;

        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;
        this.holograms = holograms;

        if (holograms == null) {
            throw new IllegalArgumentException("Holograms cannot be null");
        }

        setNoGravity(true);
        setAutoViewable(false);
    }

    @Override
    public void updateNewViewer(@NotNull Player player) {
        super.updateNewViewer(player);

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
}