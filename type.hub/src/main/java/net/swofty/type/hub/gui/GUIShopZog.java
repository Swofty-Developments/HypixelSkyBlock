package net.swofty.type.hub.gui;

import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopZog extends SkyBlockShopGUI {
    public GUIShopZog() {
        super("Zog", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.ALL_SKILLS_EXP_BOOST), 1, new CoinShopPrice(50000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.MINING_EXP_BOOST_COMMON), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.FARMING_EXP_BOOST_COMMON), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.FISHING_EXP_BOOST), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.COMBAT_EXP_BOOST), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.FORAGING_EXP_BOOST), 1, new CoinShopPrice(60000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.MINING_EXP_BOOST_RARE), 1, new CoinShopPrice(500000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.FARMING_EXP_BOOST_RARE), 1, new CoinShopPrice(500000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.BIG_TEETH), 1, new CoinShopPrice(750000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.IRON_CLAWS), 1, new CoinShopPrice(750000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.HARDENED_SCALES), 1, new CoinShopPrice(1000000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.SHARPENED_CLAWS), 1, new CoinShopPrice(1000000)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.BUBBLEGUM), 1, new CoinShopPrice(5000000)));
    }
}
