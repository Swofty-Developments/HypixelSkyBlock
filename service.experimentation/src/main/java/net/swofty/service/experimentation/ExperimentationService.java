package net.swofty.service.experimentation;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class ExperimentationService implements SkyBlockService {

    public static void main(String[] args) {
        SkyBlockService.init(new ExperimentationService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.EXPERIMENTATION;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.experimentation.endpoints", ServiceEndpoint.class).toList();
    }
}

