package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

public class GetGameCountsProtocolObject extends ProtocolObject
        <GetGameCountsProtocolObject.GetGameCountsMessage,
                GetGameCountsProtocolObject.GetGameCountsResponse> {

    @Override
    public Serializer<GetGameCountsMessage> getSerializer() {
        return new Serializer<GetGameCountsMessage>() {
            @Override
            public String serialize(GetGameCountsMessage value) {
                JSONObject json = new JSONObject();
                json.put("type", value.type.name());
                if (value.gameTypeName != null) json.put("gameTypeName", value.gameTypeName);
                if (value.mapName != null) json.put("mapName", value.mapName);
                return json.toString();
            }

            @Override
            public GetGameCountsMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GetGameCountsMessage(
                        ServerType.valueOf(obj.getString("type")),
                        obj.optString("gameTypeName", null),
                        obj.optString("mapName", null)
                );
            }

            @Override
            public GetGameCountsMessage clone(GetGameCountsMessage value) {
                return new GetGameCountsMessage(value.type, value.gameTypeName, value.mapName);
            }
        };
    }

    @Override
    public Serializer<GetGameCountsResponse> getReturnSerializer() {
        return new Serializer<GetGameCountsResponse>() {
            @Override
            public String serialize(GetGameCountsResponse value) {
                JSONObject json = new JSONObject();
                json.put("playerCount", value.playerCount);
                json.put("gameCount", value.gameCount);
                return json.toString();
            }

            @Override
            public GetGameCountsResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GetGameCountsResponse(
                        obj.getInt("playerCount"),
                        obj.getInt("gameCount")
                );
            }

            @Override
            public GetGameCountsResponse clone(GetGameCountsResponse value) {
                return new GetGameCountsResponse(value.playerCount, value.gameCount);
            }
        };
    }

    public record GetGameCountsMessage(ServerType type, String gameTypeName, String mapName) { }

    public record GetGameCountsResponse(int playerCount, int gameCount) { }
}
