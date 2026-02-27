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
import net.swofty.type.skyblockgeneric.mission.missions.MissionDepositCoinsInBank;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;
import java.util.Map;

public class GUIBankerDeposit extends HypixelInventoryGUI {

    public GUIBankerDeposit() {
        super(I18n.string("gui_banker.deposit.title"), InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, new GUIBanker()));

        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.deposit.whole_purse"), Material.CHEST, 64,
                        List.of(
                                I18n.string("gui_banker.deposit.whole_purse_subtitle"),
                                " ",
                                I18n.string("gui_banker.deposit.current_balance", Map.of(
                                        "balance", StringUtility.decimalify(
                                                player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue().getAmount(), 1)
                                )),
                                I18n.string("gui_banker.deposit.amount_to_deposit", Map.of(
                                        "amount", StringUtility.decimalify(player.getCoins(), 1)
                                )),
                                " ",
                                I18n.string("gui_banker.deposit.click")
                        ));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.closeInventory();
                attemptDeposit(player, player.getCoins());
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.deposit.half_purse"), Material.CHEST, 32,
                        List.of(
                                I18n.string("gui_banker.deposit.whole_purse_subtitle"),
                                " ",
                                I18n.string("gui_banker.deposit.current_balance", Map.of(
                                        "balance", StringUtility.decimalify(
                                                player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue().getAmount(), 1)
                                )),
                                I18n.string("gui_banker.deposit.amount_to_deposit", Map.of(
                                        "amount", StringUtility.decimalify(player.getCoins() / 2, 1)
                                )),
                                " ",
                                I18n.string("gui_banker.deposit.click")
                        ));
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.closeInventory();
                attemptDeposit(player, player.getCoins() / 2);
            }
        });

        set(new GUIQueryItem(15) {
            @Override
            public HypixelInventoryGUI onQueryFinish(String query, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                try {
                    double amount = Double.parseDouble(query);
                    if (amount > player.getCoins()) {
                        player.sendMessage(I18n.string("gui_banker.deposit.not_enough_coins"));
                        return null;
                    }
                    if (amount <= 0) {
                        player.sendMessage(I18n.string("gui_banker.deposit.invalid_amount"));
                        return null;
                    }

                    player.closeInventory();
                    attemptDeposit(player, amount);
                } catch (NumberFormatException ex) {
                    player.sendMessage(I18n.string("gui_banker.deposit.invalid_number"));
                }
                return null;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.deposit.custom_amount"), Material.OAK_SIGN, 1,
                        List.of(
                                I18n.string("gui_banker.deposit.whole_purse_subtitle"),
                                " ",
                                I18n.string("gui_banker.deposit.current_balance", Map.of(
                                        "balance", StringUtility.decimalify(
                                                player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue().getAmount(), 1)
                                )),
                                I18n.string("gui_banker.deposit.custom_amount_label"),
                                " ",
                                I18n.string("gui_banker.deposit.click")
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

    private void attemptDeposit(SkyBlockPlayer player, double amount) {
        if (player.getMissionData().isCurrentlyActive(MissionDepositCoinsInBank.class)) {
            player.getMissionData().endMission(MissionDepositCoinsInBank.class);
        }
        DatapointBankData.BankData bankData = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue();
        if (bankData.getAmount() + amount > bankData.getBalanceLimit()) {
            player.sendMessage(I18n.string("gui_banker.deposit.exceed_limit", Map.of(
                    "limit", StringUtility.commaify(bankData.getBalanceLimit())
            )));
            return;
        }

        player.sendMessage(I18n.string("gui_banker.deposit.depositing"));
        player.removeCoins(amount);
        if (!player.isCoop()) {
            bankData.addAmount(amount);
            bankData.addTransaction(new DatapointBankData.Transaction(
                    System.currentTimeMillis(),
                    amount,
                    player.getUsername()
            ));

            player.sendMessage(I18n.string("gui_banker.deposit.success", Map.of(
                    "amount", StringUtility.decimalify(amount, 1),
                    "balance", StringUtility.decimalify(bankData.getAmount(), 1)
            )));
            return;
        }
        CoopDatabase.Coop coop = player.getCoop();
        player.setBankDelayed(true);

        String lockKey = "bank_data:" + player.getSkyBlockIsland().getIslandID().toString();
        DataMutexService mutexService = new DataMutexService();

        mutexService.withSynchronizedData(
                lockKey,
                coop.members(),
                SkyBlockDataHandler.Data.BANK_DATA,
                (DatapointBankData.BankData latestBankData) -> {
                    if (latestBankData.getAmount() + amount > latestBankData.getBalanceLimit()) {
                        player.sendMessage(I18n.string("gui_banker.deposit.exceed_limit", Map.of(
                                "limit", StringUtility.commaify(latestBankData.getBalanceLimit())
                        )));
                        return null;
                    }

                    player.removeCoins(amount);
                    latestBankData.addAmount(amount);
                    latestBankData.addTransaction(new DatapointBankData.Transaction(
                            System.currentTimeMillis(), amount, player.getUsername()));

                    player.sendMessage(I18n.string("gui_banker.deposit.success", Map.of(
                            "amount", StringUtility.decimalify(amount, 1),
                            "balance", StringUtility.decimalify(latestBankData.getAmount(), 1)
                    )));

                    return latestBankData;
                },
                () -> {
                    player.sendMessage(I18n.string("gui_banker.deposit.coop_busy"));
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
