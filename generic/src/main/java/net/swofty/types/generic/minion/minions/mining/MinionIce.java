package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionIce extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 14, 64,
                        "4f12bf76811df2c020cecd34cd8d1f2aed28a560674328130d2d35a0e9b5c7b9",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 14, 192,
                        "fa602c5c2ba70cd6d4f0a88d7d2fe9b3d9737ddc40e5dfc693f69435020df6fb",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 12, 192,
                        "24b52b34966099715123e2c5794b23e410f1b5e3a1c5b7f5229508d2f329e139",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 12, 384,
                        "1e3fa04dc3bb8e0316c8d78becabadf02174d07912ff985aa41ec0053eaa0b5d",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 10, 384,
                        "a9eb2b8e4f343932840eccb734d5d27fd05fd0a6bae5be7631dafc0b6a01159a",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 10, 576,
                        "9c02d7e4885503e8cc3a4d9e09a205bdc9a3e8eec6a8f2756a95f4626116355d",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 9, 576,
                        "a2e1e8dfcf46fea63cb9c3f75cc1afd9abe7bef1179288a8ecbb90d72c7742f",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 9, 768,
                        "6f451272b76cc381f83adeca004af9bceae6858f0154110b40bad5ab70deb841",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 8, 768,
                        "bf16a2dae76f44ea65b37b49d420ba711051d84761b96ced0803abe402b9790d",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 8, 960,
                        "9777152e040f3d4089f949eafcfc38d17180c443ff5072a78dace23b3adf786c",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 7, 960,
                        "1fb459e45fdd34410832212ad1afb23fd69eb276d0b457fe38761462770caf99",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 6, 960,
                        "c0bc28604e6b9ffe9dda08372b777a9bd5492d6641168fa453463ea2b215c7fb",
                        Material.DIAMOND_PICKAXE, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(173, 216, 230); // Ice minion's boot color (Light Blue)
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(173, 216, 230); // Ice minion's leggings color (Light Blue)
    }

    //hm

    @Override
    public Color getChestplateColour() {
        return new Color(173, 216, 230); // Ice minion's chestplate color (Light Blue)
    }


    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.ICE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.ICE);
    }
}
