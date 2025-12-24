package net.swofty.commons.skyblock.bazaar;

import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;

public record OrderExpiredBazaarTransaction(
        UUID     orderId,
        String   itemName,
        UUID     owner,
        UUID     ownerProfile,
        String   side,            // "BUY" or "SELL"
        double   originalPricePaid,
        double   remainingQty,
        Instant  expiredAt
) implements BazaarTransaction {
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("orderId", orderId.toString());
        json.put("itemName", itemName);
        json.put("owner", owner.toString());
        json.put("ownerProfile", ownerProfile.toString());
        json.put("side", side);
        json.put("remainingQty", remainingQty);
        json.put("expiredAt", expiredAt.toString());
        json.put("originalPricePaid", originalPricePaid);
        return json;
    }

    @Override
    public BazaarTransaction fromJSON(JSONObject json) {
        return new OrderExpiredBazaarTransaction(
                UUID.fromString(json.getString("orderId")),
                json.getString("itemName"),
                UUID.fromString(json.getString("owner")),
                UUID.fromString(json.getString("ownerProfile")),
                json.getString("side"),
                json.getDouble("originalPricePaid"),
                json.getDouble("remainingQty"),
                Instant.parse(json.getString("expiredAt"))
        );
    }
}