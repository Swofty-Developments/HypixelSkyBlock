package net.swofty.commons.protocol.objects.darkauction;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PlayerLeftAuctionProtocol extends ProtocolObject<
        PlayerLeftAuctionProtocol.PlayerLeftMessage,
        PlayerLeftAuctionProtocol.PlayerLeftResponse> {

    @Override
    public Serializer<PlayerLeftMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PlayerLeftMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerId", value.playerId.toString());
                json.put("playerName", value.playerName);
                if (value.auctionId != null) {
                    json.put("auctionId", value.auctionId.toString());
                }
                return json.toString();
            }

            @Override
            public PlayerLeftMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID auctionId = null;
                if (jsonObject.has("auctionId") && !jsonObject.isNull("auctionId")) {
                    auctionId = UUID.fromString(jsonObject.getString("auctionId"));
                }
                return new PlayerLeftMessage(
                        UUID.fromString(jsonObject.getString("playerId")),
                        jsonObject.getString("playerName"),
                        auctionId
                );
            }

            @Override
            public PlayerLeftMessage clone(PlayerLeftMessage value) {
                return new PlayerLeftMessage(value.playerId, value.playerName, value.auctionId);
            }
        };
    }

    @Override
    public Serializer<PlayerLeftResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PlayerLeftResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("refundAmount", value.refundAmount);
                return json.toString();
            }

            @Override
            public PlayerLeftResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new PlayerLeftResponse(
                        jsonObject.getBoolean("success"),
                        jsonObject.getLong("refundAmount")
                );
            }

            @Override
            public PlayerLeftResponse clone(PlayerLeftResponse value) {
                return new PlayerLeftResponse(value.success, value.refundAmount);
            }
        };
    }

    public record PlayerLeftMessage(
            UUID playerId,
            String playerName,
            UUID auctionId
    ) {}

    public record PlayerLeftResponse(
            boolean success,
            long refundAmount
    ) {}
}
