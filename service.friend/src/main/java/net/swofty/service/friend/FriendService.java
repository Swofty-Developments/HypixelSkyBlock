package net.swofty.service.friend;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class FriendService implements SkyBlockService {

    static void main(String[] args) {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new FriendDatabase(null).connect(mongoUri);

        FriendCache.startExpirationChecker();

        SkyBlockService.init(new FriendService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.FRIEND;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.friend.endpoints", ServiceEndpoint.class).toList();
    }
}
