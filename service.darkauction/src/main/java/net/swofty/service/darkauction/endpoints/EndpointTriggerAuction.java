package net.swofty.service.darkauction.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.darkauction.TriggerDarkAuctionProtocol;
import net.swofty.service.darkauction.DarkAuctionScheduler;
import net.swofty.service.darkauction.DarkAuctionService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

public class EndpointTriggerAuction implements ServiceEndpoint<
        TriggerDarkAuctionProtocol.TriggerMessage,
        TriggerDarkAuctionProtocol.TriggerResponse> {

    @Override
    public ProtocolObject<TriggerDarkAuctionProtocol.TriggerMessage, TriggerDarkAuctionProtocol.TriggerResponse> associatedProtocolObject() {
        return new TriggerDarkAuctionProtocol();
    }

    @Override
    public TriggerDarkAuctionProtocol.TriggerResponse onMessage(
            ServiceProxyRequest request,
            TriggerDarkAuctionProtocol.TriggerMessage msg) {

        Logger.info("Received trigger request - calendarTime: {}, forced: {}", msg.calendarTime(), msg.forced());

        // Check if auction is already active (deduplication)
        if (DarkAuctionService.hasActiveAuction()) {
            Logger.info("Auction already active, ignoring trigger");
            return new TriggerDarkAuctionProtocol.TriggerResponse(false, "Auction already in progress");
        }

        // Start the auction
        try {
            DarkAuctionScheduler.startNewAuction();
            Logger.info("Dark Auction started successfully via calendar trigger");
            return new TriggerDarkAuctionProtocol.TriggerResponse(true, "Auction started");
        } catch (Exception e) {
            Logger.error(e, "Failed to start Dark Auction");
            return new TriggerDarkAuctionProtocol.TriggerResponse(false, "Failed to start: " + e.getMessage());
        }
    }
}
