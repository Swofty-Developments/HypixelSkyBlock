package net.swofty.service.auction;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class AuctionService implements SkyBlockService {
    public static AuctionsCacheService cacheService;

    public static void main(String[] args) {
        SkyBlockService.init(new AuctionService());

        cacheService = new AuctionsCacheService();

        new AuctionActiveDatabase("_placeholder").connect(ConfigProvider.settings().getMongodb());
        new AuctionInactiveDatabase("_placeholder").connect(ConfigProvider.settings().getMongodb());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.AUCTION_HOUSE;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.auction.endpoints", ServiceEndpoint.class).toList();
    }
}
