package net.swofty.service.darkauction;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.List;

public class DarkAuctionService implements SkyBlockService {
    private static DarkAuctionState currentAuction = null;

    static void main(String[] args) {
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
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("net.swofty.service.darkauction.endpoints", RedisMessageHandler.class).toList();
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
