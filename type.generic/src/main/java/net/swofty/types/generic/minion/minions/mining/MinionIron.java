package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionIron extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 17, 64,
                        "573092caea89dd12d368375f1a097796e3bc88f809accf320706bdc048cd4c5e",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 17, 192,
                        "5eb6a6a04abd873e07511b2a4edb7043c6bee92ef8e8783f716bf4c2f5d56ca3",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 15, 192,
                        "dc38929b8cfa59c7cd30939b15dbebbac6b438ae0faff04ed0e46eeceeabf62d",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 15, 384,
                        "d3495292357deb8c0b5f639119d0dcdfe8b4e5f093fc6dd2b9d709e57d1dc920",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 14, 384,
                        "c922032748df76c7c34629d4c9adefeabe80b122c058484b6ad56b8dc4b15322",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 14, 576,
                        "35eb6714e374817d33a5d147758dfd9b3ed88a01833ac896f841d53b6e544555",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 12, 576,
                        "97eb3559d738817b24f7e8c5a06b6d77cd1e6cb95f041dd1a0f85ad6afb25dd7",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 12, 768,
                        "7e860ea9693de8cc73a4087ae2bf686ef8f870e8570e2e4068afe5a34d0a42a5",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 10, 768,
                        "74bb3cc4053f1f068a89a6ac4450c9aacf5da69c4ff54a8944a1ad9d1362d74",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 10, 960,
                        "d3a7641ceb2e5c4ba42174221562ca6c264a422534e2e4098bb8e2f26c5ab36f",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 8, 960,
                        "9e9807e14639226355bd4c05915eaccccac81d9f68b968d5063146cf0979c803",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 7, 960,
                        "9fcff36d3a7717a8aaa337562423b0d884fef6aab12a467fa360e55975972c62",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(180, 180, 180);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(180, 180, 180); // Iron minion's leggings color
    }

    @Override
    public Color getChestplateColour() {
        return new Color(180, 180, 180); // Iron minion's chestplate color
    }


    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.IRON_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.IRON_ORE);
    }
}
