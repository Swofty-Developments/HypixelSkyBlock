package net.swofty.commons.protocol.objects.darkauction;

import net.swofty.commons.protocol.ServicePushProtocol;

import java.util.List;
import java.util.UUID;

public class DarkAuctionEventPushProtocol
        extends ServicePushProtocol<DarkAuctionEventPushProtocol.Request, DarkAuctionEventPushProtocol.Response> {

    public DarkAuctionEventPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(
            String eventType,
            String auctionId,
            String phase,
            int currentRound,
            String currentItemType,
            long currentBid,
            String highestBidderId,
            String highestBidderName,
            int countdown,
            List<String> roundItems,
            String refundPlayerId,
            long refundAmount,
            String newBidderId,
            long newBidAmount
    ) {}

    public record Response(boolean success, int playersInAuction, String error) {
        public static Response success(int playersInAuction) {
            return new Response(true, playersInAuction, null);
        }

        public static Response failure(String reason) {
            return new Response(false, 0, reason);
        }
    }
}
