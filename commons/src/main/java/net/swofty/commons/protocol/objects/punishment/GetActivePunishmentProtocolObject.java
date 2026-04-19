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

public class GetActivePunishmentProtocolObject
        extends ProtocolObject<GetActivePunishmentProtocolObject.GetActivePunishmentMessage,
        GetActivePunishmentProtocolObject.GetActivePunishmentResponse> {

    @Override
    public Serializer<GetActivePunishmentMessage> getSerializer() {
        return new JacksonSerializer<>(GetActivePunishmentMessage.class);
    }

    @Override
    public Serializer<GetActivePunishmentResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetActivePunishmentResponse.class);
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
