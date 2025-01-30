package net.swofty.types.generic.gui.inventory.inventories.banker;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBankData;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.mission.missions.MissionDepositCoinsInBank;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUIBankerDeposit extends SkyBlockAbstractInventory {
    private static final String STATE_BANK_DELAYED = "bank_delayed";
    private static final String STATE_DEPOSIT_SUCCESS = "deposit_success";
    private static final String STATE_DEPOSIT_FAILED = "deposit_failed";

    private final UUID bankHash;

    public GUIBankerDeposit(UUID bankHash) {
        super(InventoryType.CHEST_4_ROW);
        this.bankHash = bankHash;
        doAction(new SetTitleAction(Component.text("Bank Deposit")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Back button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Bank").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBanker());
                    return true;
                })
                .build());

        // Full purse deposit
        attachItem(GUIItem.builder(11)
                .item(() -> getDepositItemStack(player, player.getCoins(), 64))
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    attemptDeposit(ctx.player(), ctx.player().getCoins());
                    return true;
                })
                .build());

        // Half purse deposit
        attachItem(GUIItem.builder(13)
                .item(() -> getDepositItemStack(player, player.getCoins() / 2, 32))
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    attemptDeposit(ctx.player(), ctx.player().getCoins() / 2);
                    return true;
                })
                .build());

        // Custom amount deposit
        attachItem(GUIItem.builder(15)
                .item(() -> ItemStackCreator.getStack("§aCustom amount", Material.OAK_SIGN, 1,
                        "§8Bank deposit",
                        " ",
                        "§7Current balance: §6" + StringUtility.decimalify(
                                player.getDataHandler().get(DataHandler.Data.BANK_DATA, DatapointBankData.class).getValue().getAmount(), 1
                        ),
                        "§7Amount to deposit: §6Custom",
                        " ",
                        "§eClick to deposit coins!").build())
                .onClick((ctx, item) -> {
                    SkyBlockSignGUI signGUI = new SkyBlockSignGUI(ctx.player());
                    signGUI.open(new String[]{"Enter amount", "to deposit"}).thenAccept(query -> {
                        try {
                            double amount = Double.parseDouble(query);
                            if (amount > ctx.player().getCoins()) {
                                ctx.player().sendMessage("§cYou do not have that many coins to deposit!");
                                return;
                            }
                            if (amount <= 0) {
                                ctx.player().sendMessage("§cYou cannot deposit that amount!");
                                return;
                            }

                            ctx.player().closeInventory();
                            attemptDeposit(ctx.player(), amount);
                        } catch (NumberFormatException ex) {
                            ctx.player().sendMessage("§cThat is not a valid number!");
                        }
                    });
                    return true;
                })
                .build());
    }

    private ItemStack getDepositItemStack(SkyBlockPlayer player, double amount, int count) {
        return ItemStackCreator.getStack(
                count == 64 ? "§aYour whole purse" : "§aHalf of your purse",
                Material.CHEST, count,
                "§8Bank deposit",
                " ",
                "§7Current balance: §6" + StringUtility.decimalify(
                        player.getDataHandler().get(DataHandler.Data.BANK_DATA, DatapointBankData.class).getValue().getAmount(), 1
                ),
                "§7Amount to deposit: §6" + StringUtility.decimalify(amount, 1),
                " ",
                "§eClick to deposit coins!"
        ).build();
    }

    private void attemptDeposit(SkyBlockPlayer player, double amount) {
        if (player.getMissionData().isCurrentlyActive(MissionDepositCoinsInBank.class)) {
            player.getMissionData().endMission(MissionDepositCoinsInBank.class);
        }

        DatapointBankData.BankData bankData = player.getDataHandler().get(DataHandler.Data.BANK_DATA, DatapointBankData.class).getValue();
        if (bankData.getAmount() + amount > bankData.getBalanceLimit()) {
            player.sendMessage("§cYou cannot deposit that much, you would exceed your balance limit of §6" +
                    StringUtility.commaify(bankData.getBalanceLimit()) + " coins§c!");
            return;
        }

        player.sendMessage("§8Depositing coins...");
        player.setCoins(player.getCoins() - amount);

        if (!player.isCoop()) {
            processSoloDeposit(player, amount, bankData);
            return;
        }

        processCoopDeposit(player, amount, bankData);
    }

    private void processSoloDeposit(SkyBlockPlayer player, double amount, DatapointBankData.BankData bankData) {
        bankData.addAmount(amount);
        bankData.addTransaction(new DatapointBankData.Transaction(
                System.currentTimeMillis(),
                amount,
                player.getUsername()
        ));

        player.sendMessage("§aYou have deposited §6" + StringUtility.decimalify(amount, 1) +
                " coins§a! You now have §6" + StringUtility.decimalify(bankData.getAmount(), 1) +
                " coins§a in your account.");
    }

    private void processCoopDeposit(SkyBlockPlayer player, double amount, DatapointBankData.BankData bankData) {
        CoopDatabase.Coop coop = player.getCoop();
        player.setBankDelayed(true);
        bankData.setSessionHash(UUID.randomUUID());

        Thread.startVirtualThread(() -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            AtomicBoolean allow = new AtomicBoolean(true);

            player.getLogHandler().debug("Your bank hash is " + bankHash);

            for (UUID memberId : coop.members()) {
                if (memberId.equals(player.getUuid())) continue;

                futures.add(CompletableFuture.runAsync(() -> {
                    ProxyPlayer proxyPlayer = new ProxyPlayer(memberId);
                    if (!proxyPlayer.isOnline().join()) return;

                    UUID newBankHash = proxyPlayer.getBankHash().join();
                    player.getLogHandler().debug("Bank hash for " + memberId + " is " + newBankHash);
                    if (newBankHash != null && !newBankHash.equals(bankHash)) {
                        allow.set(false);
                    }
                }));
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            player.getDataHandler().get(DataHandler.Data.BANK_DATA, DatapointBankData.class).setValue(bankData);

            if (!allow.get()) {
                player.sendMessage("§cYou cannot deposit coins as your coop members have invalidated your bank session.");
                player.setBankDelayed(false);
                player.setCoins(player.getCoins() + amount);
            } else {
                bankData.addAmount(amount);
                bankData.addTransaction(new DatapointBankData.Transaction(
                        System.currentTimeMillis(),
                        amount,
                        player.getUsername()
                ));
                player.getDataHandler().get(DataHandler.Data.BANK_DATA, DatapointBankData.class).setValue(bankData);

                player.sendMessage("§aYou have deposited §6" + StringUtility.decimalify(amount, 1) +
                        " coins§a! You now have §6" + StringUtility.decimalify(bankData.getAmount(), 1) +
                        " coins§a in your account.");
                player.setBankDelayed(false);
            }
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