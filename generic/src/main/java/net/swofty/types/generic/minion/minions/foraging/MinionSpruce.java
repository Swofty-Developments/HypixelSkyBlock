package net.swofty.types.generic.minion.minions.foraging;

import net.minestom.server.color.Color;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.minion.actions.MinionMineAction;

import java.util.List;

public class MinionSpruce extends SkyBlockMinion {
    @Override
    public List<MinionTier> getTiers() {
        return List.of( //skins done
                new MinionTier(1, 48, 64,
                        "7ba04bfe516955fd43932dcb33bd5eac20b38a231d9fa8415b3fb301f60f7363",
                        Material.WOODEN_AXE, true),
                new MinionTier(2, 48, 192,
                        "3cc4e6fa46cd52a6480dc2eac053e9ac8a7d6ee0ee9c9cf74e176b289a43eb3a",
                        Material.WOODEN_AXE, true),
                new MinionTier(3, 45, 192,
                        "b2d2366357a435a230fbbdd55929c23dd4985a8978020102255b7a007476fa56",
                        Material.STONE_AXE, true),
                new MinionTier(4, 45, 384,
                        "2c188216e275281e49e64a32b787463dff849e3f6f05ae307f4b21f68be28232",
                        Material.STONE_AXE, true),
                new MinionTier(5, 42, 384,
                        "bdb2fcbf4be4a110b814d93fe8093ba66badabb6d65c58846a731935fa0228f0",
                        Material.STONE_AXE, true),
                new MinionTier(6, 42, 576,
                        "5b2efe8fe599598326b4941c2ff55c284ce26b0948b520c0490de8b0d9aeff4a",
                        Material.IRON_AXE, true),
                new MinionTier(7, 38, 576,
                        "e1b1af499ef6a63dc5b111e955c3ad7b4647841135df7953c1d441955540a6a4",
                        Material.IRON_AXE, true),
                new MinionTier(8, 38, 768,
                        "ed3f7f42298490fcf71e27a7b4c5ed5f2c556c58c97fd0f2e3460488d32938c7",
                        Material.IRON_AXE, true),
                new MinionTier(9, 33, 768,
                        "999cbe069cd5fc2368e41c9dd073d1aedaa8e5465276d4b8852ac5a917bbdda8",
                        Material.GOLDEN_AXE, true),
                new MinionTier(10, 33, 960,
                        "74ba98e2b81e9426e5f1f44b63559633b3b2ab416a72cbc3b6cb4d527aaad8cd",
                        Material.GOLDEN_AXE, true),
                new MinionTier(11, 27, 960,
                        "da54f11da358d14fa11e2c32eb1b93d9444eabcd600e32cc0ab462172a1f12c",
                        Material.DIAMOND_AXE, true)
        );
    }

    @Override
    public Color getBootColour() {
        return new Color(255,155,0);
    }

    @Override
    public Color getLeggingsColour() {
        return new Color(255,255,0);
    }

    @Override
    public Color getChestplateColour() {
        return new Color(255,255,0);
    }

    @Override
    public List<MinionExpectations> getExpectations() {
        return List.of(
                new MinionExpectations(-1, Block.SPRUCE_LOG, Block.AIR)
        );
    }

    @Override
    public MinionAction getAction() {
        return new MinionMineAction(Block.SPRUCE_LOG);
    }
}
