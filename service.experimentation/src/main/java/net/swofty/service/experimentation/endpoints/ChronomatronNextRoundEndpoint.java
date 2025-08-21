package net.swofty.service.experimentation.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronNextRoundProtocolObject;
import net.swofty.service.experimentation.data.GameSession;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.Random;
import java.util.UUID;

public class ChronomatronNextRoundEndpoint implements ServiceEndpoint<
        ChronomatronNextRoundProtocolObject.NextRoundMessage,
        ChronomatronNextRoundProtocolObject.NextRoundResponse
        > {

    @Override
    public ProtocolObject<ChronomatronNextRoundProtocolObject.NextRoundMessage, ChronomatronNextRoundProtocolObject.NextRoundResponse> associatedProtocolObject() {
        return new ChronomatronNextRoundProtocolObject();
    }

    @Override
    public ChronomatronNextRoundProtocolObject.NextRoundResponse onMessage(ServiceProxyRequest message, ChronomatronNextRoundProtocolObject.NextRoundMessage messageObject) {
        UUID playerUUID = messageObject.playerUUID;
        GameSession session = ChronomatronStartEndpoint.get(playerUUID);
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState state)) {
            return new ChronomatronNextRoundProtocolObject.NextRoundResponse(false, "no-active-session");
        }
        state.playerInputPosition = 0;
        state.correctSequence.add(new Random().nextInt(3));
        // update best chain if needed after round completion was recorded in Input endpoint
        return new ChronomatronNextRoundProtocolObject.NextRoundResponse(true, null);
    }
}


