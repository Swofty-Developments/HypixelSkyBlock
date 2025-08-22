package net.swofty.service.experimentation.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronStartProtocolObject;
import net.swofty.service.experimentation.data.GameSession;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChronomatronStartEndpoint implements ServiceEndpoint<
        ChronomatronStartProtocolObject.StartMessage,
        ChronomatronStartProtocolObject.StartResponse
        > {

    private static final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();

    @Override
    public ProtocolObject<ChronomatronStartProtocolObject.StartMessage, ChronomatronStartProtocolObject.StartResponse> associatedProtocolObject() {
        return new ChronomatronStartProtocolObject();
    }

    @Override
    public ChronomatronStartProtocolObject.StartResponse onMessage(ServiceProxyRequest message, ChronomatronStartProtocolObject.StartMessage messageObject) {
        UUID playerUUID = messageObject.playerUUID;

        GameSession session = new GameSession();
        session.setPlayerUUID(playerUUID);
        session.setGameType(GameSession.GameType.CHRONOMATRON);
        session.setTier(messageObject.tier);
        session.setStartTime(System.currentTimeMillis());

        GameSession.ChronomatronState state = new GameSession.ChronomatronState();
        // Hypixel shows a sequence that grows by 1 each round; here we seed the first few notes
        Random r = new Random();
        for (int i = 0; i < 3; i++) state.correctSequence.add(r.nextInt(3));
        session.setChronomatronBestChain(0);
        session.setGameState(state);

        sessions.put(playerUUID, session);

        return new ChronomatronStartProtocolObject.StartResponse(true, null);
    }

    public static GameSession get(UUID playerUUID) {
        return sessions.get(playerUUID);
    }
}


