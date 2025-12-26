package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.UUID;

public class RejoinGameProtocolObject extends ProtocolObject<
        RejoinGameProtocolObject.RejoinGameRequest,
        RejoinGameProtocolObject.RejoinGameResponse> {

    @Override
    public Serializer<RejoinGameRequest> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(RejoinGameRequest value) {
                JSONObject json = new JSONObject();
                json.put("playerUuid", value.playerUuid.toString());
                return json.toString();
            }

            @Override
            public RejoinGameRequest deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new RejoinGameRequest(UUID.fromString(obj.getString("playerUuid")));
            }

            @Override
            public RejoinGameRequest clone(RejoinGameRequest value) {
                return new RejoinGameRequest(value.playerUuid);
            }
        };
    }

    @Override
    public Serializer<RejoinGameResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(RejoinGameResponse value) {
                JSONObject json = new JSONObject();
                json.put("hasActiveGame", value.hasActiveGame);
                if (value.hasActiveGame && value.server != null) {
                    json.put("server", value.server.toJSON());
                    json.put("gameId", value.gameId);
                    json.put("mapName", value.mapName);
                    json.put("teamName", value.teamName != null ? value.teamName : JSONObject.NULL);
                    json.put("willBeSpectator", value.willBeSpectator);
                } else {
                    json.put("server", JSONObject.NULL);
                    json.put("gameId", JSONObject.NULL);
                    json.put("mapName", JSONObject.NULL);
                    json.put("teamName", JSONObject.NULL);
                    json.put("willBeSpectator", false);
                }
                return json.toString();
            }

            @Override
            public RejoinGameResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                boolean hasActiveGame = obj.getBoolean("hasActiveGame");
                if (!hasActiveGame || obj.isNull("server")) {
                    return new RejoinGameResponse(false, null, null, null, null, false);
                }
                return new RejoinGameResponse(
                        true,
                        UnderstandableProxyServer.singleFromJSON(obj.getJSONObject("server")),
                        obj.getString("gameId"),
                        obj.getString("mapName"),
                        obj.isNull("teamName") ? null : obj.getString("teamName"),
                        obj.getBoolean("willBeSpectator")
                );
            }

            @Override
            public RejoinGameResponse clone(RejoinGameResponse value) {
                return new RejoinGameResponse(
                        value.hasActiveGame,
                        value.server,
                        value.gameId,
                        value.mapName,
                        value.teamName,
                        value.willBeSpectator
                );
            }
        };
    }

    public record RejoinGameRequest(UUID playerUuid) {
    }

    public record RejoinGameResponse(
            boolean hasActiveGame,
            @Nullable UnderstandableProxyServer server,
            @Nullable String gameId,
            @Nullable String mapName,
            @Nullable String teamName,
            boolean willBeSpectator
    ) {
    }
}
