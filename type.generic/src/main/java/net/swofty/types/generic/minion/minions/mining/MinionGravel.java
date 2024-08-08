package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionGravel extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 26, 128,
                        "7458507ed31cf9a38986ac8795173c609637f03da653f30483a721d3fbe602d",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(2, 26, 256,
                        "fb48c89157ae36038bbd9c88054ef8797f5b6f38631c1b57e58dcb8d701fa61d",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(3, 24, 256,
                        "aae230c0ded51aa97c7964db885786f0c77f6244539b185ef4a5f2554199c785",
                        Material.STONE_SHOVEL, true),
                new MinionTier(4, 24, 384,
                        "ef5b6973f41305d2b41aa82b94ef3b95e05e943e4cd4f793ca59278c46cbb985",
                        Material.STONE_SHOVEL, true),
                new MinionTier(5, 22, 384,
                        "c5961d126cda263759e43940c5665e9f1487ac2c7e26f903e5086affb3785714",
                        Material.STONE_SHOVEL, true),
                new MinionTier(6, 22, 576,
                        "69c5f0583967589650b0de2c5108811ff01c32ac9861a820bba650f0412126d6",
                        Material.IRON_SHOVEL, true),
                new MinionTier(7, 19, 576,
                        "d092f7535b5d091cc3d3f0a343be5d46f16466ae9344b0cac452f3435f00996a",
                        Material.IRON_SHOVEL, true),
                new MinionTier(8, 19, 768,
                        "7117a2f4cf83c41a8dfb9c7a8238ca06bbdb5540a1e91e8721df5476b70f6e74",
                        Material.IRON_SHOVEL, true),
                new MinionTier(9, 16, 768,
                        "14463534f9fbf4590d9e2dcc1067231ccb8d7f641ee56f4652a17f5027f62c63",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(10, 16, 960,
                        "5c6e62f2366d42596c752925c7799c63edbfc226fffd9327ce7780b24c3abd11",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(11, 13, 960,
                        "3945c30d258d68576f061c162b7d50ca8a1f07e41d557e42723dbd4fcce5d594",
                        Material.DIAMOND_SHOVEL, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(0, 0, 0);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(0, 0, 0);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(0, 0, 0);
    }



    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.GRAVEL, Block.AIR)
        );
    }
    //hm

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.GRAVEL);
    }
}
