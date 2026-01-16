package net.swofty.commons.protocol.objects.punishment;

import com.google.gson.Gson;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.punishment.PunishmentReason;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.UUID;

public class PunishPlayerProtocolObject
        extends ProtocolObject<PunishPlayerProtocolObject.PunishPlayerMessage,
        PunishPlayerProtocolObject.PunishPlayerResponse> {

    @Override
    public Serializer<PunishPlayerMessage> getSerializer() {
        return new Serializer<>() {

            @Override
            public String serialize(PunishPlayerMessage value) {
                JSONObject json = new JSONObject();
                json.put("target", value.target().toString());
                json.put("type", value.type());
                json.put("reason", new Gson().toJson(value.reason()));
                json.put("expiresAt", value.expiresAt());
                json.put("staff", value.staff().toString());
                return json.toString();
            }

            @Override
            public PunishPlayerMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);

                return new PunishPlayerMessage(
                        UUID.fromString(obj.getString("target")),
                        obj.getString("type"),
                        new Gson().fromJson(obj.getString("reason"), PunishmentReason.class),
                        UUID.fromString(obj.getString("staff")),
                        obj.getLong("expiresAt")
                );
            }

            @Override
            public PunishPlayerMessage clone(PunishPlayerMessage value) {
                return value; // immutable
            }
        };
    }

    @Override
    public Serializer<PunishPlayerResponse> getReturnSerializer() {
        return new Serializer<>() {

            @Override
            public String serialize(PunishPlayerResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success());
                json.put("punishmentId", value.punishmentId());
                json.put("errorCode", value.errorCode());
                json.put("errorMessage", value.errorMessage());
                return json.toString();
            }

            @Override
            public PunishPlayerResponse deserialize(String json) {
                JSONObject obj = new JSONObject(json);

                return new PunishPlayerResponse(
                        obj.getBoolean("success"),
                        obj.optString("punishmentId", null),
                        obj.optString("errorCode", null) != null ? ErrorCode.valueOf(obj.getString("errorCode")) : null,
                        obj.optString("errorMessage", null)
                );
            }

            @Override
            public PunishPlayerResponse clone(PunishPlayerResponse value) {
                return value; // immutable
            }
        };
    }

    // do NOT change this to use Punishment - friendly note from Ari
    public record PunishPlayerMessage(
            @NotNull
            UUID target,
            @NotNull
            String type,
            @NotNull
            PunishmentReason reason,
            @NotNull
            UUID staff,
            long expiresAt
    ) {
    }

    public record PunishPlayerResponse(
            boolean success,
            @Nullable
            String punishmentId,
            @Nullable
            ErrorCode errorCode,
            @Nullable
            String errorMessage
    ) {
    }

    public enum ErrorCode {
        INVALID_TYPE,
        DATABASE_ERROR,
        INVALID_EXPIRY,
        UNKNOWN_ERROR
    }
}