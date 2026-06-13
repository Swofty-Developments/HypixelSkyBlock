package net.swofty.commons.protocol.objects.guild;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class IsPlayerInGuildProtocolObject extends ProtocolObject
        <IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage,
                IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse> {

    @Override
    public Serializer<IsPlayerInGuildMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(IsPlayerInGuildMessage value) {
                return new JSONObject().put("playerUUID", value.playerUUID.toString()).toString();
            }

            @Override
            public IsPlayerInGuildMessage deserialize(String json) {
                return new IsPlayerInGuildMessage(UUID.fromString(new JSONObject(json).getString("playerUUID")));
            }

            @Override
            public IsPlayerInGuildMessage clone(IsPlayerInGuildMessage value) {
                return new IsPlayerInGuildMessage(value.playerUUID);
            }
        };
    }

    @Override
    public Serializer<IsPlayerInGuildResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(IsPlayerInGuildResponse value) {
                return value.isInGuild ? "true" : "false";
            }

            @Override
            public IsPlayerInGuildResponse deserialize(String json) {
                return new IsPlayerInGuildResponse(json.equals("true"));
            }

            @Override
            public IsPlayerInGuildResponse clone(IsPlayerInGuildResponse value) {
                return new IsPlayerInGuildResponse(value.isInGuild);
            }
        };
    }

    public record IsPlayerInGuildMessage(UUID playerUUID) { }
    public record IsPlayerInGuildResponse(boolean isInGuild) { }
}
