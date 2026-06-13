package net.swofty.commons.protocol.objects.guild;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class IsPlayerInGuildProtocolObject extends ProtocolObject<IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage, IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse> {
    @Override
    public Serializer<IsPlayerInGuildMessage> getSerializer() {
        return new JacksonSerializer<>(IsPlayerInGuildMessage.class);
    }

    @Override
    public Serializer<IsPlayerInGuildResponse> getReturnSerializer() {
        return new JacksonSerializer<>(IsPlayerInGuildResponse.class);
    }

    public record IsPlayerInGuildMessage(UUID playerUUID) {
    }

    public record IsPlayerInGuildResponse(boolean isInGuild) {
    }
}
