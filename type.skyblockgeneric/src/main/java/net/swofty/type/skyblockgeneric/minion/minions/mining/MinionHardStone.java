package net.swofty.type.skyblockgeneric.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;
import net.swofty.type.skyblockgeneric.minion.actions.MinionMineAction;

import java.util.List;

public class MinionHardStone extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 14, 64,
                        "1e8bab9493708beda34255606d5883b8762746bcbe6c94e8ca78a77a408c8ba8",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 14, 192,
                        "e3e2ac0b65f1556750effa8f465899de5e914d80fab46a81bebbfff7993a5473",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 12, 192,
                        "ba048e4983e5e0790aa358f2ac14a5f18ba59ad96a74c5d56492882c23c62c26",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 12, 384,
                        "f5d6e71fe84b9315d410bc4ddf0daecee4f1d927b6ca115cb5b7b06e571a0eb",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 10, 384,
                        "ac8e3e68c469cb33141bddd81b5ca6fdcb71d3af399b6d6e4d48e06aab33e638",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 10, 576,
                        "89ebee06206f65094a6baac7c195c59b47b119af863acbfb6ef80aed04db2923",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 9, 576,
                        "8744ed895d580938e1d0b7403d023e3fc0ec0123dd1ee43ed3db8a00be977c20",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 9, 768,
                        "bd239e23a8b4dca4a8a5b565b5254bf861aac24b38c7c6e2a06f4a4b0d658193",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 8, 768,
                        "a897f4e0ed295df1c711450b42766bb11835b17770fc90f84f29330ab4a908d1",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 8, 960,
                        "5c2e3ff6d131440211dfdff76ad15007bc91243a8359d6a67b784e37f900d7f0",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 7, 960,
                        "b37997c9ab7c8d6aa55626d2f1c274b6ded842a6e0a4b1671ef743d2527fa73a",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 6, 960,
                        "c2ed3f2bf20b85828b7742460f8c78fd2001ca0380c455b86dadeb80f28bccbd",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(0,51,204);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(0,51,204);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(0,51,204);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.STONE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.STONE);
    }
}
