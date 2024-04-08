package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionSnow extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 13, 64,
                        "f6d180684c3521c9fc89478ba4405ae9ce497da8124fa0da5a0126431c4b78c3",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(2, 13, 192,
                        "69921bab54af140481c016a59a819b369667a4e4fb2f2449ceebf7c897ed588e",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(3, 12, 192,
                        "4e13862d1d0c52d272ece109e923af62aedebb13b56c47085f41752a5d4d59e2",
                        Material.STONE_SHOVEL, true),
                new MinionTier(4, 12, 384,
                        "44485d90a129ff672d9287af7bf47f8ece94abeb496bda38366330893aa69464",
                        Material.STONE_SHOVEL, true),
                new MinionTier(5, 11, 384,
                        "9da9d3bfa431206ab33e62f8815e4092dae6e8fc9f04b9a005a205061ea895a8",
                        Material.STONE_SHOVEL, true),
                new MinionTier(6, 11, 576,
                        "7c53e9ef4aba3a41fe8e03c43e6a310eec022d1089fd9a92f3af8ed8eed4ec03",
                        Material.IRON_SHOVEL, true),
                new MinionTier(7, 9, 576,
                        "e1fd2b30f2ef93785404cf4ca42e6f28755e2935cd3cae910121bfa4327345c1",
                        Material.IRON_SHOVEL, true),
                new MinionTier(8, 9, 768,
                        "9f53221b1b2e40a97a7a10fb47710e61bdd84e15052dd817da2f89783248375e",
                        Material.IRON_SHOVEL, true),
                new MinionTier(9, 8, 768,
                        "caa370beebe77ced5ba4d106591d523640f57e7c46a4cecec60a4fe0ebac4a4c",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(10, 8, 960,
                        "f2c498b33325cce5668a3395a262046412cfd4844b8d86ddaeb9c84e940e2af",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(11, 6, 960,
                        "bce70b1b8e30e90a5ad951f42ff469c19dd416cedf98d5aa4178ec953c584796",
                        Material.DIAMOND_SHOVEL, true),
                new MinionTier(12, 5, 960,
                        "5799a16bdbb0793e8042f1838bae536594ecaef0d5556462eb9396db5f4059ac",
                        Material.DIAMOND_SHOVEL, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(255,255,255);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(255,255,255);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(255,255,255);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.SNOW_BLOCK, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.SNOW_BLOCK);
    }
}
