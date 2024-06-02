package net.swofty.type.island.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIJerry extends SkyBlockInventoryGUI {
    public GUIJerry() {
        super("Jerry The Assistant", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIPatchNotes().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPatch Notes", Material.BOOK, (short) 0, 1,
                        "§7View the latest features and",
                        "§7changes to the game.",
                        "",
                        "§eClick to open!"
                );
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cNo new deliveries.");
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aDeliveries", Material.ENDER_CHEST, (short) 0, 1,
                        "§7Any items that may be delivered to",
                        "§7yourself or your island will appear",
                        "§7here for collection!",
                        "",
                        "§eClick to open!"
                );
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIGuests().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aVisits and Guestings", Material.EMERALD, (short) 0, 1,
                        "§7Learn all about how to §a/visit",
                        "§7players across the SkyBlock universe!",
                        " ",
                        "§eClick to learn!"
                );
            }
        });

        set(new GUIClickableItem(35) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
                player.sendMessage("§aI have given you an egg, place this where you would like me to move to!");

                player.addAndUpdateItem(ItemType.MOVE_JERRY);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.BEDROCK, "§aMove Jerry");
            }
        });

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
