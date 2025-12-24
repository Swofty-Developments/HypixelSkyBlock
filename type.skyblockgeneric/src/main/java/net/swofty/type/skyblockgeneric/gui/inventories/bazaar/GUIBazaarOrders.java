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
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GUIBazaarOrders extends HypixelInventoryGUI {
    private static final int[] SELL_SLOTS = {10, 11, 12, 13, 14, 15, 16};
    private static final int[] BUY_SLOTS = {19, 20, 21, 22, 23, 24, 25};
    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.##");

    public GUIBazaarOrders() {
        super("§8Co-op Bazaar Orders", InventoryType.CHEST_4_ROW);
        fill(ItemStackCreator.createNamedItemStack(Material.GRAY_STAINED_GLASS_PANE));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§6§lSELL ORDERS", Material.GOLD_INGOT, 1,
                        "§7Your active sell orders");
            }
        });

        set(new GUIItem(31) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§a§lBUY ORDERS", Material.EMERALD, 1,
                        "§7Your active buy orders");
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        ((SkyBlockPlayer) e.player()).getBazaarConnector().getPendingOrders()
                .thenAccept(orders -> loadAndDisplayOrders(((SkyBlockPlayer) e.player()), orders));
    }

    private void loadAndDisplayOrders(SkyBlockPlayer player, List<BazaarConnector.BazaarOrder> activeOrders) {
        var completedTransactions = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.COMPLETED_BAZAAR_TRANSACTIONS,
                DatapointCompletedBazaarTransactions.class
        ).getValue();

        var unclaimedTransactions = completedTransactions.getUnclaimedTransactions();

        Map<UUID, List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction>> groupedCompletions =
                unclaimedTransactions.stream()
                        .filter(tx -> tx.getRelatedOrderId() != null)
                        .collect(Collectors.groupingBy(DatapointCompletedBazaarTransactions.CompletedBazaarTransaction::getRelatedOrderId));

        List<OrderDisplayItem> displayItems = new ArrayList<>();

        for (var activeOrder : activeOrders) {
            var completions = groupedCompletions.get(activeOrder.orderId());
            if (completions != null && !completions.isEmpty()) {
                displayItems.add(new OrderDisplayItem(activeOrder, completions, true));
                displayItems.add(new OrderDisplayItem(activeOrder, null, false));
            } else {
                displayItems.add(new OrderDisplayItem(activeOrder, null, false));
            }
        }

        for (var entry : groupedCompletions.entrySet()) {
            UUID orderId = entry.getKey();
            var completions = entry.getValue();

            boolean hasActiveOrder = activeOrders.stream()
                    .anyMatch(order -> order.orderId().equals(orderId));

            if (!hasActiveOrder) {
                displayItems.add(new OrderDisplayItem(null, completions, true));
            }
        }

        updateOrderDisplay(displayItems);
    }

    private void updateOrderDisplay(List<OrderDisplayItem> items) {
        clearSlots();

        int sellIndex = 0, buyIndex = 0;

        for (OrderDisplayItem item : items) {
            boolean isSell = item.isSellOrder();
            int[] slots = isSell ? SELL_SLOTS : BUY_SLOTS;
            int index = isSell ? sellIndex++ : buyIndex++;

            if (index >= slots.length) break;

            int slot = slots[index];
            set(createOrderItem(slot, item));
        }

        if (sellIndex == 0) {
            set(new GUIItem(SELL_SLOTS[0]) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§7No Sell Orders", Material.BARRIER, 1,
                            "§7You don't have any active",
                            "§7sell orders in the Bazaar.");
                }
            });
        }

        if (buyIndex == 0) {
            set(new GUIItem(BUY_SLOTS[0]) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack("§7No Buy Orders", Material.BARRIER, 1,
                            "§7You don't have any active",
                            "§7buy orders in the Bazaar.");
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    private GUIClickableItem createOrderItem(int slot, OrderDisplayItem item) {
        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (item.isCompleted()) {
                    new GUIBazaarOrderCompletedOptions(item.getCompletions(), item.getActiveOrder()).open(p);
                } else {
                    new GUIBazaarOrderOptions(item.getActiveOrder()).open(p);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return item.createDisplayItem();
            }
        };
    }

    private void clearSlots() {
        for (int slot : SELL_SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStack.builder(Material.AIR);
                }
            });
        }
        for (int slot : BUY_SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStack.builder(Material.AIR);
                }
            });
        }
    }

    private static class OrderDisplayItem {
        private final BazaarConnector.BazaarOrder activeOrder;
        private final List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction> completions;
        private final boolean isCompleted;

        public OrderDisplayItem(BazaarConnector.BazaarOrder activeOrder,
                                List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction> completions,
                                boolean isCompleted) {
            this.activeOrder = activeOrder;
            this.completions = completions;
            this.isCompleted = isCompleted;
        }

        public boolean isSellOrder() {
            if (activeOrder != null) {
                return activeOrder.side() == BazaarConnector.OrderSide.SELL;
            } else if (completions != null && !completions.isEmpty()) {
                var firstCompletion = completions.getFirst();
                return firstCompletion.getType() == DatapointCompletedBazaarTransactions.TransactionType.SELL_COMPLETED ||
                        firstCompletion.getType() == DatapointCompletedBazaarTransactions.TransactionType.SELL_ORDER_EXPIRED;
            }
            return false;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public BazaarConnector.BazaarOrder getActiveOrder() {
            return activeOrder;
        }

        public List<DatapointCompletedBazaarTransactions.CompletedBazaarTransaction> getCompletions() {
            return completions;
        }

        public ItemStack.Builder createDisplayItem() {
            if (isCompleted && completions != null && !completions.isEmpty()) {
                return createCompletedOrderDisplay();
            } else if (activeOrder != null) {
                return createActiveOrderDisplay();
            }
            return ItemStack.builder(Material.AIR);
        }

        private ItemStack.Builder createCompletedOrderDisplay() {
            var firstCompletion = completions.getFirst();
            String itemName = firstCompletion.getItemName();
            ItemType itemType;

            try {
                itemType = ItemType.valueOf(itemName);
            } catch (IllegalArgumentException e) {
                itemType = ItemType.STONE;
            }

            List<String> lore = new ArrayList<>();
            boolean isSell = isSellOrder();

            // Single-pass calculation for better performance
            double totalQuantity = 0;
            double totalValue = 0;
            double totalRefund = 0;
            for (var completion : completions) {
                totalQuantity += completion.getQuantity();
                totalValue += completion.getTotalValue();
                totalRefund += completion.getSecondaryAmount();
            }

            lore.add("§a§l✓ COMPLETED");
            lore.add("§8Ready to claim!");
            lore.add(" ");
            lore.add("§7Completed amount: §a" + (int)totalQuantity + "§8x");
            lore.add("§7Total value: §6" + FORMATTER.format(Math.abs(totalValue)) + " coins");
            lore.add(" ");

            lore.add("§7You will receive:");
            if (isSell) {
                lore.add("  §6+" + FORMATTER.format(Math.abs(totalValue)) + " coins");
            } else {
                lore.add("  §a+" + (int)totalQuantity + "x " + itemType.getDisplayName());
                if (totalValue > 0) {
                    lore.add("  §6+" + FORMATTER.format(totalRefund) + " coins refund");
                }
            }

            lore.add(" ");
            lore.add("§eClick to claim rewards!");

            return ItemStackCreator.getStack(
                    "§a§l" + (isSell ? "SELL" : "BUY") + " §f" + itemType.getDisplayName(),
                    itemType.material,
                    Math.max(1, (int)totalQuantity),
                    lore
            );
        }

        private ItemStack.Builder createActiveOrderDisplay() {
            List<String> lore = new ArrayList<>();
            boolean isSell = activeOrder.side() == BazaarConnector.OrderSide.SELL;
            ItemType itemType = activeOrder.getItemType();

            lore.add("§8Worth " + FORMATTER.format(activeOrder.getTotalValue()) + " coins");
            lore.add(" ");
            lore.add("§7Order amount: §a" + (int)activeOrder.amount() + "§8x");
            lore.add(" ");
            lore.add("§7Price per unit: §6" + FORMATTER.format(activeOrder.price()) + " coins");
            lore.add(" ");
            lore.add("§eClick to view options!");

            return ItemStackCreator.getStack(
                    (isSell ? "§6§l" : "§a§l") + activeOrder.side() + " §f" + itemType.getDisplayName(),
                    itemType.material,
                    1,
                    lore
            );
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