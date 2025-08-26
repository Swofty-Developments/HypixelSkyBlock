package net.swofty.service.orchestrator;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class OrchestratorService implements SkyBlockService {

	public static void main(String[] args) {
		SkyBlockService.init(new OrchestratorService());
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