package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;

public class GUISpecialBazaar extends HypixelInventoryGUI {
    public GUISpecialBazaar() {
        super("Special Bazaar", InventoryType.CHEST_3_ROW);
        fill(ItemStackCreator.createNamedItemStack(Material.YELLOW_STAINED_GLASS_PANE));

        set(new GUIItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§7♲ §7Ironman", Material.IRON_CHESTPLATE, 1,
                        "§8Special Mode", "", "§7Ironman profiles can only", "§7purchase Booster Cookies here.");
            }
        });
        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                new GUIBazaarItem(ItemType.BOOSTER_COOKIE, true).open(p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return new NonPlayerItemUpdater(new SkyBlockItem(ItemType.BOOSTER_COOKIE)).getUpdatedItem();
            }
        });
        set(new GUIItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§6Booster Cookie", Material.COOKIE, 1,
                        "§8Commodity", "", "§7The only Bazaar product available", "§7on a special profile.", "", "§eClick the cookie to buy!");
            }
        });
        set(GUIClickableItem.getCloseItem(22));
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
