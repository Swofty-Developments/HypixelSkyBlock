package net.swofty.service.guild;

import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class GuildService implements SkyBlockService {

    static void main() {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new GuildDatabase(null).connect(mongoUri);

        SkyBlockService.init(new GuildService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.GUILD;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.guild.endpoints", ServiceEndpoint.class).toList();
    }
}
