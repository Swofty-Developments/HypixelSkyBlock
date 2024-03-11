package net.swofty.types.generic.gui.inventory.inventories.shop;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIShopFishMerchant extends SkyBlockShopGUI{
    public GUIShopFishMerchant() {
        super("Fish Merchant", 1);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.FISHING_ROD), 1, 100, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COD), 1, 20, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SALMON), 1, 30, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TROPICAL_FISH), 1, 100, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.PUFFERFISH), 1, 40, 1));
    }
}
