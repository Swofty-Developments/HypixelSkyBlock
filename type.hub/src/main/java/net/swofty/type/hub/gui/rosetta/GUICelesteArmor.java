package net.swofty.type.hub.gui.rosetta;

import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.gui.SkyBlockShopGUI;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.shop.type.CoinShopPrice;

public class GUICelesteArmor extends SkyBlockShopGUI {
    public GUICelesteArmor() {
        super("Celeste Armor", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.CELESTE_HELMET), 1, new CoinShopPrice(5000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.CELESTE_CHESTPLATE), 1, new CoinShopPrice(8000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.CELESTE_LEGGINGS), 1, new CoinShopPrice(7000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.CELESTE_BOOTS), 1, new CoinShopPrice(4000)));
    }
}
