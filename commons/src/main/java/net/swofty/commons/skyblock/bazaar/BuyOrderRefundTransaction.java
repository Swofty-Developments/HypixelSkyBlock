package net.swofty.commons.skyblock.bazaar;

import org.json.JSONObject;
import java.time.Instant;
import java.util.UUID;

public record BuyOrderRefundTransaction(
        UUID orderId,
        String itemName,
        UUID owner,
        UUID ownerProfile,
        double refundAmount,
        String reason,  // "COMPLETED", "EXPIRED", "CANCELLED"
        Instant timestamp
) implements BazaarTransaction {

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("orderId", orderId.toString());
        json.put("itemName", itemName);
        json.put("owner", owner.toString());
        json.put("ownerProfile", ownerProfile.toString());
        json.put("refundAmount", refundAmount);
        json.put("reason", reason);
        json.put("timestamp", timestamp.toString());
        return json;
    }

    @Override
    public BazaarTransaction fromJSON(JSONObject json) {
        return new BuyOrderRefundTransaction(
                UUID.fromString(json.getString("orderId")),
                json.getString("itemName"),
                UUID.fromString(json.getString("owner")),
                UUID.fromString(json.getString("ownerProfile")),
                json.getDouble("refundAmount"),
                json.getString("reason"),
                Instant.parse(json.getString("timestamp"))
        );
    }
}