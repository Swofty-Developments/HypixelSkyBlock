package net.swofty.service.darkauction.endpoints;

import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.darkauction.PlaceBidProtocol;
import net.swofty.service.darkauction.DarkAuctionScheduler;
import net.swofty.service.darkauction.DarkAuctionService;
import net.swofty.service.darkauction.DarkAuctionState;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.UUID;

public class EndpointPlaceBid implements ServiceEndpoint<
        PlaceBidProtocol.PlaceBidMessage,
        PlaceBidProtocol.PlaceBidResponse> {

    @Override
    public ProtocolObject<PlaceBidProtocol.PlaceBidMessage, PlaceBidProtocol.PlaceBidResponse> associatedProtocolObject() {
        return new PlaceBidProtocol();
    }

    @Override
    public PlaceBidProtocol.PlaceBidResponse onMessage(
            ServiceProxyRequest request,
            PlaceBidProtocol.PlaceBidMessage msg) {

        DarkAuctionState auction = DarkAuctionService.getCurrentAuction();

        // Check if there's an active auction
        if (auction == null) {
            Logger.warn("Bid rejected: No active auction");
            return new PlaceBidProtocol.PlaceBidResponse(false, "No active auction");
        }

        // Check if auction ID matches
        if (!auction.getAuctionId().equals(msg.auctionId())) {
            Logger.warn("Bid rejected: Auction ID mismatch");
            return new PlaceBidProtocol.PlaceBidResponse(false, "Invalid auction ID");
        }

        // Check if we're in bidding phase
        if (auction.getPhase() != DarkAuctionPhase.BIDDING) {
            Logger.warn("Bid rejected: Not in bidding phase (current: {})", auction.getPhase());
            return new PlaceBidProtocol.PlaceBidResponse(false, "Auction is not in bidding phase");
        }

        // SYNCHRONIZED to prevent race conditions
        synchronized (auction) {
            // Validate bid is higher than current
            if (msg.bidAmount() <= auction.getCurrentBid()) {
                Logger.info("Bid rejected: {} tried to bid {} but current bid is {}",
                        msg.playerName(), msg.bidAmount(), auction.getCurrentBid());
                return new PlaceBidProtocol.PlaceBidResponse(false,
                        "Bid too low - someone else bid first! Current bid is " + auction.getCurrentBid());
            }

            // If there's a previous bidder, queue their refund
            UUID previousBidder = auction.getHighestBidderId();
            long previousBid = auction.getCurrentBid();

            if (previousBidder != null && previousBid > 0) {
                Logger.info("Queueing refund of {} to previous bidder {}", previousBid, previousBidder);
                auction.queueRefund(previousBidder, previousBid);
            }

            // Update state with new bid
            auction.setCurrentBid(msg.bidAmount());
            auction.setHighestBidderId(msg.playerId());
            auction.setHighestBidderName(msg.playerName());
            auction.setEscrowedAmount(msg.playerId(), msg.bidAmount());

            // Record in bid history for reconciliation
            auction.recordBid(msg.playerId(), msg.playerName(), msg.bidAmount());

            Logger.info("Bid accepted: {} bid {} coins for {}",
                    msg.playerName(), msg.bidAmount(), auction.getCurrentItem());

            // Reset countdown timer
            DarkAuctionScheduler.resetRoundTimer(auction);

            // Broadcast BID_PLACED with refund info
            DarkAuctionScheduler.broadcastBidPlaced(auction, previousBidder, previousBid);

            return new PlaceBidProtocol.PlaceBidResponse(true, "Bid accepted");
        }
    }
}
