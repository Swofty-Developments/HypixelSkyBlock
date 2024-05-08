package net.swofty.type.hub.gui.rosetta;

import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIMercenaryArmor extends SkyBlockShopGUI {
    public GUIMercenaryArmor() {
        super("Mercenary Armor", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_AXE), 1, new CoinShopPrice(30000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_HELMET), 1, new CoinShopPrice(35000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_CHESTPLATE), 1, new CoinShopPrice(70000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_LEGGINGS), 1, new CoinShopPrice(45000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MERCENARY_BOOTS), 1, new CoinShopPrice(30000)));
    }
}
