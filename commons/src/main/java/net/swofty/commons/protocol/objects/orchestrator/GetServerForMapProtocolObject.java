package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class GetServerForMapProtocolObject extends ProtocolObject
        <GetServerForMapProtocolObject.GetServerForMapMessage,
                GetServerForMapProtocolObject.GetServerForMapResponse> {

    @Override
    public Serializer<GetServerForMapMessage> getSerializer() {
        return new JacksonSerializer<>(GetServerForMapMessage.class);
    }

    @Override
    public Serializer<GetServerForMapResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetServerForMapResponse.class);
    }

    public record GetServerForMapMessage(ServerType type, @Nullable String map, String mode, int neededSlots) {
    }

    public record GetServerForMapResponse(UnderstandableProxyServer server, String gameId, boolean success, @Nullable String error) {
    }
}
