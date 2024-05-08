package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopFishMerchant extends SkyBlockShopGUI{
    public GUIShopFishMerchant() {
        super("Fish Merchant", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.FISHING_ROD), 1, new CoinShopPrice(100)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COD), 1, new CoinShopPrice(20)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SALMON), 1, new CoinShopPrice(30)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TROPICAL_FISH), 1, new CoinShopPrice(100)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PUFFERFISH), 1, new CoinShopPrice(40)));

    }
}
