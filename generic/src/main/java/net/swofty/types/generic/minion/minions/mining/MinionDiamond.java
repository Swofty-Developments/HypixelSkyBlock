package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionDiamond extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 29, 64,
                        "fdb008068823844d129efd839c830650d98eea8104cf2b326a9be7dfb17ab99d",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 29, 192,
                        "f2d204f3b4bb4dc37a0949f587788489a1cd148821a107ab057582cec1bcd103",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 27, 192,
                        "ab8b43b33eb98b3f8e8fcbfb11929b736a56d700163df4e0dcda21c397d81c71",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 27, 384,
                        "946ae99a3a9f3ee6263e4d0976d7fa4cb6f0557c0b6145c0cbe8138e364954de",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 25, 384,
                        "107b904d172c7224f29ffa446d79770c14c40e0b11488a014d3ab6780577fdc8",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 25, 576,
                        "56f56d949394e25e5686b9f1800bb5df7c530f9e16785edb7b40076a5f93b9a3",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 22, 576,
                        "c45fd0c1dda1d4b00ab36348fcdccef5ba19cb92009c03c6e47291d6aa986d15",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 22, 768,
                        "3886a33e4ed974a4ceb231780b2316b630e434874249fe50f39197c45ee4c1b8",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 19, 768,
                        "54a90f57cffe06ee124aea38b32d66f7016ae9db64a50508ca5c74aecf8fde6e",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 19, 960,
                        "64b85d29993d1b35ee404bdc5d16c5a04a76885fd95fd337f20143c39b575a79",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 15, 960,
                        "d0c3fe68740047ca97623ee975db16902ab541ccc611f4d67056d593bf089abb",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 12, 960,
                        "a59c8a65014721b7dc612cefcadf263f0496c41430937d1802d6cfad9947c87b",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(122, 121, 100); // Diamond minion's boot color
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(122, 121, 100); // Diamond minion's leggings color
    }

    @Override
    public Color getChestplateColour() {
        return new Color(122, 121, 100); // Diamond minion's chestplate color
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.DIAMOND_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.DIAMOND_ORE);
    }
}
