package net.swofty.commons.skyblock.gui.inventory.inventories;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointDouble;
import net.swofty.commons.skyblock.gui.inventory.ItemStackCreator;
import net.swofty.commons.skyblock.gui.inventory.RefreshingGUI;
import net.swofty.commons.skyblock.gui.inventory.item.GUIClickableItem;
import net.swofty.commons.skyblock.gui.inventory.item.GUIQueryItem;
import net.swofty.commons.skyblock.utility.StringUtility;

public class GUIBanker extends SkyBlockInventoryGUI implements RefreshingGUI {
    public GUIBanker() {
        super("Personal Bank Account", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        refreshItems(e.player());
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(31));

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cThis feature is currently unavailable.");
                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(
                        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue() + 1000
                );
            }

            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aDeposit Coins", Material.CHEST, (short) 0, 1,
                        "§7Current balance: §6" + StringUtility.commaify(
                                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue()),
                        " ",
                        "§7Store coins in the bank to keep",
                        "§7them safe while you go on",
                        "§7on adventures!",
                        " ",
                        "§7You will earn §b2%§7 interest every",
                        "§7season for your first §6" + "10 million",
                        "§7banked coins.",
                        " ",
                        "§7Until interest: §cUnavailable",
                        " ",
                        "§eClick to make a deposit!");
            }
        });

        set(new GUIQueryItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("Poggers");
            }

            @Override
            public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                player.sendMessage("§aYoza how is the §c" + query + "§a going?");
                return new GUIBanker();
            }

            @Override
            public int getSlot() {
                return 0;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.BARRIER, "§aPOGPOGOPG");
            }
        });
    }

    @Override
    public int refreshRate() {
        return 20;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        System.out.println("Closed for reason " + reason.name());
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
