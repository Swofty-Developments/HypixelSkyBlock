package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.instance.block.Block;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@Getter
public class ArrowHitBlockEvent implements Event {
    private final Entity shooter;
    private final SkyBlockItem arrowItem;
    private final boolean isSkyBlockPlayer;
    private final Block hitBlock;
    private final Point hitPosition;

    public ArrowHitBlockEvent(Entity shooter, SkyBlockItem arrowItem, Block hitBlock, Point hitPosition) {
        this.shooter = shooter;
        this.arrowItem = arrowItem;
        this.isSkyBlockPlayer = shooter instanceof SkyBlockPlayer;
        this.hitBlock = hitBlock;
        this.hitPosition = hitPosition;
    }
}
