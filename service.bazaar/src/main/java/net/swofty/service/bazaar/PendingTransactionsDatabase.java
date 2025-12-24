package net.swofty.service.bazaar;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.swofty.commons.skyblock.bazaar.BazaarTransaction;
import net.swofty.commons.skyblock.bazaar.OrderExpiredBazaarTransaction;
import net.swofty.commons.skyblock.bazaar.SuccessfulBazaarTransaction;
import org.bson.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles storage and retrieval of pending bazaar transactions for offline players
 */
public class PendingTransactionsDatabase {

    /**
     * Store a pending transaction for a player who is offline or on a different profile
     */
    public static void storePendingTransaction(UUID playerUuid, UUID profileUuid, BazaarTransaction transaction) {
        Document doc = new Document("_id", UUID.randomUUID().toString())
                .append("playerUuid", playerUuid.toString())
                .append("profileUuid", profileUuid.toString())
                .append("transactionType", transaction.getClass().getSimpleName())
                .append("transactionData", Document.parse(transaction.toJSON().toString()))
                .append("createdAt", Instant.now().toString())
                .append("processed", false);

        OrderDatabase.database.getCollection("pendingTransactions").insertOne(doc);
        System.out.println("Stored pending transaction for player " + playerUuid + " on profile " + profileUuid);
    }

    /**
     * Get all pending transactions for a specific player and profile
     */
    public static List<PendingTransaction> getPendingTransactions(UUID playerUuid, UUID profileUuid) {
        List<PendingTransaction> transactions = new ArrayList<>();

        var cursor = OrderDatabase.database.getCollection("pendingTransactions").find(
                Filters.and(
                        Filters.eq("playerUuid", playerUuid.toString()),
                        Filters.eq("profileUuid", profileUuid.toString()),
                        Filters.eq("processed", false)
                )
        );

        for (Document doc : cursor) {
            try {
                String transactionType = doc.getString("transactionType");
                Document transactionData = doc.get("transactionData", Document.class);
                BazaarTransaction transaction = parseTransactionFromDocument(transactionType, transactionData);

                if (transaction != null) {
                    transactions.add(new PendingTransaction(
                            doc.getString("_id"),
                            playerUuid,
                            profileUuid,
                            transaction,
                            Instant.parse(doc.getString("createdAt"))
                    ));
                }
            } catch (Exception e) {
                System.err.println("Failed to parse pending transaction: " + e.getMessage());
            }
        }

        return transactions;
    }

    /**
     * Mark a pending transaction as processed (remove it from pending)
     */
    public static void markTransactionProcessed(String transactionId) {
        OrderDatabase.database.getCollection("pendingTransactions").updateOne(
                Filters.eq("_id", transactionId),
                Updates.set("processed", true)
        );
    }

    /**
     * Remove all processed transactions for cleanup
     */
    public static void cleanupProcessedTransactions() {
        OrderDatabase.database.getCollection("pendingTransactions").deleteMany(
                Filters.eq("processed", true)
        );
    }

    /**
     * Parse a BazaarTransaction from a MongoDB document
     */
    private static BazaarTransaction parseTransactionFromDocument(String type, Document data) {
        try {
            // Convert Document to JSONObject
            org.json.JSONObject jsonData = new org.json.JSONObject(data.toJson());

            return switch (type) {
                case "SuccessfulBazaarTransaction" -> new SuccessfulBazaarTransaction(
                        null, null, null, null, null, 0, 0, 0, 0, 0, UUID.randomUUID(), UUID.randomUUID(), null
                ).fromJSON(jsonData);
                case "OrderExpiredBazaarTransaction" -> new OrderExpiredBazaarTransaction(
                        null, null, null, null, null, 0, 0, null
                ).fromJSON(jsonData);
                default -> {
                    System.err.println("Unknown pending transaction type: " + type);
                    yield null;
                }
            };
        } catch (Exception e) {
            System.err.println("Error parsing pending transaction: " + e.getMessage());
            return null;
        }
    }

    /**
     * Represents a pending transaction with metadata
     */
    public static class PendingTransaction {
        private final String id;
        private final UUID playerUuid;
        private final UUID profileUuid;
        private final BazaarTransaction transaction;
        private final Instant createdAt;

        public PendingTransaction(String id, UUID playerUuid, UUID profileUuid,
                                  BazaarTransaction transaction, Instant createdAt) {
            this.id = id;
            this.playerUuid = playerUuid;
            this.profileUuid = profileUuid;
            this.transaction = transaction;
            this.createdAt = createdAt;
        }

        public String getId() { return id; }
        public UUID getPlayerUuid() { return playerUuid; }
        public UUID getProfileUuid() { return profileUuid; }
        public BazaarTransaction getTransaction() { return transaction; }
        public Instant getCreatedAt() { return createdAt; }
    }
}