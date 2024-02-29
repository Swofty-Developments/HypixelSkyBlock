package net.swofty.types.generic.minion.minions;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionCoal extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 15, 64),
                new MinionTier(2, 15, 192),
                new MinionTier(3, 13, 192),
                new MinionTier(4, 13, 384),
                new MinionTier(5, 12, 384),
                new MinionTier(6, 12, 576),
                new MinionTier(7, 10, 576),
                new MinionTier(8, 10, 768),
                new MinionTier(9, 9, 768),
                new MinionTier(10, 9, 960),
                new MinionTier(11, 7, 960),
                new MinionTier(12, 6, 960)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(0,0,0);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(0,0,0);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(0,0,0);
    }

    @Override
    public String getTexture() {
        return "425b8d2ea965c780652d29c26b1572686fd74f6fe6403b5a3800959feb2ad935";
    }

    @Override
    public Material getHeldItem() {
        return Material.IRON_PICKAXE;
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.COAL_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.COAL_ORE);
    }
}
