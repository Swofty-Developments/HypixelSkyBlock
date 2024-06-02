package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionCobblestone extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 14, 64,
                        "2f93289a82bd2a06cbbe61b733cfdc1f1bd93c4340f7a90abd9bdda774109071",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 14, 192,
                        "3fd87486dc94cb8cd04a3d7d06f191f027f38dad7b4ed34c6681fb4d08834c06",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 12, 192,
                        "cc088ed6bb8763af4eb7d006e00fda7dc11d7681e97c983b7011c3e872f6aab9",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 12, 384,
                        "39514fee95d702625b974f1730fd62e567c5934997f73bae7e07ab52ddf9066e",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 10, 384,
                        "3e2467b8ccaf007d03a9bb7c22d6a61397ca1bb284f128d5ccd138ad09124e68",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 10, 576,
                        "f4e01f552549037ae8887570700e74db20c6f026a650aeec5d9c8ec51ba3f515",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 9, 576,
                        "51616e63be0ff341f70862e0049812fa0c27b39a2e77058dd8bfc386375e1d16",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 9, 768,
                        "ea53e3c9f446a77e8c59df305a410a8accb751c002a41e55a1018ce1b3114690",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 8, 768,
                        "ccf546584428b5385bc0c1a0031aa87e98e85875e4d6104e1be06cef8bd74fe4",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 8, 960,
                        "989db0a9c97f0e0b5bb9ec7b3e32f8d63c648d4608cfd5be9adbe8825d4e6a94",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 7, 960,
                        "ebcc099f3a00ece0e5c4b31d31c828e52b06348d0a4eac11f3fcbef3c05cb407",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 6, 960,
                        "b88d5a7db53d90488257859ee664539e13a0ff3a29e857de7db88df077479dae",
                        Material.DIAMOND_PICKAXE, false)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(122, 121, 100);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(122, 121, 100);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(122, 121, 100);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.COBBLESTONE, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.COBBLESTONE);
    }
}
