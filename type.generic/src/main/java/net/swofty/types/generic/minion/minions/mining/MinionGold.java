package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionGold extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 22, 64,
                        "9908235a1cbecc03a72cdf710f4ed519d65b4da62b54a4ef98a7408fcf51b83b",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 22, 192,
                        "9dc1b7433a6b73ed7c9730af9e5707e09e1a4cc12adf5efde0630c518acd0962",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 20, 192,
                        "dca0983a6931cc5aa0e7ca19df16bb112d0aac598b75e63adf13e7b01c777446",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 20, 384,
                        "eab7a8f6d05623513a7d89343a51321c789e8a2b12a7fb7a0f540cfe4a3f96ac",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 18, 384,
                        "79de196f73cd05f63ce63d814e00437fea58ccf00aff98a9788381fc5f9f0434",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 18, 576,
                        "f59502e936c043f89e799096410c3ab2cf3dff270c0378440aee00aff1fa9c5e",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 16, 576,
                        "30c210dd7790579c9a1a6b6ebc39f85c2f728cc289031fbda92067fc2717ba0d",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 16, 768,
                        "908c7d47bfb82d4ea76941ed801894e3fe97404e51110f5f9f7b83e6e17e49e6",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 14, 768,
                        "9c7135715969305402d12b55879005035b3ee21e25498549366ccff6a0264223",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 14, 960,
                        "fbc5c2156615f13c4c927c0c345528e5ccbcde26a332d2937490947aa0734601",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 11, 960,
                        "6c978f9d85b9a6242e1841e444aa5c473418155958fce2e7b055f8938a7d5615",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 9, 960,
                        "26c39ce1b9574bfbb5c6a6a2684d262e62a15f12d235276e4fffc1ab4cdc85b3",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(255, 215, 0); // Gold minion's boot color
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(255, 215, 0); // Gold minion's leggings color
    }

    @Override
    public Color getChestplateColour() {
        return new Color(255, 215, 0); // Gold minion's chestplate color
    }


    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.GOLD_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.GOLD_ORE);
    }
}
