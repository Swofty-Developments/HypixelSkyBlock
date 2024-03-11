package net.swofty.types.generic.gui.inventory.inventories.shop;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIShopMineMerchant extends SkyBlockShopGUI{
    public GUIShopMineMerchant() {
        super("Mine Merchant", 1);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COAL), 2, 8, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.IRON_INGOT), 4, 22, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GOLD_INGOT), 2, 12, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ROOKIE_PICKAXE), 1, 12, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.PROMISING_PICKAXE), 1, 35, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(Material.GOLDEN_PICKAXE), 1, 1000000000, 1)); //costs 3 gold ingots
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.TORCH), 32, 16, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GRAVEL), 2, 12, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.COBBLESTONE), 1, 3, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STONE), 2, 4, 1));
        //attachItem(ShopItem.Single(new SkyBlockItem(Material.ONYX), 1, 100, 1));
    }
}
