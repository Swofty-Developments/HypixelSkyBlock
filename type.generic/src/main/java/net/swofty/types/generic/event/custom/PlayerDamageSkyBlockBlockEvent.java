package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import org.jetbrains.annotations.NotNull;

public class PlayerDamageSkyBlockBlockEvent implements PlayerInstanceEvent {
    private final Player player;
    @Getter
    private final Point blockPosition;
    @Getter
    private final ClientPlayerDiggingPacket.Status status;

    public PlayerDamageSkyBlockBlockEvent(Player player, Point blockPosition, ClientPlayerDiggingPacket.Status status) {
        this.player = player;
        this.blockPosition = blockPosition;
        this.status = status;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
