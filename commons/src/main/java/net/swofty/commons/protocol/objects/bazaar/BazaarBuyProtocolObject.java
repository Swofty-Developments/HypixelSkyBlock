package net.swofty.commons.protocol.objects.bazaar;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class BazaarBuyProtocolObject extends ProtocolObject
        <BazaarBuyProtocolObject.BazaarBuyMessage, BazaarBuyProtocolObject.BazaarBuyResponse> {

    @Override
    public Serializer<BazaarBuyMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarBuyMessage value) {
                JSONObject json = new JSONObject();
                json.put("item-name", value.itemName);
                json.put("player-uuid", value.playerUUID);
                json.put("profile-uuid", value.profileUUID);
                json.put("price", value.price);
                json.put("amount", value.amount);
                return json.toString();
            }

            @Override
            public BazaarBuyMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                BazaarBuyMessage message = new BazaarBuyMessage();
                message.itemName = jsonObject.getString("item-name");
                message.playerUUID = UUID.fromString(jsonObject.getString("player-uuid"));
                message.profileUUID = UUID.fromString(jsonObject.getString("profile-uuid"));
                message.price = jsonObject.getDouble("price");
                message.amount = jsonObject.getInt("amount");
                return message;
            }

            @Override
            public BazaarBuyMessage clone(BazaarBuyMessage value) {
                return new BazaarBuyMessage(value.itemName, value.amount, value.price, value.playerUUID, value.profileUUID);
            }
        };
    }

    @Override
    public Serializer<BazaarBuyResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarBuyResponse value) {
                JSONObject json = new JSONObject();
                json.put("successful", value.successful);
                return json.toString();
            }

            @Override
            public BazaarBuyResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                BazaarBuyResponse message = new BazaarBuyResponse();
                message.successful = jsonObject.getBoolean("successful");
                return message;
            }

            @Override
            public BazaarBuyResponse clone(BazaarBuyResponse value) {
                return new BazaarBuyResponse(value.successful);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BazaarBuyMessage {
        public String itemName;
        public int amount;
        public double price;
        public UUID playerUUID;
        public UUID profileUUID;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BazaarBuyResponse {
        public boolean successful;
    }
}
