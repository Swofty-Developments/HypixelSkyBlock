package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionGlowstone extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 25, 64,
                        "20f4d7c26b0310990a7d3a3b45948b95dd4ab407a16a4b6d3b7cb4fba031aeed",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 25, 192,
                        "c0418cf84d91171f2cd67cbaf827c5b99ce4c1eeba76e77eab241e61e865a89f",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 23, 192,
                        "7b21d7757c8ae382432b606b26ce7854f6c1555e668444ed0eecc2faab55a37d",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 23, 384,
                        "3cc302e56b0474d5e428978704cd4a85de2f6c3e885a70f781e2838b551d5bfc",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 21, 384,
                        "ba8879a5be2d2cc75fcf468054046bc1eb9c61204a66f93991c9ff840a7c57cb",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 21, 576,
                        "cd965a713f2e553c4c3ec047237b600b5ba0de9321a9c7dfe3d47b71d6afda41",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 19, 576,
                        "7f07e68f9985db6c905fe8f4f079137a6deef493413206d4ec90756245b4765e",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 19, 768,
                        "a8507f495bf89912dd2a317ae86faf8ce3631d62ca3d062e9fe5bf8d9d00fd70",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 16, 768,
                        "b30d071e8c97a9c065b307d8a845ef8be6f6db85b71a2299f1bea0be062873e7",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 16, 960,
                        "8eeb870670e9408a78b386db6c2106e93f7c8cf03344b2cb3128ae0a4ea19674",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 13, 960,
                        "8bc66c5eb7a197d959fcc5d45a7aff938e07ddcd42e3f3993bde00f56fe58dd1",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 11, 960,
                        "91701aa68a2d141fe0a85ba4825b67462de4d2cbccd8b52ef018d39e37296ee2",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(249,229,9);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(249,229,9);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(249,229,9);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.GLOWSTONE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.GLOWSTONE);
    }
}
