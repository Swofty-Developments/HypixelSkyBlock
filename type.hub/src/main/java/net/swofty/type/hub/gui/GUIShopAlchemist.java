package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopAlchemist extends ShopView {
    public GUIShopAlchemist() {
        super("Alchemist", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.NETHER_WART), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BREWING_STAND), 1, new CoinShopPrice(30)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.GLASS_BOTTLE), 8, new CoinShopPrice(48)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.WATER_BOTTLE), 1, new CoinShopPrice(6)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SUGAR), 1, new CoinShopPrice(4)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RABBIT_FOOT), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.GLISTERING_MELON), 1, new CoinShopPrice(10)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPIDER_EYE), 1, new CoinShopPrice(12)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BLAZE_POWDER), 1, new CoinShopPrice(12)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GHAST_TEAR), 1, new CoinShopPrice(200)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.MAGMA_CREAM), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GOLDEN_CARROT), 1, new CoinShopPrice(15)));
    }
}
