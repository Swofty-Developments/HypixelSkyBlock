package net.swofty.types.generic.minion.minions;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionSnow extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 13, 64,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(2, 13, 192,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(3, 12, 192,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(4, 12, 384,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(5, 11, 384,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(6, 11, 576,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(7, 9, 576,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(8, 9, 768,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(9, 8, 768,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(10, 8, 960,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(11, 6, 960,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE),
                new MinionTier(12, 5, 960,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.IRON_PICKAXE)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(255,255,255);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(255,255,255);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(255,255,255);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.SNOW_BLOCK, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.SNOW_BLOCK);
    }
}
