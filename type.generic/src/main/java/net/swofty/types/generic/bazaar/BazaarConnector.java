package net.swofty.types.generic.bazaar;

import net.swofty.commons.ServiceType;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.bazaar.*;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BazaarConnector {
    private final SkyBlockPlayer player;
    private final ProxyService bazaarService;

    public BazaarConnector(SkyBlockPlayer player) {
        this.player = player;
        this.bazaarService = new ProxyService(ServiceType.BAZAAR);
    }

    /**
     * Get the player's current profile UUID for bazaar operations
     */
    private UUID getPlayerProfileUUID() {
        return player.getProfiles().getCurrentlySelected();
    }

    // ========== ORDER MANAGEMENT ==========

    /**
     * Get all pending orders for the current player and profile
     */
    public CompletableFuture<List<BazaarOrder>> getPendingOrders() {
        var message = new BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage(
                player.getUuid(),
                getPlayerProfileUUID()
        );

        return bazaarService.<BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage,
                        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse>handleRequest(message)
                .thenApply(response -> response.orders.stream()
                        .map(order -> new BazaarOrder(
                                order.orderId(),
                                order.itemName(),
                                OrderSide.valueOf(order.side()),
                                order.price(),
                                order.amount(),
                                order.profileUUID()
                        ))
                        .toList());
    }

    /**
     * Cancel a specific order by ID
     */
    public CompletableFuture<Boolean> cancelOrder(UUID orderId) {
        var message = new BazaarCancelProtocolObject.CancelMessage(
                orderId,
                player.getUuid(),
                getPlayerProfileUUID()
        );

        return bazaarService.<BazaarCancelProtocolObject.CancelMessage,
                        BazaarCancelProtocolObject.CancelResponse>handleRequest(message)
                .thenApply(response -> response.successful);
    }

    // ========== PENDING TRANSACTIONS ==========

    /**
     * Get all pending transactions for the current player and profile
     */
    public CompletableFuture<List<PendingTransaction>> getPendingTransactions() {
        var message = new BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsMessage(
                player.getUuid(),
                getPlayerProfileUUID()
        );

        return bazaarService.<BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsMessage,
                        BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsResponse>handleRequest(message)
                .thenApply(response -> response.transactions.stream()
                        .map(txInfo -> new PendingTransaction(
                                txInfo.id,
                                txInfo.transactionType,
                                txInfo.transactionData,
                                txInfo.createdAt
                        ))
                        .collect(Collectors.toList()));
    }

    /**
     * Process pending transactions using BazaarAwarder and mark them as completed
     */
    public CompletableFuture<TransactionProcessResult> processPendingTransactions(List<String> transactionIds) {
        var message = new BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage(
                player.getUuid(),
                getPlayerProfileUUID(),
                transactionIds
        );

        return bazaarService.<BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage,
                        BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsResponse>handleRequest(message)
                .thenApply(response -> new TransactionProcessResult(
                        response.processedCount,
                        response.failedCount,
                        response.successfulTransactionIds,
                        response.failedTransactionIds
                ));
    }

    /**
     * Process all pending transactions for this player using BazaarAwarder
     */
    public CompletableFuture<TransactionProcessResult> processAllPendingTransactions() {
        return getPendingTransactions().thenCompose(transactions -> {
            if (transactions.isEmpty()) {
                return CompletableFuture.completedFuture(
                        new TransactionProcessResult(0, 0, List.of(), List.of())
                );
            }

            player.sendMessage("§6[Bazaar] §7Processing " + transactions.size() + " pending transaction" +
                    (transactions.size() == 1 ? "" : "s") + "...");

            // Process each transaction using BazaarAwarder
            List<String> successfulIds = new java.util.ArrayList<>();
            List<String> failedIds = new java.util.ArrayList<>();

            for (PendingTransaction tx : transactions) {
                try {
                    boolean processed = BazaarAwarder.processPendingTransaction(
                            player,
                            tx.transactionType(),
                            tx.transactionData()
                    );

                    if (processed) {
                        successfulIds.add(tx.id());
                    } else {
                        failedIds.add(tx.id());
                    }
                } catch (Exception e) {
                    System.err.println("Failed to process pending transaction " + tx.id() + ": " + e.getMessage());
                    failedIds.add(tx.id());
                }
            }

            // Mark processed transactions as completed and send final message
            CompletableFuture<TransactionProcessResult> result;
            if (!successfulIds.isEmpty()) {
                result = processPendingTransactions(successfulIds)
                        .thenApply(dbResult -> new TransactionProcessResult(
                                successfulIds.size(),
                                failedIds.size(),
                                successfulIds,
                                failedIds
                        ));
            } else {
                result = CompletableFuture.completedFuture(
                        new TransactionProcessResult(0, failedIds.size(), List.of(), failedIds)
                );
            }

            return result.thenApply(finalResult -> {
                // Send completion message
                if (finalResult.isFullySuccessful()) {
                    player.sendMessage("§6[Bazaar] §8All pending transactions processed successfully!");
                } else if (finalResult.hasFailures()) {
                    player.sendMessage("§6[Bazaar] §eProcessed " + finalResult.processedCount() +
                            " transaction" + (finalResult.processedCount() == 1 ? "" : "s") +
                            ", " + finalResult.failedCount() + " failed.");
                } else if (finalResult.processedCount() > 0) {
                    player.sendMessage("§6[Bazaar] §aProcessed " + finalResult.processedCount() +
                            " pending transaction" + (finalResult.processedCount() == 1 ? "" : "s") + "!");
                }
                return finalResult;
            });
        });
    }

    // ========== ITEM DATA ==========

    /**
     * Get current market data for a specific item
     */
    public CompletableFuture<BazaarItemData> getItemData(ItemType itemType) {
        var message = new BazaarGetItemProtocolObject.BazaarGetItemMessage(itemType.name());

        return bazaarService.<BazaarGetItemProtocolObject.BazaarGetItemMessage,
                        BazaarGetItemProtocolObject.BazaarGetItemResponse>handleRequest(message)
                .thenApply(response -> {
                    var buyOrders = response.buyOrders().stream()
                            .map(order -> new MarketOrder(
                                    order.playerUUID(),
                                    order.profileUUID(),
                                    order.price(),
                                    order.amount()
                            ))
                            .toList();

                    var sellOrders = response.sellOrders().stream()
                            .map(order -> new MarketOrder(
                                    order.playerUUID(),
                                    order.profileUUID(),
                                    order.price(),
                                    order.amount()
                            ))
                            .toList();

                    return new BazaarItemData(response.itemName(), buyOrders, sellOrders);
                });
    }

    /**
     * Get market statistics for an item (best prices, averages, etc.)
     */
    public CompletableFuture<BazaarStatistics> getItemStatistics(ItemType itemType) {
        return getItemData(itemType).thenApply(data -> {
            var buyStats = calculateBuyStatistics(data.buyOrders());
            var sellStats = calculateSellStatistics(data.sellOrders());

            return new BazaarStatistics(
                    buyStats.highestPrice(),    // Best bid
                    buyStats.lowestPrice(),     // Worst bid
                    buyStats.averagePrice(),    // Average bid
                    sellStats.lowestPrice(),    // Best ask
                    sellStats.highestPrice(),   // Worst ask
                    sellStats.averagePrice()    // Average ask
            );
        });
    }

    // ========== TRADING OPERATIONS ==========

    /**
     * Create a buy order for the specified item
     */
    public CompletableFuture<BazaarResult> createBuyOrder(ItemType itemType, double pricePerUnit, int quantity) {
        var message = new BazaarBuyProtocolObject.BazaarBuyMessage(
                itemType.name(),
                quantity,
                (int) Math.ceil(pricePerUnit),
                player.getUuid(),
                getPlayerProfileUUID()
        );

        return bazaarService.<BazaarBuyProtocolObject.BazaarBuyMessage,
                        BazaarBuyProtocolObject.BazaarBuyResponse>handleRequest(message)
                .thenApply(response -> new BazaarResult(
                        response.successful,
                        response.successful
                                ? "Buy order created successfully!"
                                : "Failed to create buy order"
                ));
    }

    /**
     * Create a sell order for the specified item
     */
    public CompletableFuture<BazaarResult> createSellOrder(ItemType itemType, double pricePerUnit, int quantity) {
        var message = new BazaarSellProtocolObject.BazaarSellMessage(
                itemType.name(),
                player.getUuid(),
                getPlayerProfileUUID(),
                pricePerUnit,
                quantity
        );

        return bazaarService.<BazaarSellProtocolObject.BazaarSellMessage,
                        BazaarSellProtocolObject.BazaarSellResponse>handleRequest(message)
                .thenApply(response -> new BazaarResult(
                        response.successful,
                        response.successful
                                ? "Sell order created successfully!"
                                : "Failed to create sell order"
                ));
    }

    /**
     * Instant buy - purchases items at the current best ask price
     */
    public CompletableFuture<BazaarResult> instantBuy(ItemType itemType, int maxQuantity) {
        return getItemStatistics(itemType).thenCompose(stats -> {
            if (stats.bestAsk() <= 0) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "No sell orders available for instant buy")
                );
            }

            double priceWithFee = stats.bestAsk() * 1.04; // 4% fee
            int actualQuantity = Math.min(maxQuantity, player.maxItemFit(itemType));

            if (actualQuantity <= 0) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "No inventory space available")
                );
            }

            double totalCost = priceWithFee * actualQuantity;
            if (totalCost > player.getCoins()) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "Insufficient coins for instant buy")
                );
            }

            return createBuyOrder(itemType, priceWithFee, actualQuantity);
        });
    }

    /**
     * Instant sell - sells items at the current best bid price
     */
    public CompletableFuture<BazaarResult> instantSell(ItemType itemType) {
        int availableAmount = player.getAmountInInventory(itemType);
        if (availableAmount <= 0) {
            return CompletableFuture.completedFuture(
                    new BazaarResult(false, "You don't have any " + itemType.getDisplayName() + " to sell")
            );
        }

        return getItemStatistics(itemType).thenCompose(stats -> {
            if (stats.bestBid() <= 0) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "No buy orders available for instant sell")
                );
            }

            // Remove items from inventory first
            var takenItems = player.takeItem(itemType, availableAmount);
            if (takenItems == null) {
                return CompletableFuture.completedFuture(
                        new BazaarResult(false, "Failed to remove items from inventory")
                );
            }

            return createSellOrder(itemType, stats.bestBid(), availableAmount);
        });
    }

    // ========== UTILITY METHODS ==========

    /**
     * Check if the bazaar service is online
     */
    public CompletableFuture<Boolean> isOnline() {
        return bazaarService.isOnline();
    }

    /**
     * Calculate statistics for buy orders
     */
    private OrderStatistics calculateBuyStatistics(List<MarketOrder> buyOrders) {
        if (buyOrders.isEmpty()) {
            return new OrderStatistics(0, 0, 0);
        }

        double total = buyOrders.stream().mapToDouble(MarketOrder::price).sum();
        double average = total / buyOrders.size();
        double highest = buyOrders.stream().mapToDouble(MarketOrder::price).max().orElse(0);
        double lowest = buyOrders.stream().mapToDouble(MarketOrder::price).min().orElse(0);

        return new OrderStatistics(highest, lowest, average);
    }

    /**
     * Calculate statistics for sell orders
     */
    private OrderStatistics calculateSellStatistics(List<MarketOrder> sellOrders) {
        if (sellOrders.isEmpty()) {
            return new OrderStatistics(0, 0, 0);
        }

        double total = sellOrders.stream().mapToDouble(MarketOrder::price).sum();
        double average = total / sellOrders.size();
        double highest = sellOrders.stream().mapToDouble(MarketOrder::price).max().orElse(0);
        double lowest = sellOrders.stream().mapToDouble(MarketOrder::price).min().orElse(0);

        return new OrderStatistics(highest, lowest, average);
    }

    // ========== DATA CLASSES ==========

    public enum OrderSide {
        BUY, SELL
    }

    /**
     * Represents a player's bazaar order
     */
    public record BazaarOrder(
            UUID orderId,
            String itemName,
            OrderSide side,
            double price,
            double amount,
            UUID profileUUID
    ) {
        public ItemType getItemType() {
            try {
                return ItemType.valueOf(itemName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public double getTotalValue() {
            return price * amount;
        }
    }

    /**
     * Represents a pending transaction waiting to be processed
     */
    public record PendingTransaction(
            String id,
            String transactionType,
            org.json.JSONObject transactionData,
            java.time.Instant createdAt
    ) {
        public boolean isSuccessfulTransaction() {
            return "SuccessfulBazaarTransaction".equals(transactionType);
        }

        public boolean isExpiredOrderTransaction() {
            return "OrderExpiredBazaarTransaction".equals(transactionType);
        }

        /**
         * Get a human-readable description of this pending transaction using BazaarAwarder logic
         */
        public String getDescription() {
            try {
                if (isSuccessfulTransaction()) {
                    String itemName = transactionData.getString("itemName");
                    double quantity = transactionData.getDouble("quantity");
                    double price = transactionData.getDouble("pricePerUnit");
                    return String.format("Trade completed: %.0fx %s at %.2f coins each",
                            quantity, itemName, price);
                } else if (isExpiredOrderTransaction()) {
                    String itemName = transactionData.getString("itemName");
                    String side = transactionData.getString("side");
                    double quantity = transactionData.getDouble("remainingQty");
                    return String.format("%s order expired: %.0fx %s",
                            side, quantity, itemName);
                }
            } catch (Exception e) {
                // Fall back to generic description if parsing fails
            }
            return "Pending " + transactionType;
        }
    }

    /**
     * Result of processing pending transactions
     */
    public record TransactionProcessResult(
            int processedCount,
            int failedCount,
            List<String> successfulTransactionIds,
            List<String> failedTransactionIds
    ) {
        public boolean hasFailures() {
            return failedCount > 0;
        }

        public boolean isFullySuccessful() {
            return failedCount == 0 && processedCount > 0;
        }
    }

    /**
     * Represents a market order from any player
     */
    public record MarketOrder(
            UUID playerUUID,
            UUID profileUUID,
            double price,
            double amount
    ) {}

    /**
     * Contains all market data for a specific item
     */
    public record BazaarItemData(
            String itemName,
            List<MarketOrder> buyOrders,
            List<MarketOrder> sellOrders
    ) {
        public ItemType getItemType() {
            try {
                return ItemType.valueOf(itemName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public boolean hasBuyOrders() {
            return !buyOrders.isEmpty();
        }

        public boolean hasSellOrders() {
            return !sellOrders.isEmpty();
        }
    }

    /**
     * Contains statistical information about an item's market
     */
    public record BazaarStatistics(
            double bestBid,      // Highest buy order price
            double worstBid,     // Lowest buy order price
            double averageBid,   // Average buy order price
            double bestAsk,      // Lowest sell order price
            double worstAsk,     // Highest sell order price
            double averageAsk    // Average sell order price
    ) {}

    /**
     * Result of a bazaar operation
     */
    public record BazaarResult(
            boolean success,
            String message
    ) {}

    /**
     * Helper record for order statistics
     */
    private record OrderStatistics(
            double highestPrice,
            double lowestPrice,
            double averagePrice
    ) {}
}