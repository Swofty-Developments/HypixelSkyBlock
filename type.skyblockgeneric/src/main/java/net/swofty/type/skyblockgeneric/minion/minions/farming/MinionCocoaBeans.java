package net.swofty.type.skyblockgeneric.minion.minions.farming;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;
import net.swofty.type.skyblockgeneric.minion.actions.MinionMineCocoaBeansAction;

import java.util.List;

public class MinionCocoaBeans extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 27, 64,
                        "acb680e96f6177cd8ffaf27e9625d8b544d720afc50738801818d0e745c0e5f7",
                        Material.WOODEN_HOE, true),
                new MinionTier(2, 27, 192,
                        "475cb9dcc1b3c33aca8220834588d457f9f771235f37d62050544be2f2825d1b",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 25, 192,
                        "1d569fdd54d61e55c84960271950ce755d60ea6dc03c427773098649e8b7136d",
                        Material.STONE_HOE, true),
                new MinionTier(4, 25, 384,
                        "5ed37c0b33043212ad9527df957be53f0e0fb08c184648cf0d2a64775fb6b4ec",
                        Material.STONE_HOE, true),
                new MinionTier(5, 23, 384,
                        "4ea5d503ed03184a906ad29a8b1809f20ba95b99bb889a8e6d04c2cc586c6412",
                        Material.STONE_HOE, true),
                new MinionTier(6, 23, 576,
                        "b1db22b8f0a12492c2c7cf2784025c6cad2afc66998c4f47c0f02e6100454851",
                        Material.IRON_HOE, true),
                new MinionTier(7, 21, 576,
                        "afdfa53bdd3937be5305a2ef17b3f80860d12b85000dd51a80a4f3f9b744998b",
                        Material.IRON_HOE, true),
                new MinionTier(8, 21, 768,
                        "fa73332b8e1e64e172f4e8ccb58f93e78d06185db298b409eccedf6d6f6ebde3",
                        Material.IRON_HOE, true),
                new MinionTier(9, 18, 768,
                        "db215abd78aced038772b6f73d828dbfc33369d7e9e00a58539e989508da6911",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 18, 960,
                        "80c4434c532a0e1a41dad610989f8a01432ea47adc39d64ec81fef81284d581",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 15, 960,
                        "d71be56d6fbfec9e2602737dc3df8409368e23fb854b353b2451c30daa8c425b",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 12, 960,
                        "7ca478e978e511fb038db892d1fbab5326f01fa7520e81513db654f780321db9",
                        Material.DIAMOND_HOE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(178,120,21);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(178,120,21);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(178,120,21);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(0, Block.JUNGLE_LOG, Block.COCOA, Block.AIR),
                new MobGapExpectation(1)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineCocoaBeansAction();
    }
}
