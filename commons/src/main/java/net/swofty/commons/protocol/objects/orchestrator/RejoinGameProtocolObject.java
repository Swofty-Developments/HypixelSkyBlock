package net.swofty.commons.protocol.objects.orchestrator;

import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RejoinGameProtocolObject extends ProtocolObject<
        RejoinGameProtocolObject.RejoinGameRequest,
        RejoinGameProtocolObject.RejoinGameResponse> {

    @Override
    public Serializer<RejoinGameRequest> getSerializer() {
        return new JacksonSerializer<>(RejoinGameRequest.class);
    }

    @Override
    public Serializer<RejoinGameResponse> getReturnSerializer() {
        return new JacksonSerializer<>(RejoinGameResponse.class);
    }

    public record RejoinGameRequest(UUID playerUuid) {
    }

    public record RejoinGameResponse(
            boolean hasActiveGame,
            @Nullable UnderstandableProxyServer server,
            @Nullable String gameId,
            @Nullable String mapName,
            @Nullable String teamName,
            boolean willBeSpectator,
            boolean success,
            @Nullable String error
    ) {
    }
}
