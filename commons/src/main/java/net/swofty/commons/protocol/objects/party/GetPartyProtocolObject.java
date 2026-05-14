package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.party.Party;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GetPartyProtocolObject extends ProtocolObject
        <GetPartyProtocolObject.GetPartyMessage,
                GetPartyProtocolObject.GetPartyResponse> {
    private static final Serializer<GetPartyMessage> SERIALIZER =
            new JacksonSerializer<>(GetPartyMessage.class);
    private static final Serializer<GetPartyResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetPartyResponse.class);

    @Override

    public Serializer<GetPartyMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetPartyResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetPartyMessage(UUID memberUuid) { }

    public record GetPartyResponse(Party party, boolean success, @Nullable String error) { }
}
