package net.swofty.structure;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

@Getter
@Setter
public class IslandPortal extends SkyBlockStructure {
    private PortalType type;

    public IslandPortal(int rotation, int x, int y, int z) {
        super(rotation, x, y, z);
    }

    @Override
    public void build(Instance instance) {
        fill(instance,
                -1, 0, 0,
                1, 4, 0,
                Block.NETHER_PORTAL);
    }

    public enum PortalType {
        HUB
    }
}
