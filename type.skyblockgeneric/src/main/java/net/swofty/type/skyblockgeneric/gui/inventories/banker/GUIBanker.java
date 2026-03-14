package net.swofty.type.skyblockgeneric.gui.inventories.banker;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBankData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIBanker extends HypixelInventoryGUI implements RefreshingGUI {
    public GUIBanker() {
        super(I18n.string("gui_banker.main.title"), InventoryType.CHEST_4_ROW);
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        if (((SkyBlockPlayer) e.player()).isBankDelayed) {
            e.player().sendMessage(I18n.string("gui_banker.main.processing_transactions"));
            e.player().sendMessage(I18n.string("gui_banker.main.processing_wait"));
            e.player().closeInventory();
            return;
        }

        e.inventory().setTitle(Component.text(
                ((SkyBlockPlayer) e.player()).isCoop()
                        ? I18n.string("gui_banker.main.title_coop")
                        : I18n.string("gui_banker.main.title_personal")
        ));

        refreshItems(e.player());
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(31));

        DatapointBankData.BankData bankData = (((SkyBlockPlayer) player).getSkyblockDataHandler())
                .get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class)
                .getValue();

        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.main.information"), Material.REDSTONE_TORCH, 1,
                        I18n.lore("gui_banker.main.information.lore", Map.of(
                                "limit", StringUtility.commaify(bankData.getBalanceLimit()),
                                "hours", String.valueOf(SkyBlockCalendar.getHoursUntilNextInterest())
                        )));
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBankerDeposit().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.main.deposit"), Material.CHEST, 1,
                        I18n.lore("gui_banker.main.deposit.lore", Map.of(
                                "balance", StringUtility.decimalify(bankData.getAmount(), 1),
                                "hours", String.valueOf(SkyBlockCalendar.getHoursUntilNextInterest())
                        )));
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBankerWithdraw().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(I18n.string("gui_banker.main.withdraw"), Material.DISPENSER, 1,
                        I18n.lore("gui_banker.main.withdraw.lore", Map.of(
                                "balance", StringUtility.decimalify(bankData.getAmount(), 1)
                        )));
            }
        });

        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                List<DatapointBankData.Transaction> transactions = bankData.getTransactions();

                if (transactions.isEmpty()) lore.add(I18n.string("gui_banker.main.no_transactions"));
                else {
                    for (int i = Math.min(transactions.size() - 1, 10); i >= 0; i--) {
                        DatapointBankData.Transaction transaction = transactions.get(i);

                        boolean isNegative = transaction.amount < 0;
                        String amount = StringUtility.decimalify(Math.abs(transaction.amount), 1);

                        lore.add(I18n.string("gui_banker.main.transaction_entry", Map.of(
                                "sign", isNegative ? "§c-" : "§a+",
                                "amount", amount,
                                "time_ago", StringUtility.formatTimeAsAgo(transaction.timestamp),
                                "originator", transaction.originator
                        )));
                    }
                }

                return ItemStackCreator.getStack(I18n.string("gui_banker.main.recent_transactions"),
                        Material.FILLED_MAP, 1, lore
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
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
