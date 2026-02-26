package net.swofty.commons.protocol.objects.punishment;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.UUID;

public class UnpunishPlayerProtocolObject
        extends ProtocolObject<UnpunishPlayerProtocolObject.UnpunishPlayerMessage,
        UnpunishPlayerProtocolObject.UnpunishPlayerResponse> {

    @Override
    public Serializer<UnpunishPlayerMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(UnpunishPlayerMessage value) {
                JSONObject json = new JSONObject();
                json.put("target", value.target().toString());
                json.put("staff", value.staff().toString());
                json.put("type", value.type());
                return json.toString();
            }

            @Override
            public UnpunishPlayerMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new UnpunishPlayerMessage(
                        UUID.fromString(obj.getString("target")),
                        UUID.fromString(obj.getString("staff")),
                        obj.getString("type")
                );
            }

            @Override
            public UnpunishPlayerMessage clone(UnpunishPlayerMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<UnpunishPlayerResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(UnpunishPlayerResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success());
                json.put("errorMessage", value.errorMessage());
                return json.toString();
            }

            @Override
            public UnpunishPlayerResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new UnpunishPlayerResponse(
                        obj.getBoolean("success"),
                        obj.isNull("errorMessage") ? null : obj.getString("errorMessage")
                );
            }

            @Override
            public UnpunishPlayerResponse clone(UnpunishPlayerResponse value) {
                return value;
            }
        };
    }

    public record UnpunishPlayerMessage(
            @NotNull UUID target,
            @NotNull UUID staff,
            @NotNull String type
    ) {}

    public record UnpunishPlayerResponse(
            boolean success,
            @Nullable String errorMessage
    ) {}
}
