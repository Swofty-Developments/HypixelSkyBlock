package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.Material;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class CustomBlockBreakEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final Material material;
    private final Point point;

    public CustomBlockBreakEvent(SkyBlockPlayer player, Material material, Point point) {
        this.player = player;
        this.material = material;
        this.point = point;
    }
}
