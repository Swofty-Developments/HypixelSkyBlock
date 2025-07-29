package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

@Getter
public class CustomBlockBreakEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final Material material;
    private final Point point;
    private final List<SkyBlockItem> drops;
    private final Boolean playerPlaced;

    public CustomBlockBreakEvent(SkyBlockPlayer player, Material material, Point point, List<SkyBlockItem> drops, Boolean playerPlaced) {
        this.player = player;
        this.material = material;
        this.point = point;
        this.drops = drops;
        this.playerPlaced = playerPlaced;
    }
}
