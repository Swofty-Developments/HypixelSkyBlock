package net.swofty.service.experimentation.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronInputProtocolObject;
import net.swofty.service.experimentation.data.GameSession;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;
import java.util.UUID;

public class ChronomatronInputEndpoint implements ServiceEndpoint<
        ChronomatronInputProtocolObject.InputMessage,
        ChronomatronInputProtocolObject.InputResponse
        > {

    @Override
    public ProtocolObject<ChronomatronInputProtocolObject.InputMessage, ChronomatronInputProtocolObject.InputResponse> associatedProtocolObject() {
        return new ChronomatronInputProtocolObject();
    }

    @Override
    public ChronomatronInputProtocolObject.InputResponse onMessage(ServiceProxyRequest message, ChronomatronInputProtocolObject.InputMessage messageObject) {
        UUID playerUUID = messageObject.playerUUID;
        GameSession session = ChronomatronStartEndpoint.get(playerUUID);
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState state)) {
            return new ChronomatronInputProtocolObject.InputResponse(false, false, "no-active-session");
        }

        List<Integer> correct = state.correctSequence;
        List<Integer> inputs = messageObject.inputs;

        // Validate prefix match
        for (int i = 0; i < inputs.size(); i++) {
            if (!inputs.get(i).equals(correct.get(i))) {
                return new ChronomatronInputProtocolObject.InputResponse(false, true, "incorrect");
            }
        }

        state.playerInputPosition = inputs.size();
        boolean roundComplete = inputs.size() == correct.size();

        // Track longest chain for rewards later
        if (roundComplete) {
            int current = correct.size();
            if (current > session.getChronomatronBestChain()) session.setChronomatronBestChain(current);
        }

        return new ChronomatronInputProtocolObject.InputResponse(true, roundComplete, null);
    }
}


