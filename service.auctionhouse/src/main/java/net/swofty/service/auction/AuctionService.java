package net.swofty.service.auction;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;

public class AuctionService implements SkyBlockService {
    public static AuctionsCacheService cacheService;

    static void main(String[] args) {
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
