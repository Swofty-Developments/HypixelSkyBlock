package net.swofty.types.generic.gui.inventory.inventories.shop;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIShopAlchemist extends SkyBlockShopGUI{
    public GUIShopAlchemist() {
        super("Alchemist", 1);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.NETHER_WART), 1, 30, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BREWING_STAND), 1, 30, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GLASS_BOTTLE), 8, 48, 1));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.WATER_BOTTLE), 1, 6, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SUGAR), 1, 4, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.RABBIT_FOOT), 1, 10, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GLISTERING_MELON_SLICE), 1, 10, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SPIDER_EYE), 1, 12, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BLAZE_POWDER), 1, 12, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GHAST_TEAR), 1, 200, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.MAGMA_CREAM), 1, 20, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GOLDEN_CARROT), 1, 7, 1));
    }
}
