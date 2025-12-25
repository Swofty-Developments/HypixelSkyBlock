package net.swofty.service.darkauction;

import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import net.swofty.commons.protocol.objects.darkauction.DarkAuctionEventProtocol;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.service.generic.redis.ServiceToServerManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Manages Dark Auction phases and timing.
 * This scheduler only manages the auction phases once triggered.
 */
public class DarkAuctionScheduler {
    // Auction timing constants (in seconds)
    private static final int QUEUE_DURATION_SECONDS = 35;
    private static final int AUCTION_START_DELAY_SECONDS = 10;
    private static final int ROUND_TIMER_SECONDS = 10;
    private static final int BETWEEN_ROUNDS_SECONDS = 5;

    private static ScheduledExecutorService executor;
    private static ScheduledFuture<?> roundTimerFuture;

    public static void start() {
        executor = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "dark-auction-scheduler");
            t.setDaemon(true);
            return t;
        });

        Logger.info("Dark Auction Scheduler started (waiting for calendar triggers)");
    }

    public static void startNewAuction() {
        Logger.info("Starting new Dark Auction!");

        DarkAuctionState auction = new DarkAuctionState();
        auction.setPhase(DarkAuctionPhase.QUEUE);
        DarkAuctionService.setCurrentAuction(auction);

        // Broadcast AUCTION_START to all hubs
        broadcastEvent(DarkAuctionEventProtocol.EventType.AUCTION_START, auction, 0);

        // Schedule auction begin after queue duration
        executor.schedule(() -> beginAuction(auction), QUEUE_DURATION_SECONDS, TimeUnit.SECONDS);
    }

    private static void beginAuction(DarkAuctionState auction) {
        if (auction != DarkAuctionService.getCurrentAuction()) {
            return; // Auction was cancelled
        }

        Logger.info("Auction queue phase complete, beginning bidding!");
        auction.setPhase(DarkAuctionPhase.BIDDING);
        auction.setCurrentRound(0);

        // Broadcast AUCTION_BEGIN
        broadcastEvent(DarkAuctionEventProtocol.EventType.AUCTION_BEGIN, auction, 0);

        // Start first round after delay (gives players time to get to basement)
        executor.schedule(() -> startRound(auction), AUCTION_START_DELAY_SECONDS, TimeUnit.SECONDS);
    }

    private static void startRound(DarkAuctionState auction) {
        if (auction != DarkAuctionService.getCurrentAuction()) {
            return; // Auction was cancelled
        }

        auction.resetForNextRound();
        auction.setPhase(DarkAuctionPhase.BIDDING);

        Logger.info("Starting round {} with item {}", auction.getCurrentRound() + 1, auction.getCurrentItem());

        // Broadcast ROUND_START
        broadcastEvent(DarkAuctionEventProtocol.EventType.ROUND_START, auction, ROUND_TIMER_SECONDS);

        // Schedule round end
        scheduleRoundEnd(auction, ROUND_TIMER_SECONDS);
    }

    public static void resetRoundTimer(DarkAuctionState auction) {
        // Cancel existing timer
        if (roundTimerFuture != null && !roundTimerFuture.isDone()) {
            roundTimerFuture.cancel(false);
        }

        // Reschedule for 10 more seconds
        scheduleRoundEnd(auction, ROUND_TIMER_SECONDS);
    }

    private static void scheduleRoundEnd(DarkAuctionState auction, int seconds) {
        roundTimerFuture = executor.schedule(() -> endRound(auction), seconds, TimeUnit.SECONDS);
    }

    private static void endRound(DarkAuctionState auction) {
        if (auction != DarkAuctionService.getCurrentAuction()) {
            return; // Auction was cancelled
        }

        Logger.info("Round {} ended. Winner: {}, Bid: {}",
                auction.getCurrentRound() + 1,
                auction.getHighestBidderName(),
                auction.getCurrentBid());

        // Broadcast ROUND_END
        broadcastEvent(DarkAuctionEventProtocol.EventType.ROUND_END, auction, 0);

        // Check if more rounds
        if (auction.hasMoreRounds()) {
            auction.setPhase(DarkAuctionPhase.BETWEEN);

            // Schedule next round
            executor.schedule(() -> {
                auction.advanceToNextRound();
                startRound(auction);
            }, BETWEEN_ROUNDS_SECONDS, TimeUnit.SECONDS);
        } else {
            // Auction complete
            endAuction(auction);
        }
    }

    private static void endAuction(DarkAuctionState auction) {
        Logger.info("Dark Auction complete!");
        auction.setPhase(DarkAuctionPhase.COMPLETE);

        // Broadcast AUCTION_END
        broadcastEvent(DarkAuctionEventProtocol.EventType.AUCTION_END, auction, 0);

        // Clear auction
        DarkAuctionService.setCurrentAuction(null);
    }

    public static void broadcastEvent(DarkAuctionEventProtocol.EventType type, DarkAuctionState auction, int countdown) {
        broadcastEvent(type, auction, countdown, null, 0);
    }

    public static void broadcastBidPlaced(DarkAuctionState auction, UUID refundPlayerId, long refundAmount) {
        broadcastEvent(DarkAuctionEventProtocol.EventType.BID_PLACED, auction, ROUND_TIMER_SECONDS,
                refundPlayerId, refundAmount);
    }

    private static void broadcastEvent(DarkAuctionEventProtocol.EventType type, DarkAuctionState auction,
                                       int countdown, UUID refundPlayerId, long refundAmount) {
        JSONObject message = new JSONObject();
        message.put("eventType", type.name());
        message.put("auctionId", auction.getAuctionId().toString());
        message.put("phase", auction.getPhase().name());
        message.put("currentRound", auction.getCurrentRound());
        message.put("currentItemType", auction.getCurrentItem() != null ? auction.getCurrentItem().name() : null);
        message.put("currentBid", auction.getCurrentBid());
        message.put("highestBidderId", auction.getHighestBidderId() != null ? auction.getHighestBidderId().toString() : null);
        message.put("highestBidderName", auction.getHighestBidderName());
        message.put("countdown", countdown);

        JSONArray itemsArray = new JSONArray();
        for (String item : auction.getRoundItemNames()) {
            itemsArray.put(item);
        }
        message.put("roundItems", itemsArray);

        // Include refund info for BID_PLACED events
        if (refundPlayerId != null) {
            message.put("refundPlayerId", refundPlayerId.toString());
            message.put("refundAmount", refundAmount);
        }

        // Include new bidder info for coin handling
        if (type == DarkAuctionEventProtocol.EventType.BID_PLACED) {
            message.put("newBidderId", auction.getHighestBidderId().toString());
            message.put("newBidAmount", auction.getCurrentBid());
        }

        Logger.debug("Broadcasting {} event for auction {}", type, auction.getAuctionId());

        ServiceToServerManager.sendToAllServers(
                FromServiceChannels.DARK_AUCTION_EVENT,
                message,
                5000
        ).thenAccept(responses -> {
            Logger.debug("Received {} responses for {} event", responses.size(), type);
        }).exceptionally(throwable -> {
            Logger.error("Error broadcasting {} event: {}", type, throwable.getMessage());
            return null;
        });
    }

}
