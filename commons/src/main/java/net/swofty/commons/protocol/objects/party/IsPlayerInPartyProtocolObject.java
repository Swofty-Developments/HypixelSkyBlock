package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class IsPlayerInPartyProtocolObject extends ProtocolObject
        <IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage,
                IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse> {
    private static final Serializer<IsPlayerInPartyMessage> SERIALIZER =
            new JacksonSerializer<>(IsPlayerInPartyMessage.class);
    private static final Serializer<IsPlayerInPartyResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(IsPlayerInPartyResponse.class);

    @Override

    public Serializer<IsPlayerInPartyMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<IsPlayerInPartyResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record IsPlayerInPartyMessage(UUID playerUUID) {
    }

    public record IsPlayerInPartyResponse(boolean isInParty, boolean success, @Nullable String error) {
    }
}
