package net.swofty.service.itemtracker;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class ItemTrackerService implements SkyBlockService {
    static void main(String[] args) {
        SkyBlockService.init(new ItemTrackerService());

        TrackedItemsDatabase.connect(ConfigProvider.settings().getMongodb());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.ITEM_TRACKER;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.itemtracker.endpoints", ServiceEndpoint.class).toList();
    }
}
