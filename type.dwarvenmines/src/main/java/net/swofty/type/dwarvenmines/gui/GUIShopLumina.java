package net.swofty.type.dwarvenmines.gui;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.SkyBlockShopGUI;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopLumina extends SkyBlockShopGUI {
    public GUIShopLumina() {
        super("Lumina", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.TORCH), 64, new CoinShopPrice(32)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.LANTERN), 1, new CoinShopPrice(10_000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.INFINI_TORCH), 1, new CoinShopPrice(100_000)));
    }
}
