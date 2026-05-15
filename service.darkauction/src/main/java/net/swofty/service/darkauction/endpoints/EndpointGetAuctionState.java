package net.swofty.service.darkauction.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.darkauction.GetAuctionStateProtocol;
import net.swofty.service.darkauction.DarkAuctionService;
import net.swofty.service.darkauction.DarkAuctionState;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointGetAuctionState implements RedisMessageHandler<
        GetAuctionStateProtocol.GetAuctionStateMessage,
        GetAuctionStateProtocol.GetAuctionStateResponse> {

    @Override
    public RedisProtocol<GetAuctionStateProtocol.GetAuctionStateMessage, GetAuctionStateProtocol.GetAuctionStateResponse> protocol() {
        return new GetAuctionStateProtocol();
    }

    @Override
    public GetAuctionStateProtocol.GetAuctionStateResponse handle(GetAuctionStateProtocol.GetAuctionStateMessage msg, RedisMessageContext context) {

        DarkAuctionState auction = DarkAuctionService.getCurrentAuction();

        if (auction == null) {
            return new GetAuctionStateProtocol.GetAuctionStateResponse(
                    false, null, null, 0, null, 0, null, null, List.of(), true, null
            );
        }

        return new GetAuctionStateProtocol.GetAuctionStateResponse(
                true,
                auction.getAuctionId(),
                auction.getPhase(),
                auction.getCurrentRound(),
                auction.getCurrentItem() != null ? auction.getCurrentItem().name() : null,
                auction.getCurrentBid(),
                auction.getHighestBidderId(),
                auction.getHighestBidderName(),
                auction.getRoundItemNames(),
                true,
                null
        );
    }
}
