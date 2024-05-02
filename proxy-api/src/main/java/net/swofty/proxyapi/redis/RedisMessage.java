package net.swofty.proxyapi.redis;

import net.swofty.commons.ServiceType;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.service.protocol.ProtocolSpecification;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class RedisMessage {
    private static final Map<UUID, Consumer<String>> redisMessageListeners = new HashMap<>();

    public static void register(String channel) {
        RedisAPI.getInstance().registerChannel(channel, (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);

            String[] split = messageWithoutFilter.split("}=-=-=\\{");
            UUID uuid = UUID.fromString(split[0]);
            String message = split[1];

            redisMessageListeners.get(uuid).accept(message);
            redisMessageListeners.remove(uuid);
        });
    }

    public static void sendMessageToProxy(String channel, String message, Consumer<String> response) {
        UUID uuid = UUID.randomUUID();
        UUID filterID = UUID.fromString(RedisAPI.getInstance().getFilterId());
        redisMessageListeners.put(uuid, response);

        RedisAPI.getInstance().publishMessage("proxy",
                ChannelRegistry.getFromName(channel),
                message + "}=-=-={" + uuid + "}=-=-={" + filterID);
    }

    public static void sendMessageService(ServiceType service,
                                          ProtocolSpecification specification,
                                          JSONObject message,
                                          Consumer<String> response) {
        // Check that all required keys in the protocolspecification are filled
        specification.getRequiredOutboundFields().forEach(key -> {
            if (!message.has(key)) {
                System.out.println("Message: " + message);
                throw new RuntimeException("Message does not contain required key " + key);
            }
        });

        UUID uuid = UUID.randomUUID();
        UUID toCallback = UUID.fromString(RedisAPI.getInstance().getFilterId());
        redisMessageListeners.put(uuid, response);

        RedisAPI.getInstance().publishMessage(service.name(),
                ChannelRegistry.getFromName(specification.getEndpoint()),
                new ServiceProxyRequest(uuid, toCallback.toString(),
                        specification.getRequiredInboundFields(),
                        specification.getEndpoint(), message.toString()).toJSON().toString());
    }
}
