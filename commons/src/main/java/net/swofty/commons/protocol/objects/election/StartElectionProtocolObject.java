package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class StartElectionProtocolObject
        extends ProtocolObject<StartElectionProtocolObject.StartElectionMessage,
        StartElectionProtocolObject.StartElectionResponse> {

    @Override
    public Serializer<StartElectionMessage> getSerializer() {
        return new JacksonSerializer<>(StartElectionMessage.class);
    }

    @Override
    public Serializer<StartElectionResponse> getReturnSerializer() {
        return new JacksonSerializer<>(StartElectionResponse.class);
    }

    public record StartElectionMessage(int year, String candidatesJson) {}

    public record StartElectionResponse(boolean started, String serializedData) {}
}
