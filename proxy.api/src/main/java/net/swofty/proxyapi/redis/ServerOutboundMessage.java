package net.swofty.proxyapi.redis;

import net.swofty.commons.ServiceType;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.commons.protocol.ProtocolSpecification;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerOutboundMessage {
    private static final Map<UUID, Consumer<JSONObject>> redisMessageListeners = new HashMap<>();

    public static void registerServerToProxy(ToProxyChannels channel) {
        RedisAPI.getInstance().registerChannel(channel.getChannelName(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);

            String[] split = messageWithoutFilter.split("}=-=-=\\{");
            UUID uuid = UUID.fromString(split[0]);

            JSONObject json = new JSONObject(split[1]);

            redisMessageListeners.get(uuid).accept(json);
            redisMessageListeners.remove(uuid);
        });
    }

    public static void sendMessageToProxy(ToProxyChannels channel, JSONObject message, Consumer<JSONObject> response) {
        UUID uuid = UUID.randomUUID();
        UUID filterID = UUID.fromString(RedisAPI.getInstance().getFilterId());
        redisMessageListeners.put(uuid, response);

        RedisAPI.getInstance().publishMessage("proxy",
                ChannelRegistry.getFromName(channel.getChannelName()),
                message.toString() + "}=-=-={" + uuid + "}=-=-={" + filterID);
    }

    public static void registerFromProtocolSpecification(ProtocolSpecification specification) {
        RedisAPI.getInstance().registerChannel(specification.getEndpoint(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);

            String[] split = messageWithoutFilter.split("}=-=-=\\{");

            UUID uuid = UUID.fromString(split[0]);
            JSONObject message = new JSONObject(split[1]);

            specification.getRequiredInboundFields().forEach(key -> {
                if (!message.has(key)) {
                    throw new RuntimeException("Message does not contain required key " + key);
                }
            });

            redisMessageListeners.get(uuid).accept(message);
        });
    }

    public static void sendMessageToService(ServiceType service,
                                            ProtocolSpecification specification,
                                            JSONObject message,
                                            Consumer<JSONObject> response) {
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
