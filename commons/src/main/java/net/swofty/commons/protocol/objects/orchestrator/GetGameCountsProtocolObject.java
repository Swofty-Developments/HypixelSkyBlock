package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class GetGameCountsProtocolObject extends ProtocolObject
        <GetGameCountsProtocolObject.GetGameCountsMessage,
                GetGameCountsProtocolObject.GetGameCountsResponse> {
    private static final Serializer<GetGameCountsMessage> SERIALIZER =
            new JacksonSerializer<>(GetGameCountsMessage.class);
    private static final Serializer<GetGameCountsResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetGameCountsResponse.class);

    @Override

    public Serializer<GetGameCountsMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetGameCountsResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetGameCountsMessage(ServerType type, String gameTypeName, String mapName) { }

    public record GetGameCountsResponse(int playerCount, int gameCount, boolean success, @Nullable String error) { }
}
