package net.swofty.type.skyblockgeneric.gui.inventories.shop;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopAlda extends ShopView {
    public GUIShopAlda() {
        super(I18n.t("gui_shop.alda.title"), SINGLE_SLOT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ABIPHONE_BASIC), 1, new CoinShopPrice(100000)));
    }
}
