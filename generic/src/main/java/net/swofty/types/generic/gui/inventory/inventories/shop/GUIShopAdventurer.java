package net.swofty.types.generic.gui.inventory.inventories.shop;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.SkyBlockShopGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIShopAdventurer extends SkyBlockShopGUI {
    public GUIShopAdventurer() {
        super("Adventurer", 1);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.ROTTEN_FLESH), 1, 8, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.BONE), 1, 8, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.STRING), 10, 8, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.SLIME_BALL), 1, 14, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.GUNPOWDER), 10, 8, 1));

        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.ZOMBIE_TALISMAN), 500, 8, 1));
    }
}
