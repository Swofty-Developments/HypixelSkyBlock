package net.swofty.type.hub.gui;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.gui.ShopView;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.shop.type.CoinShopPrice;

public class GUIShopZog extends ShopView {
    public GUIShopZog() {
        super("Zog", DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ALL_SKILLS_EXP_BOOST), 1, new CoinShopPrice(50000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MINING_EXP_BOOST_COMMON), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.FARMING_EXP_BOOST_COMMON), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.FISHING_EXP_BOOST), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.COMBAT_EXP_BOOST), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.FORAGING_EXP_BOOST), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.MINING_EXP_BOOST_RARE), 1, new CoinShopPrice(500000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.FARMING_EXP_BOOST_RARE), 1, new CoinShopPrice(500000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.BIG_TEETH), 1, new CoinShopPrice(750000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.IRON_CLAWS), 1, new CoinShopPrice(750000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.HARDENED_SCALES), 1, new CoinShopPrice(1000000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SHARPENED_CLAWS), 1, new CoinShopPrice(1000000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.BUBBLEGUM), 1, new CoinShopPrice(5000000)));
    }
}
