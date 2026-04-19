package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class SendPartyEventToServiceProtocolObject extends ProtocolObject
        <SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage,
        SendPartyEventToServiceProtocolObject.SendPartyEventToServiceResponse> {

    @Override
    public Serializer<SendPartyEventToServiceMessage> getSerializer() {
        return new JacksonSerializer<>(SendPartyEventToServiceMessage.class);
    }

    @Override
    public Serializer<SendPartyEventToServiceResponse> getReturnSerializer() {
        return new JacksonSerializer<>(SendPartyEventToServiceResponse.class);
    }

    public record SendPartyEventToServiceMessage(PartyEvent event) {
    }

    public record SendPartyEventToServiceResponse(boolean success) {
    }
}
