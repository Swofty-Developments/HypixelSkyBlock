package net.swofty.service.replay;

import lombok.Getter;
import net.swofty.commons.ServiceType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.replay.session.ReplaySessionManager;
import net.swofty.service.replay.storage.ReplayDatabase;
import org.tinylog.Logger;

import java.util.List;

public class ReplayService implements SkyBlockService {

    @Getter
	private static ReplayDatabase database;
    @Getter
	private static ReplaySessionManager sessionManager;

    static void main() {
        Logger.info("Starting Replay Service...");

        String mongoUri = ConfigProvider.settings().getMongodb();
        database = new ReplayDatabase();
        database.connect(mongoUri);

        sessionManager = new ReplaySessionManager(database);
        sessionManager.startCleanupTask();

        SkyBlockService.init(new ReplayService());

        Logger.info("Replay Service started successfully");
    }

	@Override
    public ServiceType getType() {
        return ServiceType.REPLAY;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.replay.endpoints", ServiceEndpoint.class).toList();
    }
}
