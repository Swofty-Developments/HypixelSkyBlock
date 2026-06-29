package net.swofty.service.store;

import org.bson.Document;

import java.time.Instant;
import java.util.List;

public record StorePurchase(
        String id,
        String playerUuid,
        String playerName,
        String productId,
        String productName,
        Instant paidAt,
        List<StoreEntitlement> entitlements,
        int attempts
) {
    public static StorePurchase fromDocument(Document document) {
        List<Document> entitlementDocuments = document.getList("entitlements", Document.class, List.of());
        Instant paidAt = document.getDate("paidAt") != null
                ? document.getDate("paidAt").toInstant()
                : Instant.now();

        return new StorePurchase(
                document.getString("_id"),
                document.getString("playerUuid"),
                document.getString("playerName"),
                document.getString("productId"),
                document.getString("productName"),
                paidAt,
                entitlementDocuments.stream().map(StoreEntitlement::fromDocument).toList(),
                document.getInteger("attempts", 0)
        );
    }
}
