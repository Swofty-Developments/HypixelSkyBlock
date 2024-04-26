package net.swofty.service.itemtracker;

import net.swofty.commons.Configuration;
import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class ItemTrackerService implements SkyBlockService {
    public static void main(String[] args) {
        SkyBlockService.init(new ItemTrackerService());

        TrackedItemsDatabase.connect(Configuration.get("mongodb"));
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
