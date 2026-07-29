package net.swofty.commons.protocol.objects.punishment;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.punishment.PunishmentReason;
import net.swofty.commons.punishment.PunishmentTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class GetActivePunishmentProtocol
        extends RedisProtocol<GetActivePunishmentProtocol.GetActivePunishmentMessage,
        GetActivePunishmentProtocol.GetActivePunishmentResponse> {
    private static final Serializer<GetActivePunishmentMessage> SERIALIZER =
            new JacksonSerializer<>(GetActivePunishmentMessage.class);
    private static final Serializer<GetActivePunishmentResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetActivePunishmentResponse.class);

    @Override
    public Serializer<GetActivePunishmentMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<GetActivePunishmentResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
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
            @NotNull List<PunishmentTag> tags,
            boolean success,
            @Nullable String error
    ) {}
}
