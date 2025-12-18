package net.swofty.type.deepcaverns.gui;

import net.swofty.commons.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.SkyBlockShopGUI;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopWalter extends SkyBlockShopGUI {
	public GUIShopWalter() {
		super("Walter", 1, SINGLE_SLOT);
	}

	@Override
	public void initializeShopItems() {
		attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.SUPERBOOM_TNT), 1, new CoinShopPrice(2500)));
	}
}
