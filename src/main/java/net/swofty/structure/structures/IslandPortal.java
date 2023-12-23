package net.swofty.structure.structures;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.swofty.structure.SkyBlockStructure;

import java.util.Map;

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

        fill(instance,
                -2, 0, 0,
                -2, 4, 0,
                Block.POLISHED_ANDESITE);

        fill(instance,
                2, 0, 0,
                2, 4, 0,
                Block.POLISHED_ANDESITE);

        fill(instance,
                2, 5, 0,
                -2, 5, 0,
                Block.STONE);

        set(instance, 1, 5, 1, Block.COBBLESTONE_WALL);
        set(instance, -1, 5, 1, Block.COBBLESTONE_WALL);
        set(instance, 1, 5, -1, Block.COBBLESTONE_WALL);
        set(instance, -1, 5, -1, Block.COBBLESTONE_WALL);

        set(instance, 3, 5, 1, Block.COBBLESTONE_WALL);
        set(instance, -3, 5, 1, Block.COBBLESTONE_WALL);
        set(instance, 3, 5, -1, Block.COBBLESTONE_WALL);
        set(instance, -3, 5, -1, Block.COBBLESTONE_WALL);

        fill(instance,
                -3, 6, -1,
                3, 6, 1,
                Block.STONE_BRICK_SLAB);

        fill(instance,
                -3, 4, -1,
                3, 4, -1,
                Block.STONE_BRICK_SLAB.withProperties(Map.of("type", "top")));
        fill(instance,
                -3, 4, 1,
                3, 4, 1,
                Block.STONE_BRICK_SLAB.withProperties(Map.of("type", "top")));

        set(instance, 2, 0, 1, Block.COBBLESTONE_WALL);
        set(instance, -2, 0, 1, Block.COBBLESTONE_WALL);
        set(instance, 2, 1, 1, Block.TORCH);
        set(instance, -2, 1, 1, Block.TORCH);
    }

    public enum PortalType {
        HUB
    }
}
