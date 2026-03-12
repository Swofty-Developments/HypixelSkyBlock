package net.swofty.service.jacobscontest;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class JacobsContestService implements SkyBlockService {
    static void main(String[] args) {
        SkyBlockService.init(new JacobsContestService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.JACOBS_CONTEST;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.jacobscontest.endpoints", ServiceEndpoint.class).toList();
    }
}
