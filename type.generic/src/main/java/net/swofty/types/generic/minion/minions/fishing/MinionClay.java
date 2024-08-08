package net.swofty.types.generic.minion.minions.fishing;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionClay extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 32, 64,
                        "af9b312c8f53da289060e6452855072e07971458abbf338ddec351e16c171ff8",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(2, 32, 192,
                        "7411bd08421fccfea5077320a5cd2e4eecd285c86fc9d2687abb795ef119097f",
                        Material.WOODEN_SHOVEL, true),
                new MinionTier(3, 30, 192,
                        "fd4ffcb5df4ef82fc07bc7585474c97fc0f2bf59022ffd6c2606b4675f8aaa42",
                        Material.STONE_SHOVEL, true),
                new MinionTier(4, 30, 384,
                        "fb2cfdad77fb027ede854bcd14ee5c0b4133aa25bf4c444789782c89acd00593",
                        Material.STONE_SHOVEL, true),
                new MinionTier(5, 27.5f, 384,
                        "393452da603462cce47dda35da160316291d8d8e6db8f377f5df71971242f3d1",
                        Material.STONE_SHOVEL, true),
                new MinionTier(6, 27.5f, 576,
                        "23974725dd17729fc5f751a6749e02c8fa3d9299d890c9164225b1fbb7280329",
                        Material.IRON_SHOVEL, true),
                new MinionTier(7, 24, 576,
                        "94a6fbf682862d7f0b68c192521e455122bb8f3a9b7ba876294027b7a35cd1a7",
                        Material.IRON_SHOVEL, true),
                new MinionTier(8, 24, 768,
                        "f0ec6c510e8c72627efd3011bb3dcf5ad33d6b6162fa7fcbd46d661db02b2e68",
                        Material.IRON_SHOVEL, true),
                new MinionTier(9, 20, 768,
                        "c7de1140a2d1ce558dffb2f69666dc9145aa8166f1528a259013d2aa49c949a8",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(10, 20, 960,
                        "b1655ad07817ef1e71c9b82024648340f0e46a3254857e1c7fee2a1eb2eaab41",
                        Material.GOLDEN_SHOVEL, true),
                new MinionTier(11, 16, 960,
                        "8428bb198b27ac6656698cb3081c2ba94c8cee2f33d16e8e9e11e82a4c1763c6",
                        Material.DIAMOND_SHOVEL, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(160,134,56);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(160,134,56);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(160,134,56);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.CLAY, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.CLAY);
    }
}
