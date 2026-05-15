package net.swofty.proxyapi.redis;

import net.swofty.commons.ServiceType;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.redis.RedisEnvelope;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.tinylog.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ServerOutboundMessage {
    private static final Map<UUID, Consumer<String>> redisMessageListeners = new HashMap<>();
    public static final Map<String, ProtocolObject> protocolObjects = new HashMap<>();

    public static <T, R> void sendToProxy(ProtocolObject<T, R> protocol, T request, Consumer<R> response) {
        UUID uuid = UUID.randomUUID();
        UUID filterID = UUID.fromString(RedisAPI.getInstance().getFilterId());

        Consumer<String> consumer = (s) -> {
            R typed = protocol.translateReturnFromString(s);
            response.accept(typed);
        };
        redisMessageListeners.put(uuid, consumer);

        String serialized = protocol.translateToString(request);
        RedisAPI.getInstance().publishMessage("proxy",
                ChannelRegistry.getFromName(protocol.channel()),
                new RedisEnvelope(uuid.toString(), filterID.toString(), serialized).serialize());
    }

    public static void registerToProxyProtocol(ProtocolObject<?, ?> protocol) {
        RedisAPI.getInstance().registerChannel(protocol.channel(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);

            RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
            UUID uuid = UUID.fromString(envelope.id());

            redisMessageListeners.get(uuid).accept(envelope.payload());
            redisMessageListeners.remove(uuid);
        });
    }

    public static void registerFromProtocolObject(ProtocolObject object) {
        String requestTypeName = getRequestTypeName(object);
        protocolObjects.put(requestTypeName, object);

        RedisAPI.getInstance().registerChannel(object.channel(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);

            RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
            UUID uuid = UUID.fromString(envelope.id());
            String message = envelope.payload();

            try {
                redisMessageListeners.get(uuid).accept(message);
                redisMessageListeners.remove(uuid);
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
                        message
                ).toJSON().toString()
        );
    }

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
                    return firstTypeArg.getTypeName();
                }
            }
        }

        throw new IllegalArgumentException("Could not determine the type T for the given ProtocolObject");
    }
}
