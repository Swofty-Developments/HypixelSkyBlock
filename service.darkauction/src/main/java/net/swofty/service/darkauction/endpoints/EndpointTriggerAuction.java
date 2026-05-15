package net.swofty.service.darkauction.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.darkauction.TriggerDarkAuctionProtocol;
import net.swofty.service.darkauction.DarkAuctionScheduler;
import net.swofty.service.darkauction.DarkAuctionService;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointTriggerAuction implements RedisMessageHandler<
        TriggerDarkAuctionProtocol.TriggerMessage,
        TriggerDarkAuctionProtocol.TriggerResponse> {

    @Override
    public RedisProtocol<TriggerDarkAuctionProtocol.TriggerMessage, TriggerDarkAuctionProtocol.TriggerResponse> protocol() {
        return new TriggerDarkAuctionProtocol();
    }

    @Override
    public TriggerDarkAuctionProtocol.TriggerResponse handle(TriggerDarkAuctionProtocol.TriggerMessage msg, RedisMessageContext context) {

        Logger.info("Received trigger request - calendarTime: {}, forced: {}", msg.calendarTime(), msg.forced());

        // Check if auction is already active (deduplication)
        if (DarkAuctionService.hasActiveAuction()) {
            Logger.info("Auction already active, ignoring trigger");
            return new TriggerDarkAuctionProtocol.TriggerResponse(false, "Auction already in progress", null);
        }

        // Start the auction
        try {
            DarkAuctionScheduler.startNewAuction();
            Logger.info("Dark Auction started successfully via calendar trigger");
            return new TriggerDarkAuctionProtocol.TriggerResponse(true, "Auction started", null);
        } catch (Exception e) {
            Logger.error(e, "Failed to start Dark Auction");
            return new TriggerDarkAuctionProtocol.TriggerResponse(false, "Failed to start: " + e.getMessage(), null);
        }
    }
}
