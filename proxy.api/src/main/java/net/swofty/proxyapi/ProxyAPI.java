package net.swofty.proxyapi;

import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.RedisMessage;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;

import java.util.Arrays;
import java.util.UUID;

public class ProxyAPI {
    public ProxyAPI(String URI, UUID serverUUID, String... toRegister) {
        RedisAPI.generateInstance(URI);
        RedisAPI.getInstance().setFilterID(serverUUID.toString());

        registerChannels(toRegister);
    }

    public void registerProxyToClient(String channelID, Class<? extends ProxyToClient> clazz) {
        RedisAPI.getInstance().registerChannel(channelID, (event) -> {
            String[] split = event.message.split("}=-=-=\\{");
            UUID request = UUID.fromString(split[0].substring(split[0].indexOf(";") + 1));
            String rawMessage = split[1];

            String response;
            try {
                response = clazz.newInstance().onMessage(rawMessage);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            RedisAPI.getInstance().publishMessage(
                    "proxy",
                    ChannelRegistry.getFromName(channelID),
                    request + "}=-=-={" + response);
        });
    }

    public void start() {
        RedisAPI.getInstance().startListeners();
    }

    public static void registerChannels(String[] channels) {
        Arrays.stream(channels).forEach(RedisMessage::register);
    }
}
