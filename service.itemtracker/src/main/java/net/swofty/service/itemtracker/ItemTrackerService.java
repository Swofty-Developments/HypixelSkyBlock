package net.swofty.service.itemtracker;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;

public class ItemTrackerService implements SkyBlockService {
    static void main(String[] args) {
        // Connect the DB before the service starts handling Redis requests,
        // otherwise early messages hit a null collection and NPE.
        TrackedItemsDatabase.connect(ConfigProvider.settings().getMongodb());

        SkyBlockService.init(new ItemTrackerService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.ITEM_TRACKER;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("net.swofty.service.itemtracker.endpoints", RedisMessageHandler.class).toList();
    }
}
