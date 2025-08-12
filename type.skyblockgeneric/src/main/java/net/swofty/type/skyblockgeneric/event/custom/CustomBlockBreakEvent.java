package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.Material;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

@Getter
public class CustomBlockBreakEvent implements PlayerInstanceEvent {
    private final HypixelPlayer player;
    private final Material material;
    private final Point point;
    private final List<SkyBlockItem> drops;
    private final Boolean playerPlaced;

    public CustomBlockBreakEvent(HypixelPlayer player, Material material, Point point, List<SkyBlockItem> drops, Boolean playerPlaced) {
        this.player = player;
        this.material = material;
        this.point = point;
        this.drops = drops;
        this.playerPlaced = playerPlaced;
    }
}
