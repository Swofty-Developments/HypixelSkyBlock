package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class GetServerForMapProtocol extends RedisProtocol
        <GetServerForMapProtocol.GetServerForMapMessage,
                GetServerForMapProtocol.GetServerForMapResponse> {
    private static final Serializer<GetServerForMapMessage> SERIALIZER =
            new JacksonSerializer<>(GetServerForMapMessage.class);
    private static final Serializer<GetServerForMapResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetServerForMapResponse.class);

    @Override

    public Serializer<GetServerForMapMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetServerForMapResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetServerForMapMessage(ServerType type, @Nullable String map, String mode, int neededSlots) {
    }

    public record GetServerForMapResponse(UnderstandableProxyServer server, String gameId, boolean success, @Nullable String error) {
    }
}
