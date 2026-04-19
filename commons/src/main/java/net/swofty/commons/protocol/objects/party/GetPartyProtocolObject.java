package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.party.Party;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class GetPartyProtocolObject extends ProtocolObject
        <GetPartyProtocolObject.GetPartyMessage,
                GetPartyProtocolObject.GetPartyResponse> {

    @Override
    public Serializer<GetPartyMessage> getSerializer() {
        return new JacksonSerializer<>(GetPartyMessage.class);
    }

    @Override
    public Serializer<GetPartyResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetPartyResponse.class);
    }

    public record GetPartyMessage(UUID memberUuid) { }

    public record GetPartyResponse(Party party) { }
}
