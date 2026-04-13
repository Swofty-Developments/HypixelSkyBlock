package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.bazaar.BazaarConnector;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCompletedBazaarTransactions;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GUIBazaarOrderCompletedOptions extends HypixelInventoryGUI {
    private final List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction> completions;
    private final BazaarConnector.BazaarOrder activeOrder;
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
            .withZone(ZoneId.systemDefault());

    // Cached calculation results
    private final TransactionSummary summary;

    public GUIBazaarOrderCompletedOptions(List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction> completions,
                                          BazaarConnector.BazaarOrder activeOrder) {
        super(I18n.t("gui_bazaar.order_completed.title"), InventoryType.CHEST_4_ROW);
        this.completions = completions;
        this.activeOrder = activeOrder;
        this.summary = calculateSummary(completions);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(27, new GUIBazaarOrders()));
        set(GUIClickableItem.getCloseItem(35));

        setupItems();
    }

    /**
     * Calculate all transaction statistics in a single pass for better performance
     */
    private static TransactionSummary calculateSummary(List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction> completions) {
        if (completions == null || completions.isEmpty()) {
            return new TransactionSummary(0, 0, 0, 0, 0);
        }

        double totalQuantity = 0;
        double totalValue = 0;
        double totalSpent = 0;
        double totalSecondaryAmount = 0;

        for (var tx : completions) {
            totalQuantity += tx.getQuantity();
            totalValue += tx.getTotalValue();
            totalSpent += tx.getPricePerUnit() * tx.getQuantity();
            totalSecondaryAmount += tx.getSecondaryAmount();
        }

        return new TransactionSummary(
                totalQuantity,
                totalValue,
                totalSpent,
                totalSecondaryAmount,
                completions.size()
        );
    }

    private record TransactionSummary(
            double totalQuantity,
            double totalValue,
            double totalSpent,
            double totalSecondaryAmount, // Tax for sells, savings/refund for buys
            int transactionCount
    ) {}

    private void setupItems() {
        if (completions == null || completions.isEmpty()) return;

        var firstCompletion = completions.getFirst();
        String itemName = firstCompletion.getItemName();
        ItemType itemType;

        try {
            itemType = ItemType.valueOf(itemName);
        } catch (IllegalArgumentException e) {
            itemType = ItemType.STONE;
        }

        boolean isSell = isSellOrder();

        ItemType finalItemType = itemType;
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();

                lore.add(I18n.string("gui_bazaar.order_completed.order_completed_label", l));
                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.order_completed.completed_count", l, Component.text(String.valueOf((int) summary.totalQuantity)), Component.text(finalItemType.getDisplayName())));
                lore.add(I18n.string("gui_bazaar.order_completed.transactions_count", l, Component.text(String.valueOf(summary.transactionCount))));
                lore.add(" ");

                if (isSell) {
                    lore.add(I18n.string("gui_bazaar.order_completed.gross_earnings", l, Component.text(FORMATTER.format(summary.totalSpent))));
                    lore.add(I18n.string("gui_bazaar.order_completed.tax_paid", l, Component.text(FORMATTER.format(summary.totalSecondaryAmount))));
                    lore.add(I18n.string("gui_bazaar.order_completed.net_earnings", l, Component.text(FORMATTER.format(Math.abs(summary.totalValue)))));
                } else {
                    lore.add(I18n.string("gui_bazaar.order_completed.total_spent", l, Component.text(FORMATTER.format(summary.totalSpent))));
                    if (summary.totalSecondaryAmount > 0) {
                        lore.add(I18n.string("gui_bazaar.order_completed.total_saved", l, Component.text(FORMATTER.format(summary.totalSecondaryAmount))));
                        lore.add(I18n.string("gui_bazaar.order_completed.refund_ready", l, Component.text(FORMATTER.format(summary.totalSecondaryAmount))));
                    }
                }

                return ItemStackCreator.getStack(
                    I18n.string("gui_bazaar.order_completed.order_name", l, Component.text(finalItemType.getDisplayName())),
                        finalItemType.material,
                        Math.max(1, (int) summary.totalQuantity),
                        lore
                );
            }
        });

        setupTransactionHistory();
        setupClaimButton();
        setupToggleButton();
    }

    private void setupTransactionHistory() {
        set(new GUIItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_bazaar.order_completed.transaction_subtitle", l));
                lore.add(" ");

                int count = 0;
                for (var tx : completions) {
                    if (count >= 10) {
                        lore.add(I18n.string("gui_bazaar.order_completed.transaction_more", l, Component.text(String.valueOf(completions.size() - 10))));
                        break;
                    }

                    String timeStr = TIME_FORMATTER.format(tx.getTimestamp());

                    if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.BUY_COMPLETED) {
                        lore.add("§a▲ " + (int) tx.getQuantity() + "x at §6" +
                                FORMATTER.format(tx.getPricePerUnit()) + " §8(" + timeStr + ")");
                        if (tx.getSecondaryAmount() > 0) {
                            lore.add("  " + I18n.string("gui_bazaar.order_completed.transaction_saved", l, Component.text(FORMATTER.format(tx.getSecondaryAmount()))));
                        }
                    } else if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.SELL_COMPLETED) {
                        lore.add("§6▼ " + (int) tx.getQuantity() + "x at §6" +
                                FORMATTER.format(tx.getPricePerUnit()) + " §8(" + timeStr + ")");
                        lore.add("  " + I18n.string("gui_bazaar.order_completed.transaction_tax", l, Component.text(FORMATTER.format(tx.getSecondaryAmount()))));
                    } else if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.REFUND) {
                        lore.add(I18n.string("gui_bazaar.order_completed.refund_label", l, Component.text(FORMATTER.format(tx.getSecondaryAmount()))) + " §8(" + timeStr + ")");
                    }
                    count++;
                }

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.order_completed.transaction_history", l), Material.BOOK, 1, lore);
            }
        });
    }

    private void setupClaimButton() {
        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                claimRewards(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>(I18n.lore("gui_bazaar.order_completed.claim_rewards_header", l));
                lore.add(" ");

                boolean isSell = isSellOrder();
                if (isSell) {
                    lore.add("§6▶ +" + FORMATTER.format(Math.abs(summary.totalValue)) + " coins");
                } else {
                    lore.add("§a▶ +" + (int) summary.totalQuantity + "x " + getItemType().getDisplayName());
                    if (summary.totalSecondaryAmount > 0) {
                        lore.add("§6▶ +" + FORMATTER.format(summary.totalSecondaryAmount) + " coins refund");
                    }
                }

                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.order_completed.claim_click", l));

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.order_completed.claim_rewards", l), Material.CHEST, 1, lore);
            }
        });
    }

    private void setupToggleButton() {
        if (activeOrder == null) return;

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBazaarOrderOptions(activeOrder).open(p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return TranslatableItemStackCreator.getStack("gui_bazaar.order_completed.view_unfilled", Material.COMPASS, 1,
                    "gui_bazaar.order_completed.view_unfilled.lore",
                    Component.text(String.valueOf((int) activeOrder.amount())),
                    Component.text(FORMATTER.format(activeOrder.price())));
            }
        });
    }

    private void claimRewards(SkyBlockPlayer player) {
        Locale l = player.getLocale();
        if (completions == null || completions.isEmpty()) {
            player.sendMessage(I18n.t("gui_bazaar.order_completed.no_rewards"));
            return;
        }

        boolean isSell = isSellOrder();
        ItemType itemType = getItemType();

        try {
            if (isSell) {
                player.addCoins(Math.abs(summary.totalValue));
                player.sendMessage(I18n.string("gui_bazaar.order_completed.received_coins", l, Component.text(FORMATTER.format(Math.abs(summary.totalValue)))));
            } else {
                SkyBlockItem item = new SkyBlockItem(itemType);
                item.setAmount((int) summary.totalQuantity);
                player.addAndUpdateItem(item);
                player.sendMessage(I18n.string("gui_bazaar.order_completed.received_items", l, Component.text(String.valueOf((int) summary.totalQuantity)), Component.text(itemType.getDisplayName())));

                if (summary.totalSecondaryAmount > 0) {
                    player.addCoins(summary.totalSecondaryAmount);
                    player.sendMessage(I18n.string("gui_bazaar.order_completed.received_refund", l, Component.text(FORMATTER.format(summary.totalSecondaryAmount))));
                }
            }

            var completedTransactions = player.getSkyblockDataHandler().get(
                    SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                    DatapointCompletedBazaarTransactions.class
            ).getValue();

            List<String> transactionIds = completions.stream()
                    .map(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getId)
                    .toList();

            completedTransactions.claimTransactions(transactionIds);

            player.sendMessage(I18n.t("gui_bazaar.order_completed.all_claimed"));
            player.playSuccessSound();

            new GUIBazaarOrders().open(player);

        } catch (Exception e) {
            player.sendMessage(I18n.string("gui_bazaar.order_completed.claim_failed", l, Component.text(e.getMessage())));
            System.err.println("Failed to claim bazaar rewards: " + e.getMessage());
        }
    }

    private boolean isSellOrder() {
        if (completions == null || completions.isEmpty()) return false;
        var firstCompletion = completions.getFirst();
        return firstCompletion.getType() == DatapointCompletedBazaarTransactions.TransactionType.SELL_COMPLETED ||
                firstCompletion.getType() == DatapointCompletedBazaarTransactions.TransactionType.SELL_ORDER_EXPIRED;
    }

    private ItemType getItemType() {
        if (completions == null || completions.isEmpty()) return ItemType.STONE;
        try {
            return ItemType.valueOf(completions.getFirst().getItemName());
        } catch (IllegalArgumentException e) {
            return ItemType.STONE;
        }
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
