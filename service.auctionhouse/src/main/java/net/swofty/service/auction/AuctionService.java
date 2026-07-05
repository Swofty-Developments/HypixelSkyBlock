package net.swofty.service.auction;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;

public class AuctionService implements SkyBlockService {
    public static AuctionsCacheService cacheService;

    static void main(String[] args) {
        // SkyBlockService.init(...) blocks forever (it awaits a latch), so every dependency the
        // endpoints touch must be initialised BEFORE it is called. Databases first (the cache reads
        // their static collections), then the cache, then start handling messages.
        new AuctionActiveDatabase("_placeholder").connect(ConfigProvider.settings().getMongodb());
        new AuctionInactiveDatabase("_placeholder").connect(ConfigProvider.settings().getMongodb());

        cacheService = new AuctionsCacheService();

        SkyBlockService.init(new AuctionService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.AUCTION_HOUSE;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("net.swofty.service.auction.endpoints", RedisMessageHandler.class).toList();
    }
}
