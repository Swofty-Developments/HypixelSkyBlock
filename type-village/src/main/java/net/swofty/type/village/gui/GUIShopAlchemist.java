package net.swofty.type.village.gui;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIShopAlchemist extends SkyBlockShopGUI{
    public GUIShopAlchemist() {
        super("Alchemist", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.NETHER_WART), 1, new CoinShopPrice(30), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BREWING_STAND), 1, new CoinShopPrice(30), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GLASS_BOTTLE), 8, new CoinShopPrice(48), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.WATER_BOTTLE), 1, new CoinShopPrice(6), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SUGAR), 1, new CoinShopPrice(4), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RABBIT_FOOT), 1, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GLISTERING_MELON_SLICE), 1, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPIDER_EYE), 1, new CoinShopPrice(12), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BLAZE_POWDER), 1, new CoinShopPrice(12), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GHAST_TEAR), 1, new CoinShopPrice(200), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.MAGMA_CREAM), 1, new CoinShopPrice(20), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GOLDEN_CARROT), 1, new CoinShopPrice(7), 1));
    }
}
