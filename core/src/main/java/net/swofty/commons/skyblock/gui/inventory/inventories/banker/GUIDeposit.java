package net.swofty.commons.skyblock.gui.inventory.inventories.banker;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointDouble;
import net.swofty.commons.skyblock.gui.inventory.ItemStackCreator;
import net.swofty.commons.skyblock.gui.inventory.RefreshingGUI;
import net.swofty.commons.skyblock.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.commons.skyblock.gui.inventory.item.GUIClickableItem;
import net.swofty.commons.skyblock.gui.inventory.item.GUIQueryItem;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.utility.StringUtility;

public class GUIDeposit extends SkyBlockInventoryGUI implements RefreshingGUI {

    public GUIDeposit() { super("Bank Deposit", InventoryType.CHEST_4_ROW); }


    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        refreshItems(e.player());
    }


    @Override
    public void refreshItems(SkyBlockPlayer player) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(31));
        DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS , DatapointDouble.class);
        DatapointDouble bankCoins = player.getDataHandler().get(DataHandler.Data.BANK_COINS , DatapointDouble.class);

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
               addCoins(bankCoins , coins.getValue());
               subtractCoins(coins , coins.getValue());
            }

            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§aYour whole purse", Material.CHEST, (short) 0, 64,
                        "§8Bank deposit",
                        " ",
                        "§7Current balance: §6" + StringUtility.commaify(bankCoins.getValue()),
                        "§7Amount to deposit: §6" + StringUtility.commaify(coins.getValue()),
                        " ",
                        "§eClick to deposit coins!"
                );
            }
        });

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                double deposit = coins.getValue() / 2;
                addCoins(bankCoins , deposit);
                subtractCoins(coins , deposit);
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§aHalf your purse", Material.CHEST, (short) 0, 32,
                        "§8Bank deposit",
                        " ",
                        "§7Current balance: §6" + StringUtility.commaify(bankCoins.getValue()),
                        "§7Amount to deposit: §6" + StringUtility.commaify(coins.getValue() / 2),
                        " ",
                        "§eClick to deposit coins!"
                );
            }
        });

        set(new GUIQueryItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {}

            @Override
            public SkyBlockInventoryGUI onQueryFinish(String query, SkyBlockPlayer player) {
                double deposit = 0;
                try{
                    deposit = Double.parseDouble(query);
                }catch (NumberFormatException exception){
                    player.sendMessage("invalid number!");
                    return null;
                }
               if (deposit > coins.getValue()){
                   player.sendMessage("You are broke!");
                   return null;
               }
               addCoins(bankCoins , deposit);
               subtractCoins(coins , deposit);
               return new GUIBanker();
            }

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSpecific amount", Material.OAK_SIGN, (short) 0, 1,
                        "§7Current balance: §6" + StringUtility.commaify(bankCoins.getValue()),
                        " ",
                        "§eClick to deposit coins!"
                );

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

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
    private void addCoins(DatapointDouble datapoint , double value) {
        if (value == 0) return;
        datapoint.setValue(datapoint.getValue() + value);
    }
    private void subtractCoins(DatapointDouble datapointDouble , double value) {
        if (value == 0) return;
        if (value > datapointDouble.getValue()) return;
        datapointDouble.setValue(datapointDouble.getValue() - value);
    }
}
