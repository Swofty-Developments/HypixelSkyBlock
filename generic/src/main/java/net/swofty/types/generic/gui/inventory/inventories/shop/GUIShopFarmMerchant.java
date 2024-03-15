package net.swofty.types.generic.gui.inventory.inventories.shop;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIShopFarmMerchant extends SkyBlockShopGUI {
    public GUIShopFarmMerchant() {
        super("Farm Merchant", 1);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.WHEAT), 3, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CARROT), 3, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.POTATO), 3, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.MELON_SLICE), 10, new CoinShopPrice(4), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SUGAR_CANE), 3, new CoinShopPrice(10), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PUMPKIN), 1, new CoinShopPrice(25), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COCOA_BEANS), 1, new CoinShopPrice(5), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RED_MUSHROOM), 1, new CoinShopPrice(25), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BROWN_MUSHROOM), 1, new CoinShopPrice(25), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SAND), 2, new CoinShopPrice(4), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CACTUS), 1, new CoinShopPrice(15), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BONE_MEAL), 3, new CoinShopPrice(2), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ROOKIE_HOE), 1, new CoinShopPrice(10), 1));

    }
}
