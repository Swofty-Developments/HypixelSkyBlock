package net.swofty.type.hub.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUILonelyPhilosopher extends HypixelInventoryGUI {

    public GUILonelyPhilosopher() {
        super("Lonely Philosopher", InventoryType.CHEST_6_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                double coins = player.getCoins();
                double price = 150000;
                if (coins < price) {
                    player.sendMessage("§cYou don't have enough coins!");
                    return;
                }
                player.addAndUpdateItem(ItemType.HUB_CASTLE_TRAVEL_SCROLL);
                player.playSuccessSound();
                player.removeCoins(price);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                return ItemStackCreator.getStack("§5Travel Scroll To Hub Castle", Material.MAP, 1,
                        "§7Consume this item to add its",
                        "§7destination to your fast travel",
                        "§7options.",
                        "",
                        "§7Requires §bMVP§c+ §7to consume!",
                        "",
                        "§7Island: §aHub",
                        "§7Teleport: §eCastle",
                        "",
                        "§5§lEPIC TRAVEL SCROLL",
                        "",
                        "§7Cost",
                        "§6150,000 Coins");
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }

}
