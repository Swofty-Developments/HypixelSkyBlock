package net.swofty.type.deepcaverns.gui;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopWalter extends ShopView {
	public GUIShopWalter() {
		super("Walter", SINGLE_SLOT);
	}

	@Override
	public void initializeShopItems() {
		attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.SUPERBOOM_TNT), 1, new CoinShopPrice(2500)));
	}
}
