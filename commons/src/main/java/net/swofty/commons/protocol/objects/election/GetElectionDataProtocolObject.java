package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class GetElectionDataProtocolObject
        extends ProtocolObject<GetElectionDataProtocolObject.GetElectionDataMessage,
        GetElectionDataProtocolObject.GetElectionDataResponse> {

    @Override
    public Serializer<GetElectionDataMessage> getSerializer() {
        return new JacksonSerializer<>(GetElectionDataMessage.class);
    }

    @Override
    public Serializer<GetElectionDataResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetElectionDataResponse.class);
    }

    public record GetElectionDataMessage() {}

    public record GetElectionDataResponse(
            boolean found,
            String serializedData,
            boolean success,
            @Nullable String error
    ) {}
}
