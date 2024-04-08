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
                        "1e21f8cfe8cc0e08990d52165e78e6f816f4f8e1ce7cb0f0a6c8ebe1da85e42a",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 29, 192,
                        "c833c2d4757ca1c0b1ffbe7858c8ae363a7f4d1b2b0bbb10b1fb1d4b57329d0",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 27, 192,
                        "e8079ef54395a9cea64d98e1a8165dc2400b73b2400bd16567a5ac40f629e5c7",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 27, 384,
                        "512ae4e2d6f01ce87e6a44af7f7cf74e5f94da97639ca98da0eb6509a3162e5b",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 25, 384,
                        "4265dd62a9ea120b04bf6ca4d0768b209fd2cafe799b22ceed2198f086f47d91",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 25, 576,
                        "ec7c7be1ae6420b986399f24a361655d4ab8c2bd7b841c0470e2bd32ec833737",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 23, 576,
                        "20d9c22822df0a1b2935354c7d9bc3f8c6245b58bfdaa7a2979f19dce8459072",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 23, 768,
                        "7c815dbb615b51cbf55a3c967962c198df1ecc170080e920d539dcebbb6dcbda=",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 21, 768,
                        "63b2a4640e565e8218e4199af2db30d08f58473d8fb017eb8c3165b543ef7395",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 21, 960,
                        "54a658c7cb7b219357ff88d16dde23dae4d0bc5d79aae88733a6434adac1bb74",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 18, 960,
                        "7284cdcde5316be5d361de8080f4d069e220e1bda910d763a53fd8795c2b4952",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 16, 960,
                        "968eaaaba0cd0a75228fa399a786584125cf431a6a910c34dda891dd64a6693b",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(189, 68, 52); // Redstone minion's boot color
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(189, 68, 52); // Redstone minion's leggings color
    }

    @Override
    public Color getChestplateColour() {
        return new Color(189, 68, 52); // Redstone minion's chestplate color
    }



    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.REDSTONE_ORE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.REDSTONE_ORE);
    }
}
