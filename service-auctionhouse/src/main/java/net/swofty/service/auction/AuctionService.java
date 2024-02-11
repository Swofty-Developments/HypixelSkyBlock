package net.swofty.service.auction;

import net.swofty.commons.Configuration;
import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class AuctionService implements SkyBlockService {
    public static AuctionsCacheService cacheService;

    public static void main(String[] args) {
        SkyBlockService.init(new AuctionService());

        cacheService = new AuctionsCacheService();

        new AuctionActiveDatabase("_placeholder").connect(Configuration.get("mongodb"));
        new AuctionInactiveDatabase("_placeholder").connect(Configuration.get("mongodb"));
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
