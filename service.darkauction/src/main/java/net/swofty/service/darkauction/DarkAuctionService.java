package net.swofty.service.darkauction;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.List;

public class DarkAuctionService implements SkyBlockService {
    private static DarkAuctionState currentAuction = null;

    public static void main(String[] args) {
        SkyBlockService.init(new DarkAuctionService());

        // Start the scheduler that checks SkyBlock time
        DarkAuctionScheduler.start();
        Logger.info("DarkAuctionService started with scheduler");
    }

    @Override
    public ServiceType getType() {
        return ServiceType.DARK_AUCTION;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.darkauction.endpoints", ServiceEndpoint.class).toList();
    }

    public static DarkAuctionState getCurrentAuction() {
        return currentAuction;
    }

    public static void setCurrentAuction(DarkAuctionState auction) {
        currentAuction = auction;
    }

    public static boolean hasActiveAuction() {
        return currentAuction != null;
    }
}
