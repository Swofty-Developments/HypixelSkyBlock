package net.swofty.types.generic.chest;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.BlockActionPacket;

import java.util.Arrays;

public enum ChestAnimationType {
    OPEN,
    CLOSE;

    public void play(Instance instance , Point[] positions){
        Arrays.stream(positions).forEach((position) -> {
            BlockActionPacket actionPacket = new BlockActionPacket(
                    position,
                    (byte) 1,
                    this == ChestAnimationType.OPEN ? (byte) 1 : 0,
                    instance.getBlock(position)
            );
            instance.getPlayers().forEach(player -> player.sendPacket(actionPacket));
        });
    }
}
