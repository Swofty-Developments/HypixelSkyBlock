package net.swofty.type.village.gui;

import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.type.CoinShopPrice;

public class GUIShopWeaponsmith extends SkyBlockShopGUI{

    public GUIShopWeaponsmith() {
        super("Weaponsmith", 1, DEFAULT);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.UNDEAD_SWORD), 1, new CoinShopPrice(100), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.END_SWORD), 1, new CoinShopPrice(150), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SPIDER_SWORD), 1, new CoinShopPrice(100), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.DIAMOND_SWORD), 1, new CoinShopPrice(60), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.BOW), 1, new CoinShopPrice(25), 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ARROW), 1, new CoinShopPrice(3), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.WITHER_BOW), 1, new CoinShopPrice(250), 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ARTISANAL_SHORTBOW), 1, new CoinShopPrice(600), 1));
    }
}
