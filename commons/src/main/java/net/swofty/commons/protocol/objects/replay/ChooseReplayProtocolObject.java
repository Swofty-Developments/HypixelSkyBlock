package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class ChooseReplayProtocolObject extends ProtocolObject
        <ChooseReplayProtocolObject.ChooseReplayMessage,
            ChooseReplayProtocolObject.ChooseReplayResponse> {

    @Override
    public Serializer<ChooseReplayMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(ChooseReplayMessage value) {
                JSONObject json = new JSONObject();
                json.put("uuid", value.player.toString());
                json.put("replayId", value.replayId);
                return json.toString();
            }

            @Override
            public ChooseReplayMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new ChooseReplayMessage(
                    UUID.fromString(obj.getString("uuid")),
                    obj.getString("replayId")
                );
            }

            @Override
            public ChooseReplayMessage clone(ChooseReplayMessage value) {
                return new ChooseReplayMessage(value.player, value.replayId);
            }
        };
    }

    @Override
    public Serializer<ChooseReplayResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(ChooseReplayResponse value) {
                JSONObject json = new JSONObject();
                json.put("error", value.error);
                return json.toString();
            }

            @Override
            public ChooseReplayResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new ChooseReplayResponse(obj.getBoolean("error"));
            }

            @Override
            public ChooseReplayResponse clone(ChooseReplayResponse value) {
                return new ChooseReplayResponse(value.error);
            }
        };
    }

    public record ChooseReplayMessage(UUID player, String replayId) {
    }

    public record ChooseReplayResponse(boolean error) {
    }
}
