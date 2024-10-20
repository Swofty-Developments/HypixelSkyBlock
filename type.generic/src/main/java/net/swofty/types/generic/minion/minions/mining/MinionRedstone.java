package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionRedstone extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 29, 64,
                        "1edefcf1a89d687a0a4ecf1589977af1e520fc673c48a0434be426612e8faa67",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 29, 192,
                        "4ebdbf0aca7d245f6d54c91c37ec7102a55dd0f3b0cfe3c2485f3a99b3e53aa0",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 27, 192,
                        "c1a5175a1caf7a88a82b88b4737159132a68dc9fc99936696b1573ea5a7bb76d",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 27, 384,
                        "bbf83cb38bd6861b33665c1c6f56e29cbc4a87a2f494581999d51d309d58d0aa",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 25, 384,
                        "d96fa75edd9bc6e1d89789e58a489c4594d406dd93d7c566ed4534971b52c118",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 25, 576,
                        "9cfd7010be9a08edd1e91c4203fccff6ddf71e680e4dfb4d32c38dee99d4a389",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 23, 576,
                        "18db0ef0af4853603a3f663de24381159e9faaa1cdf93b026719dab050ea9954",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 23, 768,
                        "a40b85c00f824f61beefd651c9588698e49d01902e84a098f79ee09941d8e4ac",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 21, 768,
                        "85d61b9d0b8ad786e8e1ff1dbbde1221a8691fda1daf93c8605cbc2e4fdea63",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 21, 960,
                        "6588bed4136c95dd961b54a06307b2489726bbfe4fda41cee8ab2c57fa36f291",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 18, 960,
                        "6670498256b1cbae7c8463bc2d65036cf07447b146f7d3f69bfa2dc07e9fd8cf",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 16, 960,
                        "968eaaaba0cd0a75228fa399a786584125cf431a6a910c34dda891dd64a6693b",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(219, 79, 15);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(219, 79, 15);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(219, 79, 15);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.REDSTONE_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.REDSTONE_ORE);
    }
}
