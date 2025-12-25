package net.swofty.service.darkauction.endpoints;

import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.darkauction.PlayerLeftAuctionProtocol;
import net.swofty.service.darkauction.DarkAuctionScheduler;
import net.swofty.service.darkauction.DarkAuctionService;
import net.swofty.service.darkauction.DarkAuctionState;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.UUID;

public class EndpointPlayerLeftAuction implements ServiceEndpoint<
        PlayerLeftAuctionProtocol.PlayerLeftMessage,
        PlayerLeftAuctionProtocol.PlayerLeftResponse> {

    @Override
    public ProtocolObject<PlayerLeftAuctionProtocol.PlayerLeftMessage, PlayerLeftAuctionProtocol.PlayerLeftResponse> associatedProtocolObject() {
        return new PlayerLeftAuctionProtocol();
    }

    @Override
    public PlayerLeftAuctionProtocol.PlayerLeftResponse onMessage(
            ServiceProxyRequest request,
            PlayerLeftAuctionProtocol.PlayerLeftMessage msg) {

        DarkAuctionState auction = DarkAuctionService.getCurrentAuction();

        // If no active auction, nothing to clean up
        if (auction == null) {
            Logger.debug("Player {} left but no active auction", msg.playerName());
            return new PlayerLeftAuctionProtocol.PlayerLeftResponse(true, 0);
        }

        // If auction ID was provided and doesn't match, ignore
        if (msg.auctionId() != null && !auction.getAuctionId().equals(msg.auctionId())) {
            Logger.debug("Player {} left but auction ID mismatch", msg.playerName());
            return new PlayerLeftAuctionProtocol.PlayerLeftResponse(true, 0);
        }

        synchronized (auction) {
            UUID playerId = msg.playerId();
            long refundAmount = 0;

            // Check if player has escrowed coins (was bidding)
            Long escrowed = auction.getEscrowedCoins().get(playerId);

            // If they were the highest bidder, we need to handle reconciliation
            if (playerId.equals(auction.getHighestBidderId())) {
                Logger.info("Highest bidder {} left the auction during phase {}",
                        msg.playerName(), auction.getPhase());

                if (escrowed != null && escrowed > 0) {
                    refundAmount = escrowed;
                }

                // Remove their bids from history
                auction.removeBidsFromPlayer(playerId);
                auction.getEscrowedCoins().remove(playerId);

                // Find the next highest bidder
                DarkAuctionState.BidEntry nextBidder = auction.findNextHighestBidder(playerId);

                if (nextBidder != null) {
                    // Promote the runner-up to highest bidder
                    Logger.info("Promoting {} as new highest bidder with bid {}",
                            nextBidder.playerName(), nextBidder.bidAmount());

                    auction.setHighestBidderId(nextBidder.playerId());
                    auction.setHighestBidderName(nextBidder.playerName());
                    auction.setCurrentBid(nextBidder.bidAmount());

                    // Set escrow for the promoted bidder (their coins will be re-taken via broadcast)
                    auction.setEscrowedAmount(nextBidder.playerId(), nextBidder.bidAmount());

                    // Broadcast the change so all servers know about the new highest bidder
                    // This will also trigger coin deduction from the promoted player
                    if (auction.getPhase() == DarkAuctionPhase.BIDDING) {
                        DarkAuctionScheduler.broadcastBidPlaced(auction, null, 0);
                    }
                } else {
                    // No other bidders - reset the round bid state to starting bid
                    Logger.info("No other bidders found, resetting bid to starting bid");
                    auction.setHighestBidderId(null);
                    auction.setHighestBidderName(null);
                    auction.setCurrentBid(DarkAuctionState.STARTING_BID);

                    // Broadcast the reset
                    if (auction.getPhase() == DarkAuctionPhase.BIDDING) {
                        DarkAuctionScheduler.broadcastBidPlaced(auction, null, 0);
                    }
                }
            } else if (escrowed != null && escrowed > 0) {
                // They had some escrow but weren't highest bidder (shouldn't happen normally,
                // but handle it anyway)
                refundAmount = escrowed;
                auction.getEscrowedCoins().remove(playerId);
                auction.removeBidsFromPlayer(playerId);
            } else {
                // Player was just in the queue or spectating, just remove from history
                auction.removeBidsFromPlayer(playerId);
            }

            Logger.info("Player {} left Dark Auction, refund amount: {}", msg.playerName(), refundAmount);

            return new PlayerLeftAuctionProtocol.PlayerLeftResponse(true, refundAmount);
        }
    }
}
