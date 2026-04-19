package net.swofty.commons.protocol.objects.punishment;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UnpunishPlayerProtocolObject
        extends ProtocolObject<UnpunishPlayerProtocolObject.UnpunishPlayerMessage,
        UnpunishPlayerProtocolObject.UnpunishPlayerResponse> {

    @Override
    public Serializer<UnpunishPlayerMessage> getSerializer() {
        return new JacksonSerializer<>(UnpunishPlayerMessage.class);
    }

    @Override
    public Serializer<UnpunishPlayerResponse> getReturnSerializer() {
        return new JacksonSerializer<>(UnpunishPlayerResponse.class);
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
