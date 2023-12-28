package net.swofty.event.custom;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.user.SkyBlockIsland;
import net.swofty.user.SkyBlockPlayer;

@Getter
public class CustomBlockBreak implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final Block block;
    private final Point point;

    public CustomBlockBreak(SkyBlockPlayer player, Block block, Point point) {
        this.player = player;
        this.block = block;
        this.point = point;
    }
}
