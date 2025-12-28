package net.swofty.commons.skyblock.bazaar;

import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;

// Fired when a buy+sell actually crosses
public record SuccessfulBazaarTransaction(
        String itemName,
        UUID buyer,
        UUID buyerProfile,
        UUID seller,
        UUID sellerProfile,
        double actualPricePerUnit,      // What was actually paid
        double originalBuyerBid,        // What buyer originally offered
        double quantity,
        double taxTaken,
        double priceImprovement,        // originalBuyerBid - actualPricePerUnit
        UUID buyerOrderId,              // Track which order this partial fill belongs to
        UUID sellerOrderId,             // Track seller's order ID too
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
        json.put("actualPricePerUnit", actualPricePerUnit);
        json.put("originalBuyerBid", originalBuyerBid);
        json.put("quantity", quantity);
        json.put("taxTaken", taxTaken);
        json.put("priceImprovement", priceImprovement);
        json.put("buyerOrderId", buyerOrderId.toString());
        json.put("sellerOrderId", sellerOrderId.toString());
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
                json.getDouble("actualPricePerUnit"),
                json.getDouble("originalBuyerBid"),
                json.getDouble("quantity"),
                json.getDouble("taxTaken"),
                json.getDouble("priceImprovement"),
                UUID.fromString(json.getString("buyerOrderId")),
                UUID.fromString(json.getString("sellerOrderId")),
                Instant.parse(json.getString("timestamp"))
        );
    }
}