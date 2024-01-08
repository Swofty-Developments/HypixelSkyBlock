package net.swofty.types.generic.entity.npc;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.*;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NPCEntityImpl extends Entity {
    @Getter
    private ArrayList<SkyBlockPlayer> inRangeOf = new ArrayList<>();
    private final ArrayList<Player> packetsSent = new ArrayList<>();
    private final String username;

    private final String skinTexture;
    private final String skinSignature;

    public NPCEntityImpl(@NotNull String bottomDisplay, @Nullable String skinTexture, @Nullable String skinSignature) {
        super(EntityType.PLAYER);
        this.username = bottomDisplay;

        this.skinTexture = skinTexture;
        this.skinSignature = skinSignature;
        this.uuid = UUID.randomUUID();

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
                new SpawnPlayerPacket(getEntityId(), getUuid(), getPosition()),
                new EntityHeadLookPacket(getEntityId(), getPosition().yaw()),
                new EntityMetaDataPacket(getEntityId(), Map.of(17, Metadata.Byte((byte) 127)))
        );
        setInvisible(true);

        packetsSent.add(player);
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (packetsSent.contains(player)) {
                player.sendPacket(new PlayerInfoRemovePacket(uuid));
            }
        }, TaskSchedule.seconds(3), TaskSchedule.stop());
    }

    @Override
    public void updateOldViewer(@NotNull Player player) {
        super.updateOldViewer(player);

        player.sendPacket(new PlayerInfoRemovePacket(getUuid()));

        packetsSent.remove(player);
    }

    /**
     * Clears the cache for a player, is only run on quit, {@see QuitAction.java}
     *
     * @param player
     */
    public void clearCache(SkyBlockPlayer player) {
        inRangeOf.remove(player);
        packetsSent.remove(player);
    }
}