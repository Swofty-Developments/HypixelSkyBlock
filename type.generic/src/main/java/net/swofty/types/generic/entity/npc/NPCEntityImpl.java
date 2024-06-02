package net.swofty.types.generic.entity.npc;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public class NPCEntityImpl extends Entity {
    @Getter
    private List<SkyBlockPlayer> inRangeOf = Collections.synchronizedList(new ArrayList<>());
    private final ArrayList<Player> packetsSent = new ArrayList<>();
    private final String username;

    private final String skinTexture;
    private final String skinSignature;
    private final String[] holograms;

    public NPCEntityImpl(@NotNull String bottomDisplay, @Nullable String skinTexture, @Nullable String skinSignature, @NotNull String[] holograms) {
        super(EntityType.PLAYER);
        this.username = bottomDisplay;

        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;
        this.uuid = UUID.randomUUID();
        this.holograms = holograms;

        if (holograms == null) {
            throw new IllegalArgumentException("Holograms cannot be null");
        }

        setNoGravity(true);
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
                                uuid,
                                username,
                                properties,
                                false,
                                0,
                                GameMode.CREATIVE,
                                Component.text("§8[NPC] " + this.uuid.toString().substring(0, 8)),
                                null)),
                new SpawnEntityPacket(this.getEntityId(), this.getUuid(), EntityType.PLAYER.id(),
                        getPosition(),
                        (float) 0,
                        0,
                        (short) 0,
                        (short) 0,
                        (short) 0),
                new EntityHeadLookPacket(getEntityId(), getPosition().yaw()),
                new EntityMetaDataPacket(getEntityId(), Map.of(17, Metadata.Byte((byte) 127)))
        );
        setInvisible(true);

        packetsSent.add(player);
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (packetsSent.contains(player)) {
                player.sendPacket(new PlayerInfoRemovePacket(uuid));
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