package net.swofty.velocity.redis.listeners;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import net.kyori.adventure.text.Component;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.to.StorePurchaseFulfillmentProtocol;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.data.UserDatabase;
import org.bson.Document;
import org.tinylog.Logger;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ListenerStorePurchaseFulfillment implements RedisMessageHandler<
    StorePurchaseFulfillmentProtocol.Request,
    StorePurchaseFulfillmentProtocol.Response> {

    private static final Map<String, Integer> STORE_RANK_STRENGTH = Map.of(
        "DEFAULT", 0,
        "VIP", 1,
        "VIP_PLUS", 2,
        "MVP", 3,
        "MVP_PLUS", 4,
        "MVP_PLUS_PLUS", 5
    );
    private static final ScheduledExecutorService EXPIRATION_RECONCILER =
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "store-rank-expiration-reconciler");
            thread.setDaemon(true);
            return thread;
        });

    @Override
    public RedisProtocol<StorePurchaseFulfillmentProtocol.Request, StorePurchaseFulfillmentProtocol.Response> protocol() {
        return new StorePurchaseFulfillmentProtocol();
    }

    @Override
    public StorePurchaseFulfillmentProtocol.Response handle(
        StorePurchaseFulfillmentProtocol.Request message,
        RedisMessageContext context
    ) {
        try {
            UUID playerUuid = UUID.fromString(message.playerUuid());
            MongoCollection<Document> collection = UserDatabase.database.getCollection("store-player-entitlements");
            Date now = new Date();
            Date paidAt = new Date(message.paidAt() > 0 ? message.paidAt() : now.getTime());

            collection.updateOne(
                new Document("_id", playerUuid.toString()),
                new Document("$setOnInsert", initialProjection(message, now)),
                new com.mongodb.client.model.UpdateOptions().upsert(true)
            );
            preserveTemporaryRankFallback(playerUuid, collection, message);

            Document filter = new Document("_id", playerUuid.toString())
                .append("appliedPurchaseIds", new Document("$ne", message.purchaseId()));
            Document update = updateFor(message, now, paidAt);
            Document projection = collection.findOneAndUpdate(
                filter,
                update,
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            );

            boolean duplicate = false;
            if (projection == null) {
                duplicate = true;
                projection = collection.find(new Document("_id", playerUuid.toString())).first();
            }

            if (projection == null) {
                return new StorePurchaseFulfillmentProtocol.Response(false, duplicate, "Projection was not written.");
            }

            applyRankProjection(playerUuid, projection);
            if (!duplicate) {
                notifyOnlinePlayer(playerUuid, message.productName());
            }

            return new StorePurchaseFulfillmentProtocol.Response(true, duplicate, null);
        } catch (Exception exception) {
            Logger.error(exception, "Failed to fulfill store purchase {}", message.purchaseId());
            return new StorePurchaseFulfillmentProtocol.Response(false, false, exception.getMessage());
        }
    }

    public static void startRankExpirationReconciler() {
        EXPIRATION_RECONCILER.scheduleWithFixedDelay(() -> {
            try {
                MongoCollection<Document> collection = UserDatabase.database.getCollection("store-player-entitlements");
                Date now = new Date();
                Document expiredRankFilter = new Document("entitlements", new Document("$elemMatch",
                    new Document("type", "RANK").append("expiresAt", new Document("$lte", now))));

                for (Document projection : collection.find(expiredRankFilter)) {
                    applyRankProjection(UUID.fromString(projection.getString("_id")), projection);
                }
            } catch (Exception exception) {
                Logger.error(exception, "Failed to reconcile expired store rank entitlements");
            }
        }, 1, 60, TimeUnit.SECONDS);
    }

    private void preserveTemporaryRankFallback(
        UUID playerUuid,
        MongoCollection<Document> collection,
        StorePurchaseFulfillmentProtocol.Request message
    ) {
        boolean hasTemporaryRank = message.entitlements().stream()
            .anyMatch(entitlement -> "RANK".equals(entitlement.type()) && entitlement.durationDays() != null);
        if (!hasTemporaryRank) return;

        Document projection = collection.find(new Document("_id", playerUuid.toString())).first();
        if (projection != null && projection.getString("temporaryRankFallback") != null) return;

        String currentRank = currentProfileRank(playerUuid);
        if ("MVP_PLUS_PLUS".equals(currentRank)) {
            currentRank = "MVP_PLUS";
        }

        collection.updateOne(
            new Document("_id", playerUuid.toString())
                .append("temporaryRankFallback", new Document("$exists", false)),
            new Document("$set", new Document("temporaryRankFallback", currentRank))
        );
    }

    private Document updateFor(StorePurchaseFulfillmentProtocol.Request message, Date now, Date paidAt) {
        Document set = new Document("playerName", message.playerName())
            .append("updatedAt", now);
        Document addToSet = new Document("appliedPurchaseIds", message.purchaseId());
        Document inc = new Document();
        Document eachEntitlements = new Document("$each", entitlementDocuments(message, paidAt));
        addToSet.append("entitlements", eachEntitlements);

        for (StorePurchaseFulfillmentProtocol.Entitlement entitlement : message.entitlements()) {
            switch (entitlement.type()) {
                case "STORE_CURRENCY" -> inc.append("storeGold", entitlement.amount());
                case "SKYBLOCK_GEMS" -> inc.append("skyBlockGems", entitlement.amount());
                case "FEATURE" -> addToSet.append("featureFlags", entitlement.key());
                case "BOOSTER" -> inc.append("boosters." + entitlement.key(), entitlement.amount());
                case "COSMETIC" -> inc.append("cosmetics." + entitlement.key(), entitlement.amount());
                default -> {
                }
            }
        }

        Document update = new Document("$set", set)
            .append("$addToSet", addToSet);
        if (!inc.isEmpty()) {
            update.append("$inc", inc);
        }
        return update;
    }

    private Document initialProjection(StorePurchaseFulfillmentProtocol.Request message, Date now) {
        return new Document("_id", message.playerUuid())
            .append("playerName", message.playerName())
            .append("createdAt", now)
            .append("updatedAt", now)
            .append("storeGold", 0L)
            .append("skyBlockGems", 0L);
    }

    private List<Document> entitlementDocuments(StorePurchaseFulfillmentProtocol.Request message, Date paidAt) {
        return message.entitlements().stream().map(entitlement -> {
            Document document = new Document("purchaseId", message.purchaseId())
                .append("productId", message.productId())
                .append("productName", message.productName())
                .append("type", entitlement.type())
                .append("key", entitlement.key())
                .append("amount", entitlement.amount())
                .append("awardedAt", paidAt);

            if (entitlement.durationDays() != null) {
                Instant expiresAt = paidAt.toInstant().plus(entitlement.durationDays(), ChronoUnit.DAYS);
                document.append("expiresAt", Date.from(expiresAt));
            }

            return document;
        }).toList();
    }

    private static void applyRankProjection(UUID playerUuid, Document projection) {
        String highestRank = highestActiveStoreRank(projection);
        String currentRank = currentProfileRank(playerUuid);

        if ("STAFF".equals(currentRank) || "YOUTUBE".equals(currentRank)) {
            return;
        }

        if (highestRank == null) {
            if ("MVP_PLUS_PLUS".equals(currentRank)) {
                String fallback = projection.getString("temporaryRankFallback");
                setProfileRank(playerUuid, fallback != null ? fallback : "DEFAULT");
            }
            return;
        }

        int currentStrength = STORE_RANK_STRENGTH.getOrDefault(currentRank, 0);
        int targetStrength = STORE_RANK_STRENGTH.getOrDefault(highestRank, 0);
        if (targetStrength < currentStrength && !"MVP_PLUS_PLUS".equals(currentRank)) {
            return;
        }

        setProfileRank(playerUuid, highestRank);
    }

    private static String currentProfileRank(UUID playerUuid) {
        Document profile = UserDatabase.collection.find(new Document("_id", playerUuid.toString())).first();
        return profile != null ? deserializeRank(profile.getString("rank")) : "DEFAULT";
    }

    private static void setProfileRank(UUID playerUuid, String rank) {
        UserDatabase.collection.updateOne(
            new Document("_id", playerUuid.toString()),
            new Document("$set", new Document("rank", serializeRank(rank)))
                .append("$setOnInsert", new Document("_id", playerUuid.toString())),
            new com.mongodb.client.model.UpdateOptions().upsert(true)
        );
    }

    private static String highestActiveStoreRank(Document projection) {
        List<Document> entitlements = projection.getList("entitlements", Document.class, List.of());
        Instant now = Instant.now();
        String best = null;
        int bestStrength = -1;

        for (Document entitlement : entitlements) {
            if (!"RANK".equals(entitlement.getString("type"))) continue;
            Date expiresAt = entitlement.getDate("expiresAt");
            if (expiresAt != null && expiresAt.toInstant().isBefore(now)) continue;

            String rank = entitlement.getString("key");
            int strength = STORE_RANK_STRENGTH.getOrDefault(rank, -1);
            if (strength > bestStrength) {
                best = rank;
                bestStrength = strength;
            }
        }

        return best;
    }

    private static String deserializeRank(String value) {
        if (value == null || value.isBlank()) return "DEFAULT";
        return value.replace("\"", "");
    }

    private static String serializeRank(String rank) {
        return "\"" + rank + "\"";
    }

    private void notifyOnlinePlayer(UUID playerUuid, String productName) {
        SkyBlockVelocity.getServer().getPlayer(playerUuid).ifPresent(player ->
            player.sendMessage(Component.text("§aYour purchase of §e" + productName + "§a has been delivered."))
        );
    }
}
