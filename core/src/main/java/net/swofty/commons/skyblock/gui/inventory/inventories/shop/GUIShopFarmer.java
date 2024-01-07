package net.swofty.commons.skyblock.gui.inventory.inventories.shop;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.gui.inventory.SkyBlockShopGUI;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.SkyBlockItem;

public class GUIShopFarmer extends SkyBlockShopGUI {
    public GUIShopFarmer() {
        super("Farm Merchant", 1);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.WHEAT), 3, 10, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CARROT), 3, 10, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.POTATO), 3, 10, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.MELON_SLICE), 10, 4, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SUGAR_CANE), 3, 10, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PUMPKIN), 1, 25, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COCOA_BEANS), 1, 5, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RED_MUSHROOM), 1, 25, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BROWN_MUSHROOM), 1, 25, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SAND), 2, 4, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.CACTUS), 1, 15, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BONE_MEAL), 3, 2, 1));

        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ROOKIE_HOE), 1, 10, 1));
    }
}
