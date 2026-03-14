package net.swofty.service.bazaar;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class BazaarService implements SkyBlockService {

    public static void main(String[] args) {
        SkyBlockService.init(new BazaarService());

        // Connect to MongoDB for orders
        OrderDatabase.connect(ConfigProvider.settings().getMongodb());

        // Initialize the bazaar market (loads existing orders)
        BazaarMarket.get();
        // load persisted orders
        OrderRepository.loadAll();
    }

    @Override
    public ServiceType getType() {
        return ServiceType.BAZAAR;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.bazaar.endpoints", ServiceEndpoint.class).toList();
    }
}