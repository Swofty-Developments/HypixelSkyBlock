package net.swofty.types.generic.gui.inventory.inventories.shop;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIShopWeaponsmith extends SkyBlockShopGUI{
    public GUIShopWeaponsmith() {
        super("Weaponsmith", 1);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.UNDEAD_SWORD), 1, 100, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.END_SWORD), 1, 150, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SPIDER_SWORD), 1, 100, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.DIAMOND_SWORD), 1, 60, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.BOW), 1, 25, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ARROW), 12, 40, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.WITHER_BOW), 1, 250, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ARTISANAL_SHORTBOW), 1, 600, 1));
    }
}
