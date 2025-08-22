package net.swofty.service.experimentation.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronGetStateProtocolObject;
import net.swofty.service.experimentation.data.GameSession;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.ArrayList;
import java.util.UUID;

public class ChronomatronGetStateEndpoint implements ServiceEndpoint<
        ChronomatronGetStateProtocolObject.GetStateMessage,
        ChronomatronGetStateProtocolObject.GetStateResponse
        > {

    @Override
    public ProtocolObject<ChronomatronGetStateProtocolObject.GetStateMessage, ChronomatronGetStateProtocolObject.GetStateResponse> associatedProtocolObject() {
        return new ChronomatronGetStateProtocolObject();
    }

    @Override
    public ChronomatronGetStateProtocolObject.GetStateResponse onMessage(ServiceProxyRequest message, ChronomatronGetStateProtocolObject.GetStateMessage messageObject) {
        UUID playerUUID = messageObject.playerUUID;
        GameSession session = ChronomatronStartEndpoint.get(playerUUID);
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState state)) {
            return new ChronomatronGetStateProtocolObject.GetStateResponse(false, new ArrayList<>(), 0, "no-active-session");
        }
        return new ChronomatronGetStateProtocolObject.GetStateResponse(true, new ArrayList<>(state.correctSequence), state.playerInputPosition, null);
    }
}


