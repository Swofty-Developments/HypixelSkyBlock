package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionSand extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 64,
                        "81f8e2ad021eefd1217e650e848b57622144d2bf8a39fbd50dab937a7eac10de",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(2, 26, 192,
                        "2ab4e2bdb878de70505120203b4481f63611c7feac98db194f864be10b07b87e",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(3, 24, 192,
                        "a53d8f2c1449bc5b89e485182633b26970538e74410ac9e6e4f5eb1195c36887",
                        Material.STONE_SHOVEL, true),
                new MinionTier(4, 24, 384,
                        "847709a9f5bae2c5e727aee4be706a359c51acb842aafa1a4d23fb62f73e9aa6",
                        Material.STONE_SHOVEL, true),
                new MinionTier(5, 22, 384,
                        "52b94ddeedecce5f90f9d227015dd6071c314cf0234433329e53f5b26b8cf890",
                        Material.STONE_SHOVEL, true),
                new MinionTier(6, 22, 576,
                        "7a756b6a9735b74031b284be6064898f649e5bb4d1300aafc3c0b280dad04b69",
                        Material.IRON_SHOVEL, true),
                new MinionTier(7, 19, 576,
                        "13a1a8b92d83d2200d172d4bbda8d69e37afeb676d214b83af00f246c267dcd2",
                        Material.IRON_SHOVEL, true),
                new MinionTier(8, 19, 768,
                        "765db90f1e3dab4df3a5a42cd80f7e71a92ea4739395df56f1750c73c27cdc4f",
                        Material.IRON_SHOVEL, true),
                new MinionTier(9, 16, 768,
                        "281ccdfe00a7843bce0c109676c1b59dd156389f730f00d3987c10aef64a7f96",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(10, 16, 960,
                        "fdceae5bc34dee02b31a68b0015d0ca808844e491cf926c6763d52b26191993f",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(11, 13, 960,
                        "c0e9118bcebf481394132a5111fcbcd9981b9a99504923b04794912660e22cea",
                        Material.DIAMOND_SHOVEL, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(249, 229, 97);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(249, 229, 97);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(249, 229, 97);
    }

    @Override
    public List<MinionExpectation> getExpectations() {
        return List.of(
                new BlockExpectation(-1, Block.SAND, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.SAND);
    }
}
