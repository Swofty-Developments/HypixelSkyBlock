package net.swofty.types.generic.minion.minions.foraging;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionCutTreeAction;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionFlower extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 30, 960,
                        "baa7c59b2f792d8d091aecacf47a19f8ab93f3fd3c48f6930b1c2baeb09e0f9b",
                        Material.WOODEN_HOE, false),
                new MinionTier(2, 29, 960,
                        "ddb0a9581e7d5f989d4fb6350fc7c51d65b3e49e4a0be35c3f3523287a0ff979",
                        Material.WOODEN_HOE, true),
                new MinionTier(3, 28, 960,
                        "de5c24a8bcd21e4f0a37551a2ad197a798be986ef08371ab11e95c2044bb1bc0",
                        Material.STONE_HOE, true),
                new MinionTier(4, 27, 960,
                        "5d473a99697430f82786b331c9657adef370655492b6763de9ea24066168ab41",
                        Material.STONE_HOE, true),
                new MinionTier(5, 26, 960,
                        "6367c303c2c4f6ef4e0feeb528f37bcd71c1c0765621259ff00530c5d99b584b",
                        Material.STONE_HOE, true),
                new MinionTier(6, 25, 960,
                        "b8ffa832227440cbd8f218fb20f2cca7f9778024814b4e92bd5112d0a3f4b7f9",
                        Material.IRON_HOE, true),
                new MinionTier(7, 24, 960,
                        "3bc942565909d05cb447945726411a3da83dcaa0a5c9b04fa041c3c5ca84e955",
                        Material.IRON_HOE, true),
                new MinionTier(8, 23, 960,
                        "8959d9c639b20b294cf6cd0726422092682e762d3991ddac39d92cdc60334103",
                        Material.IRON_HOE, true),
                new MinionTier(9, 22, 960,
                        "4beed0b166465261f07399fe97304b9913f522e0d42e78d86849ec72be3d7fa9",
                        Material.GOLDEN_HOE, true),
                new MinionTier(10, 20, 960,
                        "d719f6041aaaf6c7b55042a550d51e17af727f6b8e41af09a1aded49c9ff9e31",
                        Material.GOLDEN_HOE, true),
                new MinionTier(11, 18, 960,
                        "1142fe535855dd6b06f4f0817dbc8bf98da31265ae918b854cd11fcacd6fab4c",
                        Material.DIAMOND_HOE, true),
                new MinionTier(12, 15, 960,
                        "6762823b4a9b3a3515044d0bc527ac33f7d13d73d80184fcebecd240f9d66703",
                        Material.DIAMOND_HOE, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(23,198,38);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(23,198,38);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(23,198,38);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.DANDELION, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.DANDELION);
    }
}
