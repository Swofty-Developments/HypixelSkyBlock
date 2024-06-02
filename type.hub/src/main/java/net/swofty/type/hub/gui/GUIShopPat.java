package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

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
