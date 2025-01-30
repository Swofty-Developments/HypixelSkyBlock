package net.swofty.types.generic.gui.inventory.inventories.banker;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBankData;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class GUIBanker extends SkyBlockAbstractInventory {
    private static final String STATE_BANK_DELAYED = "bank_delayed";

    public GUIBanker() {
        super(InventoryType.CHEST_4_ROW);
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        if (player.isBankDelayed()) {
            player.sendMessage("§cYou currently have processing transactions!");
            player.sendMessage("§cPlease wait a moment before accessing your bank account.");
            player.closeInventory();
            return;
        }

        doAction(new SetTitleAction(Component.text(
                (player.isCoop() ? "Co-op" : "Personal") + " Bank Account"
        )));

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Close button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        setupBankItems(player);
        startLoop("refresh", 20, () -> refreshItems(player));
    }

    private void setupBankItems(SkyBlockPlayer player) {
        DatapointBankData.BankData bankData = player.getDataHandler()
                .get(DataHandler.Data.BANK_DATA, DatapointBankData.class)
                .getValue();

        // Information Item
        attachItem(GUIItem.builder(32)
                .item(() -> ItemStackCreator.getStack("§aInformation", Material.REDSTONE_TORCH, 1,
                        "§7Keep your coins safe in the bank!",
                        "§7You lose half the coins in your purse when dying in combat.",
                        " ",
                        "§7Balance limit: §6" + StringUtility.commaify(bankData.getBalanceLimit()) + " Coins",
                        " ",
                        "§7The banker rewards you every 31",
                        "§7hours with §binterest §7for the coins in your bank balance.",
                        "§7 ",
                        "§7Interest is in: §b" + SkyBlockCalendar.getHoursUntilNextInterest() + "h"
                ).build())
                .build());

        // Deposit Button
        attachItem(GUIItem.builder(11)
                .item(() -> ItemStackCreator.getStack("§aDeposit Coins", Material.CHEST, 1,
                        "§7Current balance: §6" + StringUtility.decimalify(bankData.getAmount(), 1),
                        " ",
                        "§7Store coins in the bank to keep",
                        "§7them safe while you go on",
                        "§7on adventures!",
                        " ",
                        "§7You will earn §b2%§7 interest every",
                        "§7season for your first §6" + "10 million",
                        "§7banked coins.",
                        " ",
                        "§7Until interest: §b" + SkyBlockCalendar.getHoursUntilNextInterest() + "h",
                        " ",
                        "§eClick to make a deposit!"
                ).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBankerDeposit(bankData.getSessionHash()));
                    return true;
                })
                .build());

        // Withdraw Button
        attachItem(GUIItem.builder(13)
                .item(() -> ItemStackCreator.getStack("§aWithdraw Coins", Material.DISPENSER, 1,
                        "§7Current balance: §6" + StringUtility.decimalify(bankData.getAmount(), 1),
                        " ",
                        "§7Withdraw coins from the bank",
                        "§7to use them for trading or",
                        "§7other purposes!",
                        " ",
                        "§eClick to make a withdrawal!"
                ).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBankerWithdraw(bankData.getSessionHash()));
                    return true;
                })
                .build());

        // Recent Transactions
        attachItem(GUIItem.builder(15)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    List<DatapointBankData.Transaction> transactions = bankData.getTransactions();

                    if (transactions.isEmpty()) {
                        lore.add("§cNo transactions yet!");
                    } else {
                        for (int i = Math.min(transactions.size() - 1, 10); i >= 0; i--) {
                            DatapointBankData.Transaction transaction = transactions.get(i);
                            boolean isNegative = transaction.amount < 0;
                            String amount = StringUtility.decimalify(Math.abs(transaction.amount), 1);

                            lore.add("§7" + (isNegative ? "§c-" : "§a+")
                                    + " §6" + amount + "§7, §e" + StringUtility.formatTimeAsAgo(transaction.timestamp)
                                    + "§7 by §b" + transaction.originator);
                        }
                    }

                    return ItemStackCreator.getStack("§aRecent Transactions",
                            Material.FILLED_MAP, 1, lore).build();
                })
                .build());
    }

    private void refreshItems(SkyBlockPlayer player) {
        setupBankItems(player);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}