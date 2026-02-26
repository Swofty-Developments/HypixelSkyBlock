package net.swofty.commons.protocol.objects.punishment;

import com.google.gson.Gson;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.punishment.PunishmentReason;
import net.swofty.commons.punishment.PunishmentTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class GetActivePunishmentProtocolObject
        extends ProtocolObject<GetActivePunishmentProtocolObject.GetActivePunishmentMessage,
        GetActivePunishmentProtocolObject.GetActivePunishmentResponse> {

    @Override
    public Serializer<GetActivePunishmentMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetActivePunishmentMessage value) {
                JSONObject json = new JSONObject();
                json.put("target", value.target().toString());
                json.put("type", value.type());
                return json.toString();
            }

            @Override
            public GetActivePunishmentMessage deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                return new GetActivePunishmentMessage(
                        UUID.fromString(obj.getString("target")),
                        obj.getString("type")
                );
            }

            @Override
            public GetActivePunishmentMessage clone(GetActivePunishmentMessage value) {
                return value;
            }
        };
    }

    @Override
    public Serializer<GetActivePunishmentResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetActivePunishmentResponse value) {
                Gson gson = new Gson();
                JSONObject json = new JSONObject();
                json.put("found", value.found());
                json.put("type", value.type());
                json.put("banId", value.banId());
                json.put("reason", value.reason() != null ? gson.toJson(value.reason()) : null);
                json.put("expiresAt", value.expiresAt());
                json.put("tags", value.tags() != null ? gson.toJson(value.tags()) : null);
                return json.toString();
            }

            @Override
            public GetActivePunishmentResponse deserialize(String json) {
                Gson gson = new Gson();
                JSONObject obj = new JSONObject(json);
                boolean found = obj.getBoolean("found");
                if (!found) {
                    return new GetActivePunishmentResponse(false, null, null, null, 0, List.of());
                }
                List<PunishmentTag> tags = List.of();
                if (!obj.isNull("tags")) {
                    tags = List.of(gson.fromJson(obj.getString("tags"), PunishmentTag[].class));
                }
                return new GetActivePunishmentResponse(
                        true,
                        obj.optString("type", null),
                        obj.optString("banId", null),
                        obj.isNull("reason") ? null : gson.fromJson(obj.getString("reason"), PunishmentReason.class),
                        obj.getLong("expiresAt"),
                        tags
                );
            }

            @Override
            public GetActivePunishmentResponse clone(GetActivePunishmentResponse value) {
                return value;
            }
        };
    }

    public record GetActivePunishmentMessage(
            @NotNull UUID target,
            @NotNull String type
    ) {}

    public record GetActivePunishmentResponse(
            boolean found,
            @Nullable String type,
            @Nullable String banId,
            @Nullable PunishmentReason reason,
            long expiresAt,
            @NotNull List<PunishmentTag> tags
    ) {}
}
