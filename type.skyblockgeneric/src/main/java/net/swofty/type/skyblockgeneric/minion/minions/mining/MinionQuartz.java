package net.swofty.type.skyblockgeneric.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;
import net.swofty.type.skyblockgeneric.minion.actions.MinionMineAction;

import java.util.List;

public class MinionQuartz extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 22.5f, 64,
                        "d270093be62dfd3019f908043db570b5dfd366fd5345fccf9da340e75c701a60",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 22.5f, 192,
                        "c305506b47609d71488a793c12479ad8b990f7f39fd7de53a45f4c50874d1051",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 21, 192,
                        "83f023160a3289b9c21431194940c8e5f45c7e43687cf1834755151d7c2250f7",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 21, 384,
                        "c2bc6c98d4cbab68af7d8434116a92a351011165f73a3f6356fb88df8af40a49",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 19, 384,
                        "5c0e10de9331da29e0a15e73475a351b8337cd4725b8b24880fb728eb9d679dd",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 19, 576,
                        "300120cabf0ae77a143adca34b9d7187ca1ef6d724269b256d5e3663c7f19bd9",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 17, 576,
                        "bde647431a27149bf3f462a22515863af6c36532c1f66668688131ca11453fd1",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 17, 768,
                        "9899278d0464397dd076408812eef40758f75b1cdb82c04c08c81503453e07e6",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 14.5f, 768,
                        "2974bc0b9771a4af994ea571638adf1e98cd896acf95cc27b890915669bcedfd",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 14.5f, 960,
                        "3ae41345d675f4ed4dc5145662303123cb828b6e1a3e72d8278174488562dfa9",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 11.5f, 960,
                        "7aeec9ef192e733bfcb723afd489cbf4735e7cfdd2ec45cae924009a8f093708",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 10, 960,
                        "cb2903b51e16da7d778b7d0352c3ff08a21245bc476b73cdb82417ba4ff8a139",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(247, 246, 242);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(247, 246, 242);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(247, 246, 242);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.NETHER_QUARTZ_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.NETHER_QUARTZ_ORE);
    }
}
