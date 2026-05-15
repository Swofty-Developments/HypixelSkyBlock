package net.swofty.commons.protocol.objects.election;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class GetElectionDataProtocol
        extends RedisProtocol<GetElectionDataProtocol.GetElectionDataMessage,
        GetElectionDataProtocol.GetElectionDataResponse> {
    private static final Serializer<GetElectionDataMessage> SERIALIZER =
            new JacksonSerializer<>(GetElectionDataMessage.class);
    private static final Serializer<GetElectionDataResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetElectionDataResponse.class);

    @Override
    public Serializer<GetElectionDataMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<GetElectionDataResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record GetElectionDataMessage() {}

    public record GetElectionDataResponse(
            boolean found,
            String serializedData,
            boolean success,
            @Nullable String error
    ) {}
}
