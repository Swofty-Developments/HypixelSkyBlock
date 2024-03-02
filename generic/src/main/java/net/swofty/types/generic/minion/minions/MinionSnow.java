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
                new MinionTier(1, 13, 64),
                new MinionTier(2, 13, 192),
                new MinionTier(3, 12, 192),
                new MinionTier(4, 12, 384),
                new MinionTier(5, 11, 384),
                new MinionTier(6, 11, 576),
                new MinionTier(7, 9.5D, 576),
                new MinionTier(8, 9.5D, 768),
                new MinionTier(9, 8, 768),
                new MinionTier(10, 8, 960),
                new MinionTier(11, 6.5D, 960),
                new MinionTier(12, 5.8D, 960)
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
    public String getTexture() {
        return "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3";
    }

    @Override
    public Material getHeldItem() {
        return Material.IRON_PICKAXE;
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
