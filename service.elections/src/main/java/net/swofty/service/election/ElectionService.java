package net.swofty.service.election;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class ElectionService implements SkyBlockService {

    static void main(String[] args) {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new ElectionDatabase(null).connect(mongoUri);
        SkyBlockService.init(new ElectionService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.ELECTION;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.election.endpoints", ServiceEndpoint.class).toList();
    }
}
