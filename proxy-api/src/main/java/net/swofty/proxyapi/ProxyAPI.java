package net.swofty.proxyapi;

import net.swofty.commons.ServerType;
import net.swofty.proxyapi.redis.RedisMessage;
import net.swofty.redisapi.api.RedisAPI;
import org.tinylog.Logger;

import java.util.UUID;

public class ProxyAPI {
    public ProxyAPI(ServerType type, String URI, UUID serverUUID) {
        RedisAPI.generateInstance(URI);
        RedisAPI.getInstance().setFilterID(serverUUID.toString());

        registerChannels();

        RedisMessage.sendMessageToProxy("server-initialized", type.toString(), (response) -> {
            System.out.println("Received response: " + response);
        });
    }

    public static void registerChannels() {
        RedisMessage.register("server-initialized");
    }
}
