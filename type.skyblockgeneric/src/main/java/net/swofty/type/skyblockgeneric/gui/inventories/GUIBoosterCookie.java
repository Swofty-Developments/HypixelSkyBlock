package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIBoosterCookie extends HypixelInventoryGUI {
    public GUIBoosterCookie() {
        super("Consume Booster Cookie?", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(new GUIClickableItem(15) {

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                return ItemStackCreator.getStack("§cCancel", Material.RED_CONCRETE,1,
                        "§7I'm not hungry...");
            }
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                player.closeInventory();
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                long time;
                if (player.getBoosterCookieExpirationDate() - System.currentTimeMillis() > System.currentTimeMillis()) {
                    time = player.getBoosterCookieExpirationDate() - System.currentTimeMillis() + System.currentTimeMillis();
                } else {
                    time = System.currentTimeMillis();
                }
                time += 345600000; //4d
                player.setBoosterCookieExpirationDate(time);
                player.addBits(4000);
                player.setItemInMainHand(ItemStack.AIR);
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                return ItemStackCreator.getStack("§eConsume Cookie", Material.COOKIE,1,
                        "§7Gain the §dCookie Buff§!",
                        " ",
                        "§7Duration: §b4 days§!",
                        " ",
                        "§7You will be able to gain",
                        "§b4,000 Bits §7from this",
                        "§7cookie."
                );
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {}

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
