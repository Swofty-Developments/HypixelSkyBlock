package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class ChooseGameProtocolObject extends ProtocolObject
        <ChooseGameProtocolObject.ChooseGameMessage,
                ChooseGameProtocolObject.ChooseGameResponse> {

    @Override
    public Serializer<ChooseGameMessage> getSerializer() {
        return new Serializer<ChooseGameMessage>() {
            @Override
            public String serialize(ChooseGameMessage value) {
                JSONObject json = new JSONObject();
                json.put("uuid", value.player.toString());
                json.put("server", value.server.toJSON());
                json.put("gameId", value.gameId);
                return json.toString();
            }

            @Override
            public ChooseGameMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new ChooseGameMessage(
                        UUID.fromString(obj.getString("uuid")),
                        UnderstandableProxyServer.singleFromJSON(obj.getJSONObject("server")),
                        obj.getString("gameId")
                );
            }

            @Override
            public ChooseGameMessage clone(ChooseGameMessage value) {
                return new ChooseGameMessage(value.player, value.server, value.gameId);
            }
        };
    }

    @Override
    public Serializer<ChooseGameResponse> getReturnSerializer() {
        return new Serializer<ChooseGameResponse>() {
            @Override
            public String serialize(ChooseGameResponse value) {
                JSONObject json = new JSONObject();
                json.put("error", value.error);
                return json.toString();
            }

            @Override
            public ChooseGameResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new ChooseGameResponse(obj.getBoolean("error"));
            }

            @Override
            public ChooseGameResponse clone(ChooseGameResponse value) {
                return new ChooseGameResponse(value.error);
            }
        };
    }

    public record ChooseGameMessage(UUID player, UnderstandableProxyServer server, String gameId) {
    }

    public record ChooseGameResponse(boolean error) {
    }
}
