package net.swofty.type.hub.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopWeaponsmith extends SkyBlockShopGUI{

    public GUIShopWeaponsmith() {
        super("Weaponsmith", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.UNDEAD_SWORD), 1, new CoinShopPrice(100)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.END_SWORD), 1, new CoinShopPrice(150)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.SPIDER_SWORD), 1, new CoinShopPrice(100)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.DIAMOND_SWORD), 1, new CoinShopPrice(60)));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.BOW), 1, new CoinShopPrice(25)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemTypeLinker.FLINT_ARROW), 1, new CoinShopPrice(3)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.WITHER_BOW), 1, new CoinShopPrice(250)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemTypeLinker.ARTISANAL_SHORTBOW), 1, new CoinShopPrice(600)));
    }
}
