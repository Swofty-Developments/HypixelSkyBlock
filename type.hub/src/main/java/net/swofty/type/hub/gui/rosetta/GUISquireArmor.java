package net.swofty.type.hub.gui.rosetta;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUISquireArmor extends ShopView {
    public GUISquireArmor() {
        super("Squire Armor", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_SWORD), 1, new CoinShopPrice(5000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_HELMET), 1, new CoinShopPrice(5000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_CHESTPLATE), 1, new CoinShopPrice(8000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_LEGGINGS), 1, new CoinShopPrice(7000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SQUIRE_BOOTS), 1, new CoinShopPrice(4000)));
    }
}
