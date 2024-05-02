package net.swofty.types.generic.redis;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.proxyapi.redis.ProxyToClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisOriginServer implements ProxyToClient {
    public static Map<UUID, ServerType> origin = new HashMap<>();

    @Override
    public String onMessage(String message) {
        UUID uuid = UUID.fromString(message.split(":")[0]);
        ServerType serverType = ServerType.valueOf(message.split(":")[1]);

        origin.put(uuid, serverType);

        MinecraftServer.getSchedulerManager().scheduleTask(() -> origin.remove(uuid),
                TaskSchedule.seconds(3), TaskSchedule.stop());
        return "true";
    }
}
