package net.swofty.types.generic.minion.minions.mining;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionObsidian extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of(
                new MinionTier(1, 45, 64,
                        "8799e3e68599902e4d2a1f6a83e851ced923b417cbef616f4b20f521d73cc0bd",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(2, 45, 192,
                        "2e23904f297e695b34b7228f7261cb0e92f3997f256e54215a0c7c43deab6a4",
                        Material.WOODEN_PICKAXE, true),
                new MinionTier(3, 42, 192,
                        "941fe0587c906c7a90ff8f33878372415f7e97343d42f5ad2a694e74f1743cb5",
                        Material.STONE_PICKAXE, true),
                new MinionTier(4, 42, 384,
                        "a3b6894b171c08030ee1912111f56145354bd5bb4c84e87532fbf90a58c1152c",
                        Material.STONE_PICKAXE, true),
                new MinionTier(5, 39, 384,
                        "5602c201ca0cf8fee876ff8c35bb7b751b90d0f35693161e56882bfc3aa8e00a",
                        Material.STONE_PICKAXE, true),
                new MinionTier(6, 39, 576,
                        "df02ea2296a5aa300baf68940cd2be784aa88a6f4c3874c156dbd0c2f35dff7c",
                        Material.IRON_PICKAXE, true),
                new MinionTier(7, 35, 576,
                        "4b1931187d69fe9149a288890939a38c9164aed51923097a97ea1680e2304fe9",
                        Material.IRON_PICKAXE, true),
                new MinionTier(8, 35, 768,
                        "e719664dca20535088ecaf0093d1fde4a1ffce9ff17195b708cdb26c5313e6a9=",
                        Material.IRON_PICKAXE, true),
                new MinionTier(9, 30, 768,
                        "87df3b9e30fa91891c5f26593349a0f8b8f3a132f6cb7c17e3de90ff89c3dd15",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(10, 30, 960,
                        "ce956c2dcc64c25b8ca9262c875fe41a00700072c04ba10d4de57f9d647c8fe2",
                        Material.GOLDEN_PICKAXE, true),
                new MinionTier(11, 24, 960,
                        "6683ed5cc2a062c4924d3eda50c336c64a9a9868ef9c2d7624e74aab01ecf6ed",
                        Material.DIAMOND_PICKAXE, true),
                new MinionTier(12, 21, 960,
                        "d80c2415787ef06c5aee6df1d60b91283cbf02dc22c48a7cd28e652b45f21d32",
                        Material.DIAMOND_PICKAXE, true)
        );
    }
    //hm

    @Override
    public Color getBootColour() {
        return new Color(25, 25, 25); // Obsidian minion's boot color
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(25, 25, 25); // Obsidian minion's leggings color
    }

    @Override
    public Color getChestplateColour() {
        return new Color(25, 25, 25); // Obsidian minion's chestplate color
    }


    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.OBSIDIAN, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.OBSIDIAN);
    }
}
