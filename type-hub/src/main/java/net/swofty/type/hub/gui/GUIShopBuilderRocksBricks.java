package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopBuilderRocksBricks extends SkyBlockShopGUI {
    public GUIShopBuilderRocksBricks() {
        super("Rocks & Bricks", 1, UPPER5ROWS);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STONE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GRANITE), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.POLISHED_GRANITE), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DIORITE), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.POLISHED_DIORITE), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ANDESITE), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.POLISHED_ANDESITE), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAVEL), 1, new CoinShopPrice(32)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.OBSIDIAN), 1, new CoinShopPrice(99)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SMOOTH_STONE_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COBBLESTONE_STAIRS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COBBLESTONE_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COBBLESTONE), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COBBLESTONE_WALL), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.MOSSY_COBBLESTONE), 1, new CoinShopPrice(12)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.MOSSY_COBBLESTONE_WALL), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.DIRT), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COARSE_DIRT), 1, new CoinShopPrice(2)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STONE_BRICKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STONE_BRICK_STAIRS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STONE_BRICK_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CRACKED_STONE_BRICKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CHISELED_STONE_BRICKS), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.MOSSY_STONE_BRICKS), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GRASS_BLOCK), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PODZOL), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SANDSTONE), 1, new CoinShopPrice(6)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SANDSTONE_STAIRS), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SANDSTONE_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SMOOTH_SANDSTONE), 1, new CoinShopPrice(6)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CHISELED_SANDSTONE), 1, new CoinShopPrice(6)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SAND), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BRICKS), 1, new CoinShopPrice(6)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BRICK_STAIRS), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BRICK_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RED_SANDSTONE), 1, new CoinShopPrice(6)));

        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RED_SANDSTONE_STAIRS), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RED_SANDSTONE_SLAB), 1, new CoinShopPrice(2)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SMOOTH_RED_SANDSTONE), 1, new CoinShopPrice(6)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CHISELED_RED_SANDSTONE), 1, new CoinShopPrice(6)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.IRON_DOOR), 1, new CoinShopPrice(36)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.IRON_TRAPDOOR), 1, new CoinShopPrice(24)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TERRACOTTA), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ICE), 1, new CoinShopPrice(1)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PACKED_ICE), 1, new CoinShopPrice(9)));
    }
}
