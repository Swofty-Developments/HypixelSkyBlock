package net.swofty.service.datamutex;

import net.swofty.commons.ServiceType;
import net.swofty.service.datamutex.endpoints.SynchronizeDataEndpoint;
import net.swofty.service.datamutex.endpoints.UnlockDataEndpoint;
import net.swofty.service.datamutex.endpoints.UpdateSynchronizedDataEndpoint;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class DataMutexService implements SkyBlockService {
    @Override
    public ServiceType getType() {
        return ServiceType.DATA_MUTEX;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return List.of(
                new SynchronizeDataEndpoint(),
                new UpdateSynchronizedDataEndpoint(),
                new UnlockDataEndpoint()
        );
    }

    public static void main(String[] args) {
        SkyBlockService.init(new DataMutexService());
    }
}