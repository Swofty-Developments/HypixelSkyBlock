package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class IsPlayerInPartyProtocolObject extends ProtocolObject
        <IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage,
                IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse> {

    @Override
    public Serializer<IsPlayerInPartyMessage> getSerializer() {
        return new JacksonSerializer<>(IsPlayerInPartyMessage.class);
    }

    @Override
    public Serializer<IsPlayerInPartyResponse> getReturnSerializer() {
        return new JacksonSerializer<>(IsPlayerInPartyResponse.class);
    }

    public record IsPlayerInPartyMessage(UUID playerUUID) {
    }

    public record IsPlayerInPartyResponse(boolean isInParty) {
    }
}
