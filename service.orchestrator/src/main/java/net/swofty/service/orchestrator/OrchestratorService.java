package net.swofty.service.orchestrator;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrchestratorService implements SkyBlockService {

	static void main() {
        SkyBlockService.init(new OrchestratorService());

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "orchestrator-cleanup");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(OrchestratorCache::cleanup, 5, 5, TimeUnit.SECONDS);
		Logger.info("Started orchestrator service");
	}

	@Override
	public ServiceType getType() {
		return ServiceType.ORCHESTRATOR;
	}

	@Override
	public List<ServiceEndpoint> getEndpoints() {
		return loopThroughPackage("net.swofty.service.orchestrator.endpoints", ServiceEndpoint.class).toList();
	}
}