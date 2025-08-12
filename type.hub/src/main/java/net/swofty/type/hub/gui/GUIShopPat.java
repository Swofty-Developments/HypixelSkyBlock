package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.SkyBlockShopGUI;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.shop.type.CoinShopPrice;

public class GUIShopPat extends SkyBlockShopGUI {
    public GUIShopPat() {
        super("Pat", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.FLINT), 10, new CoinShopPrice(60)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAVEL), 15, new CoinShopPrice(65)));
    }
}
