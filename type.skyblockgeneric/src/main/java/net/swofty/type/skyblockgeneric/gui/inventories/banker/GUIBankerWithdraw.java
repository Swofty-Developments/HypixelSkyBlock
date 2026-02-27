package net.swofty.type.skyblockgeneric.gui.inventories.banker;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIQueryItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.DataMutexService;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBankData;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;
import java.util.Map;

public class GUIBankerWithdraw extends HypixelInventoryGUI {

    public GUIBankerWithdraw() {
        super(I18n.string("gui_banker.withdraw.title"), InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, new GUIBanker()));

        double bankBalance = ((SkyBlockPlayer) e.player()).getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue().getAmount();

        set(new GUIClickableItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.withdraw.everything"), Material.DISPENSER, 64,
                        List.of(
                                I18n.string("gui_banker.withdraw.everything_subtitle"),
                                " ",
                                I18n.string("gui_banker.withdraw.current_balance", Map.of(
                                        "balance", StringUtility.decimalify(bankBalance, 1)
                                )),
                                I18n.string("gui_banker.withdraw.amount_to_withdraw", Map.of(
                                        "amount", StringUtility.decimalify(bankBalance, 1)
                                )),
                                " ",
                                I18n.string("gui_banker.withdraw.click")
                        ));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.closeInventory();
                attemptWithdrawal(player, bankBalance);
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.withdraw.half_account"), Material.DISPENSER, 32,
                        List.of(
                                I18n.string("gui_banker.withdraw.everything_subtitle"),
                                " ",
                                I18n.string("gui_banker.withdraw.current_balance", Map.of(
                                        "balance", StringUtility.decimalify(bankBalance, 1)
                                )),
                                I18n.string("gui_banker.withdraw.amount_to_withdraw", Map.of(
                                        "amount", StringUtility.decimalify(bankBalance / 2, 1)
                                )),
                                " ",
                                I18n.string("gui_banker.withdraw.click")
                        ));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.closeInventory();
                attemptWithdrawal(player, bankBalance / 2);
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.closeInventory();
                attemptWithdrawal(player, bankBalance / 5);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.withdraw.twenty_percent"), Material.DISPENSER, 1,
                        List.of(
                                I18n.string("gui_banker.withdraw.everything_subtitle"),
                                " ",
                                I18n.string("gui_banker.withdraw.current_balance", Map.of(
                                        "balance", StringUtility.decimalify(bankBalance, 1)
                                )),
                                I18n.string("gui_banker.withdraw.amount_to_withdraw", Map.of(
                                        "amount", StringUtility.decimalify(bankBalance / 5, 1)
                                )),
                                " ",
                                I18n.string("gui_banker.withdraw.click")
                        ));
            }
        });

        set(new GUIQueryItem(16) {
            @Override
            public HypixelInventoryGUI onQueryFinish(String query, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                try {
                    double amount = Double.parseDouble(query);
                    if (amount > bankBalance) {
                        player.sendMessage(I18n.string("gui_banker.withdraw.not_enough_coins"));
                        return null;
                    }
                    if (amount <= 0) {
                        player.sendMessage(I18n.string("gui_banker.withdraw.invalid_amount"));
                        return null;
                    }

                    player.closeInventory();
                    attemptWithdrawal(player, amount);
                } catch (NumberFormatException ex) {
                    player.sendMessage(I18n.string("gui_banker.withdraw.invalid_number"));
                }
                return null;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.withdraw.custom_amount"), Material.OAK_SIGN, 1,
                        List.of(
                                I18n.string("gui_banker.withdraw.everything_subtitle"),
                                " ",
                                I18n.string("gui_banker.withdraw.current_balance", Map.of(
                                        "balance", StringUtility.decimalify(
                                                player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue().getAmount(), 1)
                                )),
                                " ",
                                I18n.string("gui_banker.withdraw.click")
                        ));
            }
        });
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        if (e == null) return;
        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
        player.setBankDelayed(false);
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
        ((SkyBlockPlayer) player).setBankDelayed(false);
    }

    private void attemptWithdrawal(SkyBlockPlayer player, double amount) {
        player.sendMessage(I18n.string("gui_banker.withdraw.withdrawing"));

        if (!player.isCoop()) {
            DatapointBankData.BankData bankData = player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue();
            if (amount > bankData.getAmount()) {
                player.sendMessage(I18n.string("gui_banker.withdraw.not_enough_coins"));
                return;
            }

            bankData.removeAmount(amount);
            bankData.addTransaction(new DatapointBankData.Transaction(
                    System.currentTimeMillis(), -amount, player.getUsername()));

            player.addCoins(amount);
            player.sendMessage(I18n.string("gui_banker.withdraw.success", Map.of(
                    "amount", StringUtility.decimalify(amount, 1),
                    "balance", StringUtility.decimalify(bankData.getAmount(), 1)
            )));
            return;
        }

        // Coop scenario - use mutex service
        CoopDatabase.Coop coop = player.getCoop();
        String lockKey = "bank_data:" + player.getSkyBlockIsland().getIslandID().toString();

        DataMutexService mutexService = new DataMutexService();

        mutexService.withSynchronizedData(
                lockKey,
                coop.members(),
                SkyBlockDataHandler.Data.BANK_DATA,

                (DatapointBankData.BankData latestBankData) -> {
                    if (amount > latestBankData.getAmount()) {
                        player.sendMessage(I18n.string("gui_banker.withdraw.not_enough_coins"));
                        return null;
                    }

                    latestBankData.removeAmount(amount);
                    latestBankData.addTransaction(new DatapointBankData.Transaction(
                            System.currentTimeMillis(), -amount, player.getUsername()));

                    player.addCoins(amount);
                    player.sendMessage(I18n.string("gui_banker.withdraw.success", Map.of(
                            "amount", StringUtility.decimalify(amount, 1),
                            "balance", StringUtility.decimalify(latestBankData.getAmount(), 1)
                    )));

                    return latestBankData;
                },
                () -> {
                    player.sendMessage(I18n.string("gui_banker.withdraw.coop_busy"));
                }
        );
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