package net.swofty.service.experimentation.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronFinishProtocolObject;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.service.experimentation.data.GameSession;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

public class ChronomatronFinishEndpoint implements ServiceEndpoint<
        ChronomatronFinishProtocolObject.FinishMessage,
        ChronomatronFinishProtocolObject.FinishResponse
        > {

    @Override
    public ProtocolObject<ChronomatronFinishProtocolObject.FinishMessage, ChronomatronFinishProtocolObject.FinishResponse> associatedProtocolObject() {
        return new ChronomatronFinishProtocolObject();
    }

    @Override
    public ChronomatronFinishProtocolObject.FinishResponse onMessage(ServiceProxyRequest message, ChronomatronFinishProtocolObject.FinishMessage messageObject) {
        UUID playerUUID = messageObject.playerUUID;
        GameSession session = ChronomatronStartEndpoint.get(playerUUID);
        if (session == null) {
            return new ChronomatronFinishProtocolObject.FinishResponse(false, 0, 0, "no-active-session");
        }

        int bestChain = session.getChronomatronBestChain();
        int xpPerNote = xpPerNoteForTier(session.getTier());
        int capped = Math.min(bestChain, 15);
        int xpAward = xpPerNote * capped;

        int bonusClicks = (bestChain >= 12) ? 3 : (bestChain >= 9) ? 2 : (bestChain >= 5) ? 1 : 0;
        session.setBonusClicksEarned(bonusClicks);

        // Persist bonus clicks into player's experimentation datapoint
        try {
            JSONObject newData = new JSONObject().put("superpairs_bonus_clicks", bonusClicks);
            // Broadcast to all servers; whoever has the player will update
            ServiceToServerManager.sendToAllServers(FromServiceChannels.UPDATE_PLAYER_DATA,
                    new JSONObject()
                            .put("playerUUID", playerUUID.toString())
                            .put("dataKey", "experimentation")
                            .put("newData", newData.toString())
            );
        } catch (Exception ignored) {}

        return new ChronomatronFinishProtocolObject.FinishResponse(true, bestChain, xpAward, null);
    }

    private int xpPerNoteForTier(String tier) {
        if (tier == null) return 1500;
        String t = tier.toLowerCase();
        if (t.contains("meta")) return 6000;
        if (t.contains("trans")) return 4500;
        if (t.contains("supreme")) return 3500;
        if (t.contains("grand")) return 2500;
        return 1500; // high
    }
}


