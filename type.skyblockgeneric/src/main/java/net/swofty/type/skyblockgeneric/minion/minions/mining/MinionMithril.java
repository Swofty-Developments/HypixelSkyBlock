package net.swofty.type.skyblockgeneric.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.minion.MinionAction;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;
import net.swofty.type.skyblockgeneric.minion.actions.MinionMineAction;

import java.util.List;

public class MinionMithril extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 80, 64,
                        "c62fa670ff8599b32ab344195ba15f3ef64c3a8aa8a37821c08375950cb74cd0",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 80, 192,
                        "a17035b9d12e07ca8e1ef1e0ed419787363c9ae7cd35f4dd092f4f0c54d841bc",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 75, 192,
                        "246cab4c33e2132dbedb1205fabe28ba7f13c7f7b0101f252c69460325f7179d",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 75, 384,
                        "ca61d14dbaedc3e758f6fc3b7c5e7df2b5b11cb027fc4351226d39cbf5695928",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 70, 384,
                        "89224b56ea053a952a8044415874b63e4813e9118a56c4fe7027464438783044",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 70, 576,
                        "6d7416596bb1acc5864f18d6e9f08fde7406b53017c460a30ea2da6472f8e75f",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 65, 576,
                        "eeb1bef854222bee8ca543478c30cb6068a1cb80ce68d756513d5fa26ccc56a3",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 65, 768,
                        "b522693f95a8fc95179d3dd3d41ae045cb8502362f3c8d70c53d7819b4ab5010",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 60, 768,
                        "6d35c0c2a1e1e09f75f1e7695a6757014f4fa601bdd1b7a0b6a64ce529fd29eb",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 60, 960,
                        "420f70ad97f1dade93ce1d560de9ea61313966cf9ef09bf4fe5fbbbff6085405",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 55, 960,
                        "3ca2b317a1fd2c15fce3f477182babb3c09b9933afc83d5cdb2bcb091e60f0e3",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 50, 960,
                        "2939c105c2eedbf04600b07de5801e42ec9329054feef89cb0b6a956034e6ee4",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(208,158,60);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(208,158,60);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(208,158,60);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.PRISMARINE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.PRISMARINE);
    }
}
