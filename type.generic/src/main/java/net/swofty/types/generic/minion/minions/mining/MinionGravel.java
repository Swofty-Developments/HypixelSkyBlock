package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionGravel extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 64,
                        "4a3700896117fd94889b0216d124f1e67e4f0e9ca3b04d168f849a297be2047d",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(2, 26, 192,
                        "42ff2723653485266994f373dfc42ae7191e915a988c0a211c08512d49537e2a",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(3, 24, 192,
                        "f6bade9160f39ac68e23b865407768e9ef2aff684a5cb4c5990217eeef5edefc",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(4, 24, 384,
                        "2ee6c57f32d447a2d80fbe2a8a2b75df49f1ad658a550fe76d3510efb1149917",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(5, 22, 384,
                        "b1dc11062ef2fc5ae04ca2b473a9ac68c0f238ac17c0ead8927e32b7a62bbda2",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(6, 22, 576,
                        "40dce9099737f40505ca6052a6d1ae89d130455f4d347c0114efd50abc3a1af4",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(7, 19, 576,
                        "e95a9f97387f3892298e855075145823888e308c8c31aaf176d3d57fd24acea3",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(8, 19, 768,
                        "dd0ea7955d363aeb4d1e6965e64c52744da0aeb7fe8c976617cb8f0e8b6d491d",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(9, 16, 768,
                        "f13abcf4dedd27e9d19973b7b5365c7f7105141af237d85390c1eea8e8d31950",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(10, 16, 960,
                        "289b398bf60585fb5ab4c6a8bd07bb9f3699af0d4884a3d5b6ff9285e76e782f",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(11, 13, 960,
                        "1479dca9f84860056e5fb5f56b2edcf3c90b7375e305eaeaf319a3bf2b0121e5",
                        Material.WOODEN_SHOVEL, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(130, 130, 130); // Gravel minion's boot color (Gray)
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(130, 130, 130); // Gravel minion's leggings color (Gray)
    }

    @Override
    public Color getChestplateColour() {
        return new Color(130, 130, 130); // Gravel minion's chestplate color (Gray)
    }



    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.GRAVEL, Block.AIR)
        );
    }
    //hm

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.GRAVEL);
    }
}
