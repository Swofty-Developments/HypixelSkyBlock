package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.objects.darkauction.DarkAuctionEventProtocol;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.darkauction.DarkAuctionHandler;
import org.json.JSONObject;
import org.tinylog.Logger;

/**
 * Redis handler for Dark Auction events.
 * Only responsible for receiving and routing messages to DarkAuctionHandler.
 */
public class RedisDarkAuctionEvent implements ServiceToClient {

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.DARK_AUCTION_EVENT;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        if (HypixelConst.getTypeLoader().getType() != ServerType.SKYBLOCK_HUB) {
            return null;
        }

        try {
            String eventTypeStr = message.getString("eventType");
            DarkAuctionEventProtocol.EventType eventType = DarkAuctionEventProtocol.EventType.valueOf(eventTypeStr);

            Logger.info("Received Dark Auction event: {}", eventType);

            switch (eventType) {
                case AUCTION_START -> DarkAuctionHandler.handleAuctionStart(message);
                case AUCTION_BEGIN -> DarkAuctionHandler.handleAuctionBegin(message);
                case ROUND_START -> DarkAuctionHandler.handleRoundStart(message);
                case BID_PLACED -> DarkAuctionHandler.handleBidPlaced(message);
                case ROUND_END -> DarkAuctionHandler.handleRoundEnd(message);
                case AUCTION_END -> DarkAuctionHandler.handleAuctionEnd(message);
            }

            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("playersInAuction", DarkAuctionHandler.getPlayersInAuctionCount());
            return response;

        } catch (Exception e) {
            Logger.error(e, "Error handling Dark Auction event");
            JSONObject response = new JSONObject();
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }
}
