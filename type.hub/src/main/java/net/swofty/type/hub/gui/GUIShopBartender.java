package net.swofty.type.hub.gui;

import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopBartender extends SkyBlockShopGUI {

    public GUIShopBartender() {
        super("Bartender", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.CHEAP_COFFEE), 1, new CoinShopPrice(1000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.TEPID_GREEN_TEA), 1, new CoinShopPrice(1000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.PULPOUS_ORANGE_JUICE), 1, new CoinShopPrice(1000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.BITTER_ICE_TEA), 1, new CoinShopPrice(1200)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.KNOCKOFF_COLA), 1, new CoinShopPrice(1500)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.DECENT_COFFEE), 1, new CoinShopPrice(5000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.ZOMBIE_BRAIN_MIXIN), 1, new CoinShopPrice(150000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.SPIDER_EGG_MIXIN), 1, new CoinShopPrice(150000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.WOLF_FUR_MIXIN), 1, new CoinShopPrice(150000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.END_PORTAL_FUMES), 1, new CoinShopPrice(150000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.GABAGOEY_MIXIN), 1, new CoinShopPrice(150000)));
    }
}
