package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.UpgradeableItemTier;
import net.swofty.type.bedwarsgame.shop.UpgradeableShopItem;

import java.util.List;

public class AxeShopItem extends UpgradeableShopItem {

    public static final Tag<Integer> AXE_UPGRADE_TAG = Tag.Integer("axe_upgrade");

    public AxeShopItem() {
        super("Upgradeable Axe", "Permanently upgrade your axe.",
                List.of(
                        new UpgradeableItemTier("Wooden Axe", 10, Currency.IRON, Material.WOODEN_AXE),
                        new UpgradeableItemTier("Stone Axe", 10, Currency.IRON, Material.STONE_AXE),
                        new UpgradeableItemTier("Golden Axe", 3, Currency.GOLD, Material.GOLDEN_AXE),
                        new UpgradeableItemTier("Diamond Axe", 6, Currency.GOLD, Material.DIAMOND_AXE)
                ),
                AXE_UPGRADE_TAG
        );
    }

    @Override
    public boolean isSameType(Material material) {
        return material == Material.WOODEN_AXE ||
               material == Material.STONE_AXE ||
               material == Material.GOLDEN_AXE ||
               material == Material.DIAMOND_AXE;
    }
}

