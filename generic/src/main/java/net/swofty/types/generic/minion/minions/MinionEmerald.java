package net.swofty.types.generic.minion.minions;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionEmerald extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 28, 64,
                        "a5ea54a75201d645b20cb34a2a155db54a9238fa0b9d925fa3a2ddc066af27c9",
                        Material.WOODEN_PICKAXE),
                new MinionTier(2, 28, 192,
                        "a20372b6e8449291994a040e733ef35f2a31ea99d88f2a3e111ea8441244080c",
                        Material.WOODEN_PICKAXE),
                new MinionTier(3, 26, 192,
                        "535eae18037119d628f68a44fe8d4b7c28403a4021d7d98b028b82d281176d3a",
                        Material.STONE_PICKAXE),
                new MinionTier(4, 26, 384,
                        "236a449d572a65a14368e641e6c06fe893ba29ac6db0a7e0923fecf1432b4957",
                        Material.STONE_PICKAXE),
                new MinionTier(5, 10, 384,
                        "f5dd62137e50eee0951171ac9966e0d8ab49ca3e52e1b1eed4711f7dce8f60f4",
                        Material.STONE_PICKAXE),
                new MinionTier(6, 10, 576,
                        "a5fc61967af63216fd3832b6ee423c79bd1d1d3feb49aede404b6db3f0aa7cc4",
                        Material.IRON_PICKAXE),
                new MinionTier(7, 9, 576,
                        "a7c0fe964ff99300c9298eacb33263bca1108c4f40a6e5ffeec4deb394f2596c",
                        Material.IRON_PICKAXE),
                new MinionTier(8, 2, 768,
                        "7f4977ffb6b6fde4dfa52e21e36dd5ec5061c0043a6a7593c4c12e3a3e381b75",
                        Material.IRON_PICKAXE),
                new MinionTier(9, 8, 768,
                        "7735aed3d8311f1f2489ba62ac86a804386c55ffcd06e52c60fd8ed931346acc",
                        Material.GOLDEN_PICKAXE),
                new MinionTier(10, 8, 960,
                        "b9ea83b1359e01b42cd51c49b131fb7a537894fbfca87bdebd46033e5516e02b",
                        Material.GOLDEN_PICKAXE),
                new MinionTier(11, 7, 960,
                        "217d484450de17672cca24b7308e036f1a7ff188c223f95529549d586376ca00",
                        Material.DIAMOND_PICKAXE),
                new MinionTier(12, 6, 960,
                        "d4101619e77eed74b94b40a8d95ef2ac7eb827954ed87c85253435433f49b877",
                        Material.DIAMOND_PICKAXE)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(0, 255, 0); // Emerald minion's boot color (green)
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(0, 255, 0); // Emerald minion's leggings color (green)
    }

    @Override
    public Color getChestplateColour() {
        return new Color(0, 255, 0); // Emerald minion's chestplate color (green)
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.EMERALD_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.EMERALD_ORE);
    }
}
