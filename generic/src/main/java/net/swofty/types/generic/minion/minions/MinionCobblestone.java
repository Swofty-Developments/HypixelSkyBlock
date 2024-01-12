package net.swofty.types.generic.minion.minions;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionCobblestone extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 14, 64),
                new MinionTier(2, 14, 192),
                new MinionTier(3, 12, 192),
                new MinionTier(4, 12, 384),
                new MinionTier(5, 10, 384),
                new MinionTier(6, 10, 576),
                new MinionTier(7, 9, 576),
                new MinionTier(8, 9, 768),
                new MinionTier(9, 8, 768),
                new MinionTier(10, 8, 960),
                new MinionTier(11, 7, 960)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(64, 64, 64);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(64, 64, 64);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(64, 64, 64);
    }

    @Override
    public String getTexture() {
        return "2f93289a82bd2a06cbbe61b733cfdc1f1bd93c4340f7a90abd9bdda774109071";
    }

    @Override
    public Material getHeldItem() {
        return Material.IRON_PICKAXE;
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.COBBLESTONE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.COBBLESTONE);
    }
}
