package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.UpgradeableItemTier;
import net.swofty.type.bedwarsgame.shop.UpgradeableShopItem;

import java.util.List;

public class PickaxeShopItem extends UpgradeableShopItem {

    public static final Tag<Integer> PICKAXE_UPGRADE_TAG = Tag.Integer("pickaxe_upgrade");

    public PickaxeShopItem() {
        super("Upgradeable Pickaxe", "Permanently upgrade your pickaxe.",
                List.of(
                        new UpgradeableItemTier("Wooden Pickaxe", 10, Currency.IRON, Material.WOODEN_PICKAXE),
                        new UpgradeableItemTier("Stone Pickaxe", 10, Currency.IRON, Material.STONE_PICKAXE),
                        new UpgradeableItemTier("Golden Pickaxe", 3, Currency.GOLD, Material.GOLDEN_PICKAXE),
                        new UpgradeableItemTier("Diamond Pickaxe", 6, Currency.GOLD, Material.DIAMOND_PICKAXE)
                ),
                PICKAXE_UPGRADE_TAG
        );
    }

    @Override
    public boolean isSameType(Material material) {
        return material == Material.WOODEN_PICKAXE ||
               material == Material.STONE_PICKAXE ||
               material == Material.GOLDEN_PICKAXE ||
               material == Material.DIAMOND_PICKAXE;
    }
}

