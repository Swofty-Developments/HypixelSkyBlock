package net.swofty.service.darkauction.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.darkauction.GetAuctionStateProtocol;
import net.swofty.service.darkauction.DarkAuctionService;
import net.swofty.service.darkauction.DarkAuctionState;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class EndpointGetAuctionState implements ServiceEndpoint<
        GetAuctionStateProtocol.GetAuctionStateMessage,
        GetAuctionStateProtocol.GetAuctionStateResponse> {

    @Override
    public ProtocolObject<GetAuctionStateProtocol.GetAuctionStateMessage, GetAuctionStateProtocol.GetAuctionStateResponse> associatedProtocolObject() {
        return new GetAuctionStateProtocol();
    }

    @Override
    public GetAuctionStateProtocol.GetAuctionStateResponse onMessage(
            ServiceProxyRequest request,
            GetAuctionStateProtocol.GetAuctionStateMessage msg) {

        DarkAuctionState auction = DarkAuctionService.getCurrentAuction();

        if (auction == null) {
            return new GetAuctionStateProtocol.GetAuctionStateResponse(
                    false, null, null, 0, null, 0, null, null, List.of()
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
                auction.getRoundItemNames()
        );
    }
}
