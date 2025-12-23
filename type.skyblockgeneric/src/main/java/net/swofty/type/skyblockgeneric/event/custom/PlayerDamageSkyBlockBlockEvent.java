package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerActionPacket;
import net.minestom.server.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public class PlayerDamageSkyBlockBlockEvent implements PlayerInstanceEvent {
    private final Player player;
    @Getter
    private final Point blockPosition;
    @Getter
    private final ClientPlayerActionPacket.Status status;
    @Getter
    private final int sequence;

    public PlayerDamageSkyBlockBlockEvent(Player player, Point blockPosition,
                                          ClientPlayerActionPacket.Status status,
                                          int sequence) {
        this.player = player;
        this.blockPosition = blockPosition;
        this.status = status;
        this.sequence = sequence;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
