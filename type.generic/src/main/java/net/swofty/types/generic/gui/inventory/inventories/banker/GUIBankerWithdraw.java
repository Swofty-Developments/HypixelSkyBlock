package net.swofty.types.generic.gui.inventory.inventories.banker;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBankData;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUIBankerWithdraw extends SkyBlockAbstractInventory {
    private static final String STATE_AWAITING_WITHDRAWAL = "awaiting_withdrawal";
    private static final String STATE_PROCESSING_WITHDRAWAL = "processing_withdrawal";

    private final UUID bankHash;

    public GUIBankerWithdraw(UUID bankHash) {
        super(InventoryType.CHEST_4_ROW);
        this.bankHash = bankHash;

        doAction(new SetTitleAction(Component.text("Bank Withdrawal")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        doAction(new AddStateAction(STATE_AWAITING_WITHDRAWAL));
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        setupWithdrawalButtons(player);
    }

    private void setupWithdrawalButtons(SkyBlockPlayer player) {
        double bankBalance = player.getDataHandler()
                .get(DataHandler.Data.BANK_DATA, DatapointBankData.class)
                .getValue()
                .getAmount();

        // Back Button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Bank").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBanker());
                    return true;
                })
                .build());

        // Full Amount Button
        attachItem(createWithdrawalButton(10, "Everything in the account", 64, bankBalance, bankBalance));

        // Half Amount Button
        attachItem(createWithdrawalButton(12, "Half of the account", 32, bankBalance, bankBalance / 2));

        // 20% Amount Button
        attachItem(createWithdrawalButton(14, "20% of the account", 1, bankBalance, bankBalance / 5));

        // Custom Amount Button
        attachItem(GUIItem.builder(16)
                .item(ItemStackCreator.getStack("§aCustom amount", Material.OAK_SIGN, 1,
                        "§8Bank withdrawal",
                        " ",
                        "§7Current balance: §6" + StringUtility.decimalify(bankBalance, 1),
                        " ",
                        "§eClick to withdraw coins!").build())
                .onClick((ctx, item) -> handleCustomWithdrawal(ctx.player()))
                .build());
    }

    private GUIItem createWithdrawalButton(int slot, String name, int count, double balance, double amount) {
        return GUIItem.builder(slot)
                .item(ItemStackCreator.getStack("§a" + name, Material.DISPENSER, count,
                        "§8Bank withdrawal",
                        " ",
                        "§7Current balance: §6" + StringUtility.decimalify(balance, 1),
                        "§7Amount to withdraw: §6" + StringUtility.decimalify(amount, 1),
                        " ",
                        "§eClick to withdraw coins!").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    attemptWithdrawal(ctx.player(), amount);
                    return true;
                })
                .requireState(STATE_AWAITING_WITHDRAWAL)
                .build();
    }

    private boolean handleCustomWithdrawal(SkyBlockPlayer player) {
        SkyBlockSignGUI signGUI = new SkyBlockSignGUI(player);
        String output = signGUI.open(new String[]{"Enter amount", "to withdraw"}).join();

        try {
            double amount = Double.parseDouble(output);
            double balance = player.getDataHandler()
                    .get(DataHandler.Data.BANK_DATA, DatapointBankData.class)
                    .getValue()
                    .getAmount();

            if (amount > balance) {
                player.sendMessage("§cYou do not have that many coins to withdraw!");
                return true;
            }
            if (amount <= 0) {
                player.sendMessage("§cYou cannot withdraw that amount!");
                return true;
            }

            player.closeInventory();
            attemptWithdrawal(player, amount);
        } catch (NumberFormatException ex) {
            player.sendMessage("§cThat is not a valid number!");
        }
        return true;
    }

    private void attemptWithdrawal(SkyBlockPlayer player, double amount) {
        doAction(new AddStateAction(STATE_PROCESSING_WITHDRAWAL));
        DatapointBankData.BankData bankData = player.getDataHandler()
                .get(DataHandler.Data.BANK_DATA, DatapointBankData.class)
                .getValue();

        if (amount > bankData.getAmount()) {
            player.sendMessage("§cYou do not have that many coins to withdraw!");
            return;
        }

        player.sendMessage("§8Withdrawing coins...");
        if (!player.isCoop()) {
            processNormalWithdrawal(player, amount, bankData);
            return;
        }

        processCoopWithdrawal(player, amount, bankData);
    }

    private void processNormalWithdrawal(SkyBlockPlayer player, double amount, DatapointBankData.BankData bankData) {
        bankData.removeAmount(amount);
        bankData.addTransaction(new DatapointBankData.Transaction(
                System.currentTimeMillis(),
                -amount,
                player.getUsername()
        ));

        player.setCoins(player.getCoins() + amount);
        player.sendMessage("§aYou have withdrawn §6" + StringUtility.decimalify(amount, 1) +
                " coins§a! You now have §6" + StringUtility.decimalify(bankData.getAmount(), 1) +
                " coins§a in your account.");
    }

    private void processCoopWithdrawal(SkyBlockPlayer player, double amount, DatapointBankData.BankData bankData) {
        CoopDatabase.Coop coop = player.getCoop();
        player.setBankDelayed(true);

        Thread.startVirtualThread(() -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            AtomicBoolean allow = new AtomicBoolean(true);

            for (UUID memberId : coop.members()) {
                if (memberId.equals(player.getUuid())) continue;

                futures.add(CompletableFuture.runAsync(() -> {
                    ProxyPlayer proxyPlayer = new ProxyPlayer(memberId);
                    if (!proxyPlayer.isOnline().join()) return;

                    UUID memberBankHash = proxyPlayer.getBankHash().join();
                    if (memberBankHash != null && !memberBankHash.equals(this.bankHash)) {
                        allow.set(false);
                    }
                }));
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            if (!allow.get()) {
                player.sendMessage("§cYou cannot withdraw coins as your coop members have invalidated your bank session.");
            } else {
                bankData.removeAmount(amount);
                bankData.setSessionHash(UUID.randomUUID());
                bankData.addTransaction(new DatapointBankData.Transaction(
                        System.currentTimeMillis(),
                        -amount,
                        player.getUsername()
                ));

                player.setCoins(player.getCoins() + amount);
                player.getDataHandler()
                        .get(DataHandler.Data.BANK_DATA, DatapointBankData.class)
                        .setValue(bankData);

                player.sendMessage("§aYou have withdrawn §6" + StringUtility.decimalify(amount, 1) +
                        " coins§a! You now have §6" + StringUtility.decimalify(bankData.getAmount(), 1) +
                        " coins§a in your account.");
            }

            player.setBankDelayed(false);
            doAction(new RemoveStateAction(STATE_PROCESSING_WITHDRAWAL));
            doAction(new AddStateAction(STATE_AWAITING_WITHDRAWAL));
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        if (event == null) return;
        ((SkyBlockPlayer) event.getPlayer()).setBankDelayed(false);
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        player.setBankDelayed(false);
    }
}