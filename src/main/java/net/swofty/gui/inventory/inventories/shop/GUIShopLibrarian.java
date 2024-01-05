package net.swofty.gui.inventory.inventories.shop;

import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.enchantment.EnchantmentType;
import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.gui.inventory.SkyBlockShopGUI;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;

public class GUIShopLibrarian extends SkyBlockShopGUI {
    public GUIShopLibrarian() {
        super("Librarian", 1);
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
        attachItem(ShopItem.Stackable(new SkyBlockItem(Material.EXPERIENCE_BOTTLE), 1, 30, 1));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.BOOK), 1, 20, 1));

        SkyBlockItem sharpness = new SkyBlockItem(ItemType.ENCHANTED_BOOK);
        sharpness.getAttributeHandler().addEnchantment(
                new SkyBlockEnchantment(EnchantmentType.SHARPNESS, 1)
        );
        SkyBlockItem scavenger = new SkyBlockItem(ItemType.ENCHANTED_BOOK);
        scavenger.getAttributeHandler().addEnchantment(
                new SkyBlockEnchantment(EnchantmentType.SCAVENGER, 1)
        );
        SkyBlockItem protection = new SkyBlockItem(ItemType.ENCHANTED_BOOK);
        protection.getAttributeHandler().addEnchantment(
                new SkyBlockEnchantment(EnchantmentType.PROTECTION, 1)
        );
        SkyBlockItem efficiency = new SkyBlockItem(ItemType.ENCHANTED_BOOK);
        efficiency.getAttributeHandler().addEnchantment(
                new SkyBlockEnchantment(EnchantmentType.EFFICIENCY, 1)
        );

        attachItem(ShopItem.Single(sharpness, 1, 30, 1));
        attachItem(ShopItem.Single(scavenger, 1, 40, 1));
        attachItem(ShopItem.Single(protection, 1, 20, 1));
        attachItem(ShopItem.Single(efficiency, 1, 20, 1));
    }
}
