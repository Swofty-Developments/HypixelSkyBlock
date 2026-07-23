package net.swofty.service.store;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Sorts;
import net.swofty.service.generic.MongoDB;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public record StoreDatabase(String id) implements MongoDB {
    private static final int MAX_BACKOFF_SECONDS = 300;

    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> purchases;
    public static MongoCollection<Document> stripeEvents;
    public static MongoCollection<Document> playerEntitlements;

    @Override
    public @NotNull MongoDB connect(@NotNull String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        purchases = database.getCollection("store_purchases");
        stripeEvents = database.getCollection("stripe_events");
        playerEntitlements = database.getCollection("store-player-entitlements");

        purchases.createIndex(Indexes.ascending("status", "nextAttemptAt", "fulfillmentLeaseUntil"));
        purchases.createIndex(Indexes.ascending("playerUuid", "createdAt"));
        purchases.createIndex(Indexes.ascending("stripeSessionId"), new IndexOptions().unique(true).sparse(true));
        purchases.createIndex(Indexes.ascending("stripePaymentIntentId"), new IndexOptions().sparse(true));
        stripeEvents.createIndex(Indexes.ascending("status", "updatedAt"));
        return this;
    }

    public List<StorePurchase> claimDuePurchases(String workerId, int batchSize, Duration leaseDuration) {
        Date now = new Date();
        Document filter = claimableFilter(now);

        FindIterable<Document> due = purchases.find(filter)
                .sort(Sorts.ascending("paidAt", "createdAt"))
                .limit(batchSize);

        List<StorePurchase> claimed = new ArrayList<>();
        for (Document candidate : due) {
            Document claimFilter = new Document("_id", candidate.getString("_id"))
                    .append("$and", List.of(claimableFilter(now)));
            Document update = new Document("$set", new Document("status", "FULFILLING")
                    .append("fulfillmentLeaseOwner", workerId)
                    .append("fulfillmentLeaseUntil", Date.from(Instant.now().plus(leaseDuration)))
                    .append("updatedAt", now))
                    .append("$inc", new Document("attempts", 1));

            Document claimedDocument = purchases.findOneAndUpdate(
                    claimFilter,
                    update,
                    new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            );
            if (claimedDocument != null) {
                claimed.add(StorePurchase.fromDocument(claimedDocument));
            }
        }

        return claimed;
    }

    private Document claimableFilter(Date now) {
        return new Document("$or", List.of(
                new Document("status", new Document("$in", List.of("PAID", "FAILED")))
                        .append("nextAttemptAt", new Document("$lte", now))
                        .append("$or", List.of(
                                new Document("fulfillmentLeaseUntil", new Document("$exists", false)),
                                new Document("fulfillmentLeaseUntil", new Document("$lt", now))
                        )),
                new Document("status", "FULFILLING")
                        .append("fulfillmentLeaseUntil", new Document("$lt", now))
        ));
    }

    public void markFulfilled(StorePurchase purchase) {
        Date now = new Date();
        purchases.updateOne(
                new Document("_id", purchase.id()),
                new Document("$set", new Document("status", "FULFILLED")
                        .append("fulfilledAt", now)
                        .append("updatedAt", now))
                        .append("$unset", new Document("fulfillmentLeaseOwner", "")
                                .append("fulfillmentLeaseUntil", "")
                                .append("lastError", ""))
        );
    }

    public void markFailed(StorePurchase purchase, Throwable throwable) {
        int attempt = Math.max(1, purchase.attempts());
        long backoffSeconds = Math.min(MAX_BACKOFF_SECONDS, 1L << Math.min(attempt, 8));
        Date nextAttemptAt = Date.from(Instant.now().plusSeconds(backoffSeconds));

        purchases.updateOne(
                new Document("_id", purchase.id()),
                new Document("$set", new Document("status", "FAILED")
                        .append("nextAttemptAt", nextAttemptAt)
                        .append("updatedAt", new Date())
                        .append("lastError", throwable.getMessage()))
                        .append("$unset", new Document("fulfillmentLeaseOwner", "")
                                .append("fulfillmentLeaseUntil", ""))
        );
    }

    @Override
    public void set(@NotNull String key, @Nullable Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public @Nullable Object get(@NotNull String key, @Nullable Object def) {
        Document doc = purchases.find(new Document("_id", id)).first();
        return doc != null && doc.containsKey(key) ? doc.get(key) : def;
    }

    @Override
    public void insertOrUpdate(@NotNull String key, @Nullable Object value) {
        purchases.updateOne(
                new Document("_id", id),
                new Document("$set", new Document(key, value)),
                new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    @Override
    public boolean remove(@NotNull String id) {
        return purchases.deleteOne(new Document("_id", id)).getDeletedCount() > 0;
    }

    @Override
    public boolean exists() {
        return purchases.find(new Document("_id", id)).first() != null;
    }
}
