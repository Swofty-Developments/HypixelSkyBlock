package net.swofty.service.punishment;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.io.ObjectInputFilter;
import java.util.List;

public class PunishmentService implements SkyBlockService {

    static void main() {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new PunishmentDatabase(null).connect(mongoUri);
        SkyBlockService.init(new PunishmentService());
        PunishmentRedis.connect(ConfigProvider.settings().getRedisUri());
        ProxyRedis.connect(ConfigProvider.settings().getRedisUri());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.PUNISHMENT;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.punishment.endpoints", ServiceEndpoint.class).toList();
    }
}
