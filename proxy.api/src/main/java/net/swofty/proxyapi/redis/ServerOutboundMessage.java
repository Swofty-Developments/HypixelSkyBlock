package net.swofty.proxyapi.redis;

import net.swofty.commons.ServiceType;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ServerOutboundMessage {
    private static final Map<UUID, Consumer<String>> redisMessageListeners = new ConcurrentHashMap<>();
    public static final Map<String, ProtocolObject> protocolObjects = new ConcurrentHashMap<>();

    public static void registerServerToProxy(ToProxyChannels channel) {
        RedisAPI.getInstance().registerChannel(channel.getChannelName(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);

            String[] split = messageWithoutFilter.split("}=-=-=\\{");
            UUID uuid = UUID.fromString(split[0]);

            Consumer<String> listener = redisMessageListeners.remove(uuid);
            if (listener == null) {
                Logger.error("Received proxy response for unknown request " + uuid + " on channel " + channel.getChannelName());
                return;
            }

            listener.accept(split[1]);
        });
    }

    public static void sendMessageToProxy(ToProxyChannels channel, JSONObject message, Consumer<JSONObject> response) {
        UUID uuid = UUID.randomUUID();
        UUID filterID = UUID.fromString(RedisAPI.getInstance().getFilterId());

        Consumer<String> consumer = (s) -> {
            response.accept(new JSONObject(s));
        };
        redisMessageListeners.put(uuid, consumer);

        RedisAPI.getInstance().publishMessage("proxy",
                ChannelRegistry.getFromName(channel.getChannelName()),
                message.toString() + "}=-=-={" + uuid + "}=-=-={" + filterID);
    }

    public static void registerFromProtocolObject(ProtocolObject object) {
        String requestTypeName = getRequestTypeName(object);
        protocolObjects.put(requestTypeName, object);

        RedisAPI.getInstance().registerChannel(object.channel(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);
            String[] split = messageWithoutFilter.split("}=-=---=\\{");

            UUID uuid = UUID.fromString(split[0]);
            String message;
            if (split.length != 1) {
                message = split[1];
            } else message = "";

            try {
                Consumer<String> listener = redisMessageListeners.remove(uuid);
                if (listener == null) {
                    Logger.error("Received service response for unknown request " + uuid + " on channel " + object.channel());
                    return;
                }

                listener.accept(message);
            } catch (Exception e) {
                Logger.error("Failed to handle message from " + uuid + ": " + e.getMessage());
            }
        });
    }

    public static void sendMessageToService(ServiceType service,
                                            ProtocolObject specification,
                                            Object rawMessage,
                                            Consumer<String> response) {
        UUID requestId = UUID.randomUUID();
        String callbackId = RedisAPI.getInstance().getFilterId();
        if (callbackId == null) return;

        redisMessageListeners.put(requestId, response);

        String message = specification.translateToString(rawMessage);

        RedisAPI.getInstance().publishMessage(service.name(),
                ChannelRegistry.getFromName(specification.channel()),
                new ServiceProxyRequest(requestId, callbackId,
                        specification.channel(), message).toJSON().toString());
    }

    /**
     * Fire-and-forget: send to a service and do not wait for or register a response.
     */
    public static void sendMessageToServiceFireAndForget(ServiceType service,
                                                         ProtocolObject specification,
                                                         Object rawMessage) {
        UUID requestId = UUID.randomUUID();
        String callback = null;
        try {
            callback = RedisAPI.getInstance().getFilterId();
        } catch (Exception ignored) {
        }

        String message = specification.translateToString(rawMessage);
        RedisAPI.getInstance().publishMessage(
                service.name(),
                ChannelRegistry.getFromName(specification.channel()),
                new ServiceProxyRequest(
                        requestId,
                        callback != null ? callback : "proxy",
                        specification.channel(),
                    message,
                    false
                ).toJSON().toString()
        );
    }

    /**
     * Fire-and-forget broadcast to all service types.
     */
    public static void sendMessageToAllServicesFireAndForget(ProtocolObject specification,
                                                             Object rawMessage) {
        for (ServiceType serviceType : ServiceType.values()) {
            sendMessageToServiceFireAndForget(serviceType, specification, rawMessage);
        }
    }

    private static String getRequestTypeName(ProtocolObject<?, ?> protocolObject) {
        Class<?> clazz = protocolObject.getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType paramType) {
            Type[] typeArguments = paramType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                Type firstTypeArg = typeArguments[0];
                if (firstTypeArg instanceof Class) {
                    return ((Class<?>) firstTypeArg).getSimpleName();
                } else {
                    // Handle cases where T might be another generic type
                    return firstTypeArg.getTypeName();
                }
            }
        }

        throw new IllegalArgumentException("Could not determine the type T for the given ProtocolObject");
    }
}
