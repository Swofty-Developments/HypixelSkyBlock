package net.swofty.commons.protocol.objects.punishment;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.punishment.PunishmentReason;
import net.swofty.commons.punishment.PunishmentTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PunishPlayerProtocolObject
        extends ProtocolObject<PunishPlayerProtocolObject.PunishPlayerMessage,
        PunishPlayerProtocolObject.PunishPlayerResponse> {

    @Override
    public Serializer<PunishPlayerMessage> getSerializer() {
        return new JacksonSerializer<>(PunishPlayerMessage.class);
    }

    @Override
    public Serializer<PunishPlayerResponse> getReturnSerializer() {
        return new JacksonSerializer<>(PunishPlayerResponse.class);
    }

    public record PunishPlayerMessage(
            @NotNull UUID target,
            @NotNull String type,
            @NotNull PunishmentReason reason,
            @NotNull UUID staff,
            List<PunishmentTag> tags,
            long expiresAt
    ) {}

    public record PunishPlayerResponse(
            boolean success,
            @Nullable String punishmentId,
            @Nullable ErrorCode errorCode,
            @Nullable String errorMessage
    ) {}

    public enum ErrorCode {
        INVALID_TYPE,
        DATABASE_ERROR,
        INVALID_EXPIRY,
        ALREADY_PUNISHED,
        UNKNOWN_ERROR
    }
}
