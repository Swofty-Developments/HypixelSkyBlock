package net.swofty.service.bazaar;

import lombok.Getter;
import net.swofty.commons.Configuration;
import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class BazaarService implements SkyBlockService {
    @Getter
    public static BazaarCacheService cacheService;

    public static void main(String[] args) {
        SkyBlockService.init(new BazaarService());

        cacheService = new BazaarCacheService();
        new BazaarDatabase("_placeholder").connect(Configuration.get("mongodb"));
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
