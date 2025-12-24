package net.swofty.type.skyblockgeneric.gui.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
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

public class GUIBazaarOrderOptions extends HypixelInventoryGUI {
    private final BazaarConnector.BazaarOrder order;
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
            .withZone(ZoneId.systemDefault());

    public GUIBazaarOrderOptions(BazaarConnector.BazaarOrder order) {
        super("Order Details - " + order.getItemType().getDisplayName(), InventoryType.CHEST_4_ROW);
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
                List<String> lore = new ArrayList<>();

                var relatedTransactions = getRelatedTransactions(player);
                double originalQuantity = getOriginalQuantity(player);
                double filledQuantity = getFilledQuantity(relatedTransactions);

                lore.add("§8" + (isSell ? "Sell" : "Buy") + " Order");
                lore.add(" ");
                lore.add("§7Original Amount: §e" + (int) originalQuantity + "§8x");
                lore.add("§7Remaining Amount: §a" + (int) order.amount() + "§8x");
                lore.add("§7Price per unit: §6" + FORMATTER.format(order.price()) + " coins");
                lore.add(" ");

                if (filledQuantity > 0) {
                    lore.add("§a§l✓ " + (int) filledQuantity + " items filled");
                    lore.add("§7Progress: §a" + String.format("%.1f%%", (filledQuantity / originalQuantity) * 100));
                } else {
                    lore.add("§7§l○ No fills yet");
                    lore.add("§7Progress: §70.0%");
                }

                lore.add(" ");
                lore.add("§7Order ID: §8" + order.orderId().toString().substring(0, 8) + "...");

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
                List<String> lore = new ArrayList<>();
                lore.add("§8Transaction History");
                lore.add(" ");

                var transactions = getRelatedTransactions(player);
                if (transactions.isEmpty()) {
                    lore.add("§7No transactions yet");
                    lore.add("§7Your order is still pending");
                } else {
                    lore.add("§a" + transactions.size() + " transaction" + (transactions.size() == 1 ? "" : "s"));
                    lore.add(" ");

                    int count = 0;
                    for (var tx : transactions) {
                        if (count >= 5) {
                            lore.add("§8... and " + (transactions.size() - 5) + " more");
                            break;
                        }

                        String timeStr = TIME_FORMATTER.format(tx.getTimestamp());
                        if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.BUY_COMPLETED) {
                            lore.add("§a▲ " + (int) tx.getQuantity() + "x at §6" +
                                    FORMATTER.format(tx.getPricePerUnit()) + " §8(" + timeStr + ")");
                            if (tx.getSecondaryAmount() > 0) {
                                lore.add("  §7Saved: §a" + FORMATTER.format(tx.getSecondaryAmount()) + " coins");
                            }
                        } else if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.SELL_COMPLETED) {
                            lore.add("§6▼ " + (int) tx.getQuantity() + "x at §6" +
                                    FORMATTER.format(tx.getPricePerUnit()) + " §8(" + timeStr + ")");
                            lore.add("  §7Tax: §c-" + FORMATTER.format(tx.getSecondaryAmount()) + " coins");
                        }
                        count++;
                    }
                }

                return ItemStackCreator.getStack("§eTransaction History", Material.BOOK, 1, lore);
            }
        });
    }

    private void setupFinancialSummary() {
        boolean isSell = order.side() == BazaarConnector.OrderSide.SELL;

        set(new GUIItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                lore.add("§8Financial Summary");
                lore.add(" ");

                var transactions = getRelatedTransactions(player);
                double originalValue = getOriginalQuantity(player) * order.price();
                double currentValue = order.amount() * order.price();

                lore.add("§7Original Order Value:");
                lore.add("  §6" + FORMATTER.format(originalValue) + " coins");
                lore.add(" ");
                lore.add("§7Remaining Order Value:");
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

                        lore.add("§7Transactions Completed:");
                        lore.add("  §aGross: §6+" + FORMATTER.format(totalEarned) + " coins");
                        lore.add("  §cTax: §6-" + FORMATTER.format(totalTax) + " coins");
                        lore.add("  §aNet: §6+" + FORMATTER.format(netEarned) + " coins");
                        lore.add(" ");
                        lore.add("§7Average sell price:");
                        lore.add("  §6" + FORMATTER.format(totalEarned / getFilledQuantity(transactions)) + " coins/item");
                    } else {
                        double totalSpent = transactions.stream()
                                .mapToDouble(tx -> tx.getPricePerUnit() * tx.getQuantity())
                                .sum();
                        double totalSaved = transactions.stream()
                                .mapToDouble(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getSecondaryAmount)
                                .sum();

                        lore.add("§7Transactions Completed:");
                        lore.add("  §cSpent: §6-" + FORMATTER.format(totalSpent) + " coins");
                        lore.add("  §aSaved: §6+" + FORMATTER.format(totalSaved) + " coins");
                        lore.add(" ");
                        lore.add("§7Average buy price:");
                        lore.add("  §6" + FORMATTER.format(totalSpent / getFilledQuantity(transactions)) + " coins/item");
                        lore.add("§7vs your bid of §6" + FORMATTER.format(order.price()) + " coins/item");
                    }
                }

                return ItemStackCreator.getStack("§6Financial Summary", Material.GOLD_INGOT, 1, lore);
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
                p.sendMessage("§6[Bazaar] §7Cancelling order...");

                player.getBazaarConnector().cancelOrder(order.orderId())
                        .thenAccept(success -> {
                            if (success) {
                                p.sendMessage("§6[Bazaar] §aOrder cancelled successfully!");

                                if (isSell) {
                                    SkyBlockItem item = new SkyBlockItem(order.getItemType());
                                    item.setAmount((int) order.amount());
                                    player.addAndUpdateItem(item);
                                    p.sendMessage("§6[Bazaar] §7Returned §a" + (int) order.amount() +
                                            "x " + itemType.getDisplayName() + " §7to your inventory.");
                                } else {
                                    double refund = order.price() * order.amount();
                                    player.addCoins(refund);
                                    p.sendMessage("§6[Bazaar] §7Refunded §6" + FORMATTER.format(refund) +
                                            " coins §7to your wallet.");
                                }

                                new GUIBazaarOrders().open(p);
                            } else {
                                p.sendMessage("§6[Bazaar] §cFailed to cancel order. It may have already been filled or expired.");
                            }
                        });
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                lore.add("§7Cancel this bazaar order and");
                lore.add("§7receive back your remaining:");
                lore.add(" ");

                if (isSell) {
                    lore.add("§a▶ " + (int) order.amount() + "x " + itemType.getDisplayName());
                } else {
                    lore.add("§6▶ " + FORMATTER.format(order.amount() * order.price()) + " coins");
                }

                lore.add(" ");
                lore.add("§cThis action cannot be undone!");
                lore.add("§7Any completed transactions will");
                lore.add("§7remain completed.");
                lore.add(" ");
                lore.add("§eClick to cancel!");

                return ItemStackCreator.getStack("§cCancel Order", Material.RED_DYE, 1, lore);
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
                return ItemStackCreator.getStack("§aView Market", Material.EMERALD, 1,
                        "§7View the current market for",
                        "§a" + itemType.getDisplayName(),
                        " ",
                        "§7See current buy/sell orders,",
                        "§7price trends, and market activity.",
                        " ",
                        "§eClick to view!");
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
                    p.sendMessage("§6[Bazaar] §7No completed transactions to view for this order.");
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                var completedTransactions = player.getSkyblockDataHandler().get(
                        SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                        DatapointCompletedBazaarTransactions.class
                ).getValue();

                var unclaimedForThisOrder = completedTransactions.getUnclaimedTransactions().stream()
                        .filter(tx -> order.orderId().equals(tx.getRelatedOrderId()))
                        .toList();

                if (unclaimedForThisOrder.isEmpty()) {
                    return ItemStackCreator.getStack("§7No Completed Transactions", Material.GRAY_DYE, 1,
                            "§7This order has no completed",
                            "§7transactions ready to claim yet.",
                            " ",
                            "§7Once parts of your order are",
                            "§7filled, you can view and claim",
                            "§7them here.");
                } else {
                    double totalValue = unclaimedForThisOrder.stream()
                            .mapToDouble(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getTotalValue)
                            .sum();

                    return ItemStackCreator.getStack("§aView Completed Transactions", Material.CHEST, 1,
                            "§7Switch to view completed",
                            "§7transactions for this order.",
                            " ",
                            "§7Ready to claim: §e" + unclaimedForThisOrder.size() + " transaction" +
                                    (unclaimedForThisOrder.size() == 1 ? "" : "s"),
                            "§7Total value: §6" + FORMATTER.format(Math.abs(totalValue)) + " coins",
                            " ",
                            "§eClick to view!");
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