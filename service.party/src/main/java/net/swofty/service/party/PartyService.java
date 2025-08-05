package net.swofty.service.party;

import net.swofty.commons.Configuration;
import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class PartyService implements SkyBlockService {

    public static void main(String[] args) {
        SkyBlockService.init(new PartyService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.PARTY;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.party.endpoints", ServiceEndpoint.class).toList();
    }
}