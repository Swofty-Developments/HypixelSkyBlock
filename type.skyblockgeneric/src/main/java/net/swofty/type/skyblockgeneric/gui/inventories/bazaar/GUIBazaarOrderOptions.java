package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

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
import java.util.Map;

public class GUIBazaarOrderOptions extends HypixelInventoryGUI {
    private final BazaarConnector.BazaarOrder order;
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
            .withZone(ZoneId.systemDefault());

    public GUIBazaarOrderOptions(BazaarConnector.BazaarOrder order) {
        super(I18n.string("gui_bazaar.order_options.title", Map.of("item_name", order.getItemType().getDisplayName())), InventoryType.CHEST_4_ROW);
        this.order = order;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(27, new GUIBazaarOrders()));
        set(GUIClickableItem.getCloseItem(35));

        setupItems();
    }

    private void setupItems() {
        ItemType itemType = order.getItemType();
        if (itemType == null) return;

        boolean isSell = order.side() == BazaarConnector.OrderSide.SELL;

        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();

                var relatedTransactions = getRelatedTransactions(player);
                double originalQuantity = getOriginalQuantity(player);
                double filledQuantity = getFilledQuantity(relatedTransactions);

                lore.add(isSell ? I18n.string("gui_bazaar.order_options.order_type_sell", l) : I18n.string("gui_bazaar.order_options.order_type_buy", l));
                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.order_options.original_amount", l, Map.of("amount", String.valueOf((int) originalQuantity))));
                lore.add(I18n.string("gui_bazaar.order_options.remaining_amount", l, Map.of("amount", String.valueOf((int) order.amount()))));
                lore.add(I18n.string("gui_bazaar.order_options.price_per_unit", l, Map.of("price", FORMATTER.format(order.price()))));
                lore.add(" ");

                if (filledQuantity > 0) {
                    lore.add(I18n.string("gui_bazaar.order_options.filled_items", l, Map.of("count", String.valueOf((int) filledQuantity))));
                    lore.add(I18n.string("gui_bazaar.order_options.filled_progress", l, Map.of("percent", String.format("%.1f%%", (filledQuantity / originalQuantity) * 100))));
                } else {
                    lore.add(I18n.string("gui_bazaar.order_options.no_fills", l));
                    lore.add(I18n.string("gui_bazaar.order_options.no_fills_progress", l));
                }

                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.order_options.order_id", l, Map.of("id", order.orderId().toString().substring(0, 8))));

                return ItemStackCreator.getStack(
                        (isSell ? "§6" : "§a") + itemType.getDisplayName() + " Order",
                        itemType.material,
                        Math.max(1, (int) order.amount()),
                        lore
                );
            }
        });

        setupTransactionHistory();
        setupFinancialSummary();
        setupActionButtons();
        setupToggleButton();
    }

    private void setupTransactionHistory() {
        set(new GUIItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_bazaar.order_options.transaction_history_subtitle", l));
                lore.add(" ");

                var transactions = getRelatedTransactions(player);
                if (transactions.isEmpty()) {
                    lore.add(I18n.string("gui_bazaar.order_options.no_transactions", l));
                    lore.add(I18n.string("gui_bazaar.order_options.no_transactions_pending", l));
                } else {
                    lore.add(I18n.string("gui_bazaar.order_options.transaction_count", l, Map.of("count", String.valueOf(transactions.size()), "suffix", transactions.size() == 1 ? "" : "s")));
                    lore.add(" ");

                    int count = 0;
                    for (var tx : transactions) {
                        if (count >= 5) {
                            lore.add(I18n.string("gui_bazaar.order_options.transaction_more", l, Map.of("count", String.valueOf(transactions.size() - 5))));
                            break;
                        }

                        String timeStr = TIME_FORMATTER.format(tx.getTimestamp());
                        if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.BUY_COMPLETED) {
                            lore.add("§a▲ " + (int) tx.getQuantity() + "x at §6" +
                                    FORMATTER.format(tx.getPricePerUnit()) + " §8(" + timeStr + ")");
                            if (tx.getSecondaryAmount() > 0) {
                                lore.add("  " + I18n.string("gui_bazaar.order_options.transaction_saved", l, Map.of("amount", FORMATTER.format(tx.getSecondaryAmount()))));
                            }
                        } else if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.SELL_COMPLETED) {
                            lore.add("§6▼ " + (int) tx.getQuantity() + "x at §6" +
                                    FORMATTER.format(tx.getPricePerUnit()) + " §8(" + timeStr + ")");
                            lore.add("  " + I18n.string("gui_bazaar.order_options.transaction_tax", l, Map.of("amount", FORMATTER.format(tx.getSecondaryAmount()))));
                        }
                        count++;
                    }
                }

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.order_options.transaction_history", l), Material.BOOK, 1, lore);
            }
        });
    }

    private void setupFinancialSummary() {
        boolean isSell = order.side() == BazaarConnector.OrderSide.SELL;

        set(new GUIItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>();
                lore.add(I18n.string("gui_bazaar.order_options.financial_subtitle", l));
                lore.add(" ");

                var transactions = getRelatedTransactions(player);
                double originalValue = getOriginalQuantity(player) * order.price();
                double currentValue = order.amount() * order.price();

                lore.add(I18n.string("gui_bazaar.order_options.original_order_value", l));
                lore.add("  §6" + FORMATTER.format(originalValue) + " coins");
                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.order_options.remaining_order_value", l));
                lore.add("  §6" + FORMATTER.format(currentValue) + " coins");
                lore.add(" ");

                if (!transactions.isEmpty()) {
                    if (isSell) {
                        double totalEarned = transactions.stream()
                                .mapToDouble(tx -> tx.getPricePerUnit() * tx.getQuantity())
                                .sum();
                        double totalTax = transactions.stream()
                                .mapToDouble(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getSecondaryAmount)
                                .sum();
                        double netEarned = totalEarned - totalTax;

                        lore.add(I18n.string("gui_bazaar.order_options.transactions_completed", l));
                        lore.add("  " + I18n.string("gui_bazaar.order_options.gross_prefix", l) + FORMATTER.format(totalEarned) + " coins");
                        lore.add("  " + I18n.string("gui_bazaar.order_options.tax_prefix", l) + FORMATTER.format(totalTax) + " coins");
                        lore.add("  " + I18n.string("gui_bazaar.order_options.net_prefix", l) + FORMATTER.format(netEarned) + " coins");
                        lore.add(" ");
                        lore.add(I18n.string("gui_bazaar.order_options.avg_sell_price", l));
                        lore.add("  §6" + FORMATTER.format(totalEarned / getFilledQuantity(transactions)) + " coins/item");
                    } else {
                        double totalSpent = transactions.stream()
                                .mapToDouble(tx -> tx.getPricePerUnit() * tx.getQuantity())
                                .sum();
                        double totalSaved = transactions.stream()
                                .mapToDouble(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getSecondaryAmount)
                                .sum();

                        lore.add(I18n.string("gui_bazaar.order_options.transactions_completed", l));
                        lore.add("  " + I18n.string("gui_bazaar.order_options.spent_prefix", l) + FORMATTER.format(totalSpent) + " coins");
                        lore.add("  " + I18n.string("gui_bazaar.order_options.saved_prefix", l) + FORMATTER.format(totalSaved) + " coins");
                        lore.add(" ");
                        lore.add(I18n.string("gui_bazaar.order_options.avg_buy_price", l));
                        lore.add("  §6" + FORMATTER.format(totalSpent / getFilledQuantity(transactions)) + " coins/item");
                        lore.add(I18n.string("gui_bazaar.order_options.vs_your_bid", l, Map.of("price", FORMATTER.format(order.price()))));
                    }
                }

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.order_options.financial_summary", l), Material.GOLD_INGOT, 1, lore);
            }
        });
    }

    private void setupActionButtons() {
        ItemType itemType = order.getItemType();
        boolean isSell = order.side() == BazaarConnector.OrderSide.SELL;

        set(new GUIClickableItem(20) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                p.sendMessage(I18n.string("gui_bazaar.order_options.cancel_message", l));

                player.getBazaarConnector().cancelOrder(order.orderId())
                        .thenAccept(success -> {
                            if (success) {
                                p.sendMessage(I18n.string("gui_bazaar.order_options.cancel_success", l));

                                if (isSell) {
                                    SkyBlockItem item = new SkyBlockItem(order.getItemType());
                                    item.setAmount((int) order.amount());
                                    player.addAndUpdateItem(item);
                                    p.sendMessage(I18n.string("gui_bazaar.order_options.cancel_return_items", l, Map.of("amount", String.valueOf((int) order.amount()), "item_name", itemType.getDisplayName())));
                                } else {
                                    double refund = order.price() * order.amount();
                                    player.addCoins(refund);
                                    p.sendMessage(I18n.string("gui_bazaar.order_options.cancel_refund_coins", l, Map.of("amount", FORMATTER.format(refund))));
                                }

                                new GUIBazaarOrders().open(p);
                            } else {
                                p.sendMessage(I18n.string("gui_bazaar.order_options.cancel_failed", l));
                            }
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                List<String> lore = new ArrayList<>(I18n.lore("gui_bazaar.order_options.cancel_order.lore_header", l));
                lore.add(" ");

                if (isSell) {
                    lore.add("§a▶ " + (int) order.amount() + "x " + itemType.getDisplayName());
                } else {
                    lore.add("§6▶ " + FORMATTER.format(order.amount() * order.price()) + " coins");
                }

                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.order_options.cancel_undone", l));
                lore.addAll(I18n.lore("gui_bazaar.order_options.cancel_completed_remain", l));
                lore.add(" ");
                lore.add(I18n.string("gui_bazaar.order_options.cancel_click", l));

                return ItemStackCreator.getStack(I18n.string("gui_bazaar.order_options.cancel_order", l), Material.RED_DYE, 1, lore);
            }
        });

        set(new GUIClickableItem(24) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIBazaarItem(itemType).open(p);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                return ItemStackCreator.getStack(I18n.string("gui_bazaar.order_options.view_market", l), Material.EMERALD, 1,
                        I18n.lore("gui_bazaar.order_options.view_market.lore", l, Map.of("item_name", itemType.getDisplayName())));
            }
        });
    }

    private void setupToggleButton() {
        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                var completedTransactions = player.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                        DatapointCompletedBazaarTransactions.class
                ).getValue();

                var unclaimedForThisOrder = completedTransactions.getUnclaimedTransactions().stream()
                        .filter(tx -> order.orderId().equals(tx.getRelatedOrderId()))
                        .toList();

                if (!unclaimedForThisOrder.isEmpty()) {
                    new GUIBazaarOrderCompletedOptions(unclaimedForThisOrder, order).open(p);
                } else {
                    p.sendMessage(I18n.string("gui_bazaar.order_options.no_completed_message", p.getLocale()));
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                Locale l = p.getLocale();
                var completedTransactions = player.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                        DatapointCompletedBazaarTransactions.class
                ).getValue();

                var unclaimedForThisOrder = completedTransactions.getUnclaimedTransactions().stream()
                        .filter(tx -> order.orderId().equals(tx.getRelatedOrderId()))
                        .toList();

                if (unclaimedForThisOrder.isEmpty()) {
                    return ItemStackCreator.getStack(I18n.string("gui_bazaar.order_options.no_completed", l), Material.GRAY_DYE, 1,
                            I18n.lore("gui_bazaar.order_options.no_completed.lore", l));
                } else {
                    double totalValue = unclaimedForThisOrder.stream()
                            .mapToDouble(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getTotalValue)
                            .sum();

                    return ItemStackCreator.getStack(I18n.string("gui_bazaar.order_options.view_completed", l), Material.CHEST, 1,
                            I18n.lore("gui_bazaar.order_options.view_completed.lore", l, Map.of(
                                    "count", String.valueOf(unclaimedForThisOrder.size()),
                                    "suffix", unclaimedForThisOrder.size() == 1 ? "" : "s",
                                    "amount", FORMATTER.format(Math.abs(totalValue))
                            )));
                }
            }
        });
    }

    private List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction> getRelatedTransactions(SkyBlockPlayer p) {
        var completedTransactions = p.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                DatapointCompletedBazaarTransactions.class
        ).getValue();

        return completedTransactions.getTransactions().stream()
                .filter(tx -> order.orderId().equals(tx.getRelatedOrderId()))
                .filter(tx -> order.itemName().equals(tx.getItemName()))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .toList();
    }

    private double getOriginalQuantity(SkyBlockPlayer p) {
        var transactions = getRelatedTransactions(p);
        double filledQty = transactions.stream()
                .mapToDouble(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getQuantity)
                .sum();
        return order.amount() + filledQty;
    }

    private double getFilledQuantity(List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction> transactions) {
        return transactions.stream()
                .mapToDouble(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getQuantity)
                .sum();
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