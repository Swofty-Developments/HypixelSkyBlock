package net.swofty.type.hub.gui;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;


public class GUIBakerShop extends ShopView {

    public GUIBakerShop() {
        super("Baker", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.NEW_YEAR_CAKE_BAG), 1, new CoinShopPrice(250_000)));
    }
}

