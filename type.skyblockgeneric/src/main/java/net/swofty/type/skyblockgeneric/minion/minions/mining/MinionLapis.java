package net.swofty.type.skyblockgeneric.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;
import net.swofty.type.skyblockgeneric.minion.actions.MinionMineAction;

import java.util.List;

public class MinionLapis extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 29, 64,
                        "64fd97b9346c1208c1db3957530cdfc5789e3e65943786b0071cf2b2904a6b5c",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 29, 192,
                        "65be0e9684b28a2531bec6186f75171c1111c3133b8ea944f32c34f247ea6923",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 27, 192,
                        "2a3915a78c2397f2cef96002391f54c544889c5ced4089eb723d14b0a6f02b08",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 27, 384,
                        "97df8ae6e1436504a6f08137313e5e47e17aa078827f3a636336668a4188e6fc",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 25, 384,
                        "aa5d796b9687cc358ea59b06fdd9a0a519d2c7a2928de10d37848b91fbbc648f",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 25, 576,
                        "6e5db0956181c801b21e53cd7eb7335941801a0f335b535a7c9afd26022e9e70",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 23, 576,
                        "1a49908cf8c407860512997f8256f0b831bd8fc4f41d0bf21cd23dbc0bdebb0f",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 23, 768,
                        "c08a219f5cf568c9e03711518fcf18631a1866b407c1315017e3bf57f44ef563",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 21, 768,
                        "e5a93254f20364b7117f606fd6745769994acd3b5c057d3382e5dd828f9ebfd4",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 21, 960,
                        "6fe5c4ceb6e66e7e0c357014be3d58f052a38c040be62f26af5fb9bed437541",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 18, 960,
                        "736cd50c9e8cf786646960734b5e23e4d2e3112f4494d5ddb3c1e45033324a0e",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 16, 960,
                        "edc594af5a8391c59001582b274864cf0701505609ece652c2a356de4617c28e",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(9, 82, 160);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(9, 82, 160);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(9, 82, 160);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.LAPIS_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.LAPIS_ORE);
    }
}
