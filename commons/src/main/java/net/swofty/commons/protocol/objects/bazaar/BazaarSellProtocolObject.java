package net.swofty.commons.protocol.objects.bazaar;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class BazaarSellProtocolObject extends ProtocolObject<
        BazaarSellProtocolObject.BazaarSellMessage,
        BazaarSellProtocolObject.BazaarSellResponse> {

    @Override
    public Serializer<BazaarSellMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarSellMessage value) {
                JSONObject json = new JSONObject();
                json.put("item-name", value.itemName);
                json.put("player-uuid", value.playerUUID);
                json.put("profile-uuid", value.profileUUID);
                json.put("price", value.price);
                json.put("amount", value.amount);
                return json.toString();
            }

            @Override
            public BazaarSellMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                BazaarSellMessage message = new BazaarSellMessage();
                message.itemName = jsonObject.getString("item-name");
                message.playerUUID = UUID.fromString(jsonObject.getString("player-uuid"));
                message.profileUUID = UUID.fromString(jsonObject.getString("profile-uuid"));
                message.price = jsonObject.getDouble("price");
                message.amount = jsonObject.getInt("amount");
                return message;
            }

            @Override
            public BazaarSellMessage clone(BazaarSellMessage value) {
                return new BazaarSellMessage(value.itemName, value.playerUUID, value.profileUUID, value.price, value.amount);
            }
        };
    }

    @Override
    public Serializer<BazaarSellResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarSellResponse value) {
                JSONObject json = new JSONObject();
                json.put("successful", value.successful);
                return json.toString();
            }

            @Override
            public BazaarSellResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                BazaarSellResponse message = new BazaarSellResponse();
                message.successful = jsonObject.getBoolean("successful");
                return message;
            }

            @Override
            public BazaarSellResponse clone(BazaarSellResponse value) {
                return new BazaarSellResponse(value.successful);
            }
        };
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BazaarSellMessage {
        public String itemName;
        public UUID playerUUID;
        public UUID profileUUID;
        public Double price;
        public int amount;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BazaarSellResponse {
        public boolean successful;
    }
}
