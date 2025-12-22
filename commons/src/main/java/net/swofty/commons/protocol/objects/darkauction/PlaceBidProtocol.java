package net.swofty.commons.protocol.objects.darkauction;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PlaceBidProtocol extends ProtocolObject<
        PlaceBidProtocol.PlaceBidMessage,
        PlaceBidProtocol.PlaceBidResponse> {

    @Override
    public Serializer<PlaceBidMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PlaceBidMessage value) {
                JSONObject json = new JSONObject();
                json.put("auctionId", value.auctionId.toString());
                json.put("playerId", value.playerId.toString());
                json.put("playerName", value.playerName);
                json.put("bidAmount", value.bidAmount);
                return json.toString();
            }

            @Override
            public PlaceBidMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new PlaceBidMessage(
                        UUID.fromString(jsonObject.getString("auctionId")),
                        UUID.fromString(jsonObject.getString("playerId")),
                        jsonObject.getString("playerName"),
                        jsonObject.getLong("bidAmount")
                );
            }

            @Override
            public PlaceBidMessage clone(PlaceBidMessage value) {
                return new PlaceBidMessage(value.auctionId, value.playerId, value.playerName, value.bidAmount);
            }
        };
    }

    @Override
    public Serializer<PlaceBidResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PlaceBidResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("message", value.message);
                return json.toString();
            }

            @Override
            public PlaceBidResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new PlaceBidResponse(
                        jsonObject.getBoolean("success"),
                        jsonObject.getString("message")
                );
            }

            @Override
            public PlaceBidResponse clone(PlaceBidResponse value) {
                return new PlaceBidResponse(value.success, value.message);
            }
        };
    }

    public record PlaceBidMessage(
            UUID auctionId,
            UUID playerId,
            String playerName,
            long bidAmount
    ) {}

    public record PlaceBidResponse(
            boolean success,
            String message
    ) {}
}
