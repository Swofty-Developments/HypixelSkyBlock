package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

public class GetGameCountsProtocolObject extends ProtocolObject
        <GetGameCountsProtocolObject.GetGameCountsMessage,
                GetGameCountsProtocolObject.GetGameCountsResponse> {

    @Override
    public Serializer<GetGameCountsMessage> getSerializer() {
        return new JacksonSerializer<>(GetGameCountsMessage.class);
    }

    @Override
    public Serializer<GetGameCountsResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetGameCountsResponse.class);
    }

    public record GetGameCountsMessage(ServerType type, String gameTypeName, String mapName) { }

    public record GetGameCountsResponse(int playerCount, int gameCount) { }
}
