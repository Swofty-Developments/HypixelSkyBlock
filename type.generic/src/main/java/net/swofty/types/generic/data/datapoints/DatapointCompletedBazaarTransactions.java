package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatapointCompletedBazaarTransactions extends Datapoint<DatapointCompletedBazaarTransactions.PlayerCompletedBazaarTransactions> {

    private static final Serializer<PlayerCompletedBazaarTransactions> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerCompletedBazaarTransactions value) {
            JSONArray jsonArray = new JSONArray();

            for (CompletedBazaarTransaction transaction : value.getTransactions()) {
                JSONObject transactionObj = new JSONObject();
                transactionObj.put("id", transaction.getId());
                transactionObj.put("type", transaction.getType().name());
                transactionObj.put("itemName", transaction.getItemName());
                transactionObj.put("quantity", transaction.getQuantity());
                transactionObj.put("pricePerUnit", transaction.getPricePerUnit());
                transactionObj.put("taxTaken", transaction.getTaxTaken());
                transactionObj.put("timestamp", transaction.getTimestamp().toString());
                transactionObj.put("claimed", transaction.isClaimed());

                jsonArray.put(transactionObj);
            }

            return jsonArray.toString();
        }

        @Override
        public PlayerCompletedBazaarTransactions deserialize(String json) {
            List<CompletedBazaarTransaction> transactions = new ArrayList<>();

            if (json == null || json.isEmpty()) {
                return new PlayerCompletedBazaarTransactions(transactions);
            }

            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject transactionObj = jsonArray.getJSONObject(i);

                    CompletedBazaarTransaction transaction = new CompletedBazaarTransaction(
                            transactionObj.getString("id"),
                            TransactionType.valueOf(transactionObj.getString("type")),
                            transactionObj.getString("itemName"),
                            transactionObj.getDouble("quantity"),
                            transactionObj.getDouble("pricePerUnit"),
                            transactionObj.optDouble("taxTaken", 0.0),
                            Instant.parse(transactionObj.getString("timestamp")),
                            transactionObj.getBoolean("claimed")
                    );

                    transactions.add(transaction);
                }
            } catch (Exception e) {
                System.err.println("Failed to deserialize completed bazaar transactions: " + e.getMessage());
                return new PlayerCompletedBazaarTransactions(new ArrayList<>());
            }

            return new PlayerCompletedBazaarTransactions(transactions);
        }

        @Override
        public PlayerCompletedBazaarTransactions clone(PlayerCompletedBazaarTransactions value) {
            List<CompletedBazaarTransaction> clonedTransactions = new ArrayList<>();
            for (CompletedBazaarTransaction transaction : value.getTransactions()) {
                clonedTransactions.add(new CompletedBazaarTransaction(
                        transaction.getId(),
                        transaction.getType(),
                        transaction.getItemName(),
                        transaction.getQuantity(),
                        transaction.getPricePerUnit(),
                        transaction.getTaxTaken(),
                        transaction.getTimestamp(),
                        transaction.isClaimed()
                ));
            }
            return new PlayerCompletedBazaarTransactions(clonedTransactions);
        }
    };

    public DatapointCompletedBazaarTransactions(String key, PlayerCompletedBazaarTransactions value) {
        super(key, value, serializer);
    }

    public DatapointCompletedBazaarTransactions(String key) {
        super(key, new PlayerCompletedBazaarTransactions(), serializer);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerCompletedBazaarTransactions {
        private List<CompletedBazaarTransaction> transactions = new ArrayList<>();

        /**
         * Add a new completed transaction (unclaimed by default)
         */
        public void addTransaction(CompletedBazaarTransaction transaction) {
            transactions.add(transaction);
        }

        /**
         * Get all unclaimed transactions
         */
        public List<CompletedBazaarTransaction> getUnclaimedTransactions() {
            return transactions.stream()
                    .filter(transaction -> !transaction.isClaimed())
                    .toList();
        }

        /**
         * Get all claimed transactions
         */
        public List<CompletedBazaarTransaction> getClaimedTransactions() {
            return transactions.stream()
                    .filter(CompletedBazaarTransaction::isClaimed)
                    .toList();
        }

        /**
         * Mark a transaction as claimed by its ID
         */
        public boolean claimTransaction(String transactionId) {
            for (CompletedBazaarTransaction transaction : transactions) {
                if (transaction.getId().equals(transactionId)) {
                    transaction.setClaimed(true);
                    return true;
                }
            }
            return false;
        }

        /**
         * Mark multiple transactions as claimed
         */
        public int claimTransactions(List<String> transactionIds) {
            int claimedCount = 0;
            for (String id : transactionIds) {
                if (claimTransaction(id)) {
                    claimedCount++;
                }
            }
            return claimedCount;
        }

        /**
         * Get a transaction by its ID
         */
        public CompletedBazaarTransaction getTransactionById(String id) {
            return transactions.stream()
                    .filter(transaction -> transaction.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }

        /**
         * Remove old claimed transactions (for cleanup)
         */
        public void removeClaimedTransactionsOlderThan(Instant cutoff) {
            transactions.removeIf(transaction ->
                    transaction.isClaimed() && transaction.getTimestamp().isBefore(cutoff));
        }

        /**
         * Get count of unclaimed transactions
         */
        public int getUnclaimedCount() {
            return (int) transactions.stream().filter(transaction -> !transaction.isClaimed()).count();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompletedBazaarTransaction {
        private String id;
        private TransactionType type;
        private String itemName;
        private double quantity;
        private double pricePerUnit;
        private double taxTaken; // For expired buy orders, this stores the original price paid
        private Instant timestamp;
        private boolean claimed;

        /**
         * Create a successful buy transaction
         */
        public static CompletedBazaarTransaction createBuyTransaction(String itemName, double quantity, double pricePerUnit) {
            return new CompletedBazaarTransaction(
                    UUID.randomUUID().toString(),
                    TransactionType.BUY_COMPLETED,
                    itemName,
                    quantity,
                    pricePerUnit,
                    0.0,
                    Instant.now(),
                    false
            );
        }

        /**
         * Create a successful sell transaction
         */
        public static CompletedBazaarTransaction createSellTransaction(String itemName, double quantity, double pricePerUnit, double taxTaken) {
            return new CompletedBazaarTransaction(
                    UUID.randomUUID().toString(),
                    TransactionType.SELL_COMPLETED,
                    itemName,
                    quantity,
                    pricePerUnit,
                    taxTaken,
                    Instant.now(),
                    false
            );
        }

        /**
         * Create an expired buy order (refund coins)
         */
        public static CompletedBazaarTransaction createExpiredBuyOrder(String itemName, double quantity, double originalPricePaid) {
            return new CompletedBazaarTransaction(
                    UUID.randomUUID().toString(),
                    TransactionType.BUY_ORDER_EXPIRED,
                    itemName,
                    quantity,
                    originalPricePaid, // Store original price in pricePerUnit for display
                    originalPricePaid, // Store in taxTaken field for refund calculation
                    Instant.now(),
                    false
            );
        }

        /**
         * Create an expired sell order (return items)
         */
        public static CompletedBazaarTransaction createExpiredSellOrder(String itemName, double quantity) {
            return new CompletedBazaarTransaction(
                    UUID.randomUUID().toString(),
                    TransactionType.SELL_ORDER_EXPIRED,
                    itemName,
                    quantity,
                    0.0, // No price for returned items
                    0.0,
                    Instant.now(),
                    false
            );
        }

        /**
         * Get the total value of this transaction
         */
        public double getTotalValue() {
            return switch (type) {
                case BUY_COMPLETED -> pricePerUnit * quantity; // Total cost
                case SELL_COMPLETED -> (pricePerUnit * quantity) - taxTaken; // Net earnings
                case BUY_ORDER_EXPIRED -> taxTaken; // Refund amount
                case SELL_ORDER_EXPIRED -> 0.0; // Items returned, no monetary value
            };
        }

        /**
         * Get a human-readable description
         */
        public String getDescription() {
            return switch (type) {
                case BUY_COMPLETED -> String.format("Bought %.0fx %s for %.2f coins each", quantity, itemName, pricePerUnit);
                case SELL_COMPLETED -> String.format("Sold %.0fx %s for %.2f coins each", quantity, itemName, pricePerUnit);
                case BUY_ORDER_EXPIRED -> String.format("Buy order expired: %.0fx %s (refund %.2f coins)", quantity, itemName, taxTaken);
                case SELL_ORDER_EXPIRED -> String.format("Sell order expired: %.0fx %s returned", quantity, itemName);
            };
        }

        /**
         * Check if this transaction gives items to the player
         */
        public boolean givesItems() {
            return type == TransactionType.BUY_COMPLETED || type == TransactionType.SELL_ORDER_EXPIRED;
        }

        /**
         * Check if this transaction gives coins to the player
         */
        public boolean givesCoins() {
            return type == TransactionType.SELL_COMPLETED || type == TransactionType.BUY_ORDER_EXPIRED;
        }
    }

    public enum TransactionType {
        BUY_COMPLETED,      // Player bought items (receive items)
        SELL_COMPLETED,     // Player sold items (receive coins)
        BUY_ORDER_EXPIRED,  // Buy order expired (receive coin refund)
        SELL_ORDER_EXPIRED  // Sell order expired (receive items back)
    }
}