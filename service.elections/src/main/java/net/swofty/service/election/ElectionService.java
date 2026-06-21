package net.swofty.service.election;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;

public class ElectionService implements SkyBlockService {

    public static void main(String[] args) {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new ElectionDatabase(null).connect(mongoUri);
        SkyBlockService.init(new ElectionService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.ELECTION;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("net.swofty.service.election.endpoints", RedisMessageHandler.class).toList();
    }
}
