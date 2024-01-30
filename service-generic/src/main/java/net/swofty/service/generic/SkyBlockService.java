package net.swofty.service.generic;

import net.swofty.commons.ServiceType;

public interface SkyBlockService {
    ServiceType getType();

    static void init(SkyBlockService service) {
        new ServiceInitializer(service).init();
    }
}
