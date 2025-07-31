package net.swofty.commons.bazaar;

import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;

// Fired when a buy+sell actually crosses
public record SuccessfulBazaarTransaction(
        String   itemName,
        UUID     buyer,
        UUID     buyerProfile,
        UUID     seller,
        UUID     sellerProfile,
        double   pricePerUnit,
        double   quantity,
        double   taxTaken,        // coins withheld by the system
        Instant timestamp
) implements BazaarTransaction {
    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("itemName", itemName);
        json.put("buyer", buyer.toString());
        json.put("buyerProfile", buyerProfile.toString());
        json.put("seller", seller.toString());
        json.put("sellerProfile", sellerProfile.toString());
        json.put("pricePerUnit", pricePerUnit);
        json.put("quantity", quantity);
        json.put("taxTaken", taxTaken);
        json.put("timestamp", timestamp.toString());
        return json;
    }

    @Override
    public BazaarTransaction fromJSON(JSONObject json) {
        return new SuccessfulBazaarTransaction(
                json.getString("itemName"),
                UUID.fromString(json.getString("buyer")),
                UUID.fromString(json.getString("buyerProfile")),
                UUID.fromString(json.getString("seller")),
                UUID.fromString(json.getString("sellerProfile")),
                json.getDouble("pricePerUnit"),
                json.getDouble("quantity"),
                json.getDouble("taxTaken"),
                Instant.parse(json.getString("timestamp"))
        );
    }
}