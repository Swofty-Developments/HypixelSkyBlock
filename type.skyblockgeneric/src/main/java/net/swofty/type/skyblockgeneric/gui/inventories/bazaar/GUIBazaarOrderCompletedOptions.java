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
        super("Completed Order", InventoryType.CHEST_4_ROW);
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
                List<String> lore = new ArrayList<>();

                lore.add("§a§l✓ ORDER COMPLETED");
                lore.add(" ");
                lore.add("§7Completed: §a" + (int) summary.totalQuantity + "§8x " + finalItemType.getDisplayName());
                lore.add("§7Transactions: §e" + summary.transactionCount);
                lore.add(" ");

                if (isSell) {
                    lore.add("§7Gross earnings: §6+" + FORMATTER.format(summary.totalSpent) + " coins");
                    lore.add("§7Tax paid: §c-" + FORMATTER.format(summary.totalSecondaryAmount) + " coins");
                    lore.add("§7Net earnings: §6+" + FORMATTER.format(Math.abs(summary.totalValue)) + " coins");
                } else {
                    lore.add("§7Total spent: §c-" + FORMATTER.format(summary.totalSpent) + " coins");
                    if (summary.totalSecondaryAmount > 0) {
                        lore.add("§7Total saved: §a+" + FORMATTER.format(summary.totalSecondaryAmount) + " coins");
                        lore.add("§7Refund ready: §6+" + FORMATTER.format(summary.totalSecondaryAmount) + " coins");
                    }
                }

                return ItemStackCreator.getStack(
                        "§a" + finalItemType.getDisplayName() + " Order",
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
                List<String> lore = new ArrayList<>();
                lore.add("§8Transaction Details");
                lore.add(" ");

                int count = 0;
                for (var tx : completions) {
                    if (count >= 10) {
                        lore.add("§8... and " + (completions.size() - 10) + " more");
                        break;
                    }

                    String timeStr = TIME_FORMATTER.format(tx.getTimestamp());

                    if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.BUY_COMPLETED) {
                        lore.add("§a▲ " + (int) tx.getQuantity() + "x at §6" +
                                FORMATTER.format(tx.getPricePerUnit()) + " §8(" + timeStr + ")");
                        if (tx.getSecondaryAmount() > 0) {
                            lore.add("  §7Saved: §a+" + FORMATTER.format(tx.getSecondaryAmount()) + " coins");
                        }
                    } else if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.SELL_COMPLETED) {
                        lore.add("§6▼ " + (int) tx.getQuantity() + "x at §6" +
                                FORMATTER.format(tx.getPricePerUnit()) + " §8(" + timeStr + ")");
                        lore.add("  §7Tax: §c-" + FORMATTER.format(tx.getSecondaryAmount()) + " coins");
                    } else if (tx.getType() == DatapointCompletedBazaarTransactions.TransactionType.REFUND) {
                        lore.add("§e● Refund: §6+" + FORMATTER.format(tx.getSecondaryAmount()) + " coins §8(" + timeStr + ")");
                    }
                    count++;
                }

                return ItemStackCreator.getStack("§eTransaction History", Material.BOOK, 1, lore);
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
                List<String> lore = new ArrayList<>();
                lore.add("§7Claim all rewards from this");
                lore.add("§7completed order:");
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
                lore.add("§eClick to claim!");

                return ItemStackCreator.getStack("§a§lClaim Rewards", Material.CHEST, 1, lore);
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
                return ItemStackCreator.getStack("§eView Unfilled Order", Material.COMPASS, 1,
                        "§7Switch to view the remaining",
                        "§7unfilled portion of this order.",
                        " ",
                        "§7Remaining: §a" + (int) activeOrder.amount() + "x",
                        "§7Price: §6" + FORMATTER.format(activeOrder.price()) + " coins each",
                        " ",
                        "§eClick to switch!");
            }
        });
    }

    private void claimRewards(SkyBlockPlayer player) {
        if (completions == null || completions.isEmpty()) {
            player.sendMessage("§6[Bazaar] §cNo rewards to claim!");
            return;
        }

        boolean isSell = isSellOrder();
        ItemType itemType = getItemType();

        try {
            if (isSell) {
                player.addCoins(Math.abs(summary.totalValue));
                player.sendMessage("§6[Bazaar] §aReceived §6" + FORMATTER.format(Math.abs(summary.totalValue)) + " coins§a!");
            } else {
                SkyBlockItem item = new SkyBlockItem(itemType);
                item.setAmount((int) summary.totalQuantity);
                player.addAndUpdateItem(item);
                player.sendMessage("§6[Bazaar] §aReceived §e" + (int) summary.totalQuantity + "x " + itemType.getDisplayName() + "§a!");

                if (summary.totalSecondaryAmount > 0) {
                    player.addCoins(summary.totalSecondaryAmount);
                    player.sendMessage("§6[Bazaar] §aReceived §6" + FORMATTER.format(summary.totalSecondaryAmount) + " coins §arefund!");
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

            player.sendMessage("§6[Bazaar] §aAll rewards claimed successfully!");
            player.playSuccessSound();

            new GUIBazaarOrders().open(player);

        } catch (Exception e) {
            player.sendMessage("§6[Bazaar] §cFailed to claim rewards: " + e.getMessage());
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