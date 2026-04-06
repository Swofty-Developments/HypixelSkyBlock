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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ServerOutboundMessage {
    private static final long REDIS_READY_TIMEOUT_MS = 5000;
    private static final long RESPONSE_TIMEOUT_MS = 10000;
    private static final Map<UUID, Consumer<String>> redisMessageListeners = new HashMap<>();
    public static final Map<String, ProtocolObject> protocolObjects = new HashMap<>();

    public static void registerServerToProxy(ToProxyChannels channel) {
        RedisAPI.getInstance().registerChannel(channel.getChannelName(), (event) -> {
            String messageWithoutFilter = event.message.substring(event.message.indexOf(";") + 1);

            String[] split = messageWithoutFilter.split("}=-=-=\\{");
            UUID uuid = UUID.fromString(split[0]);

            Consumer<String> listener = redisMessageListeners.remove(uuid);
            if (listener == null) {
                return;
            }

            listener.accept(split[1]);
        });
    }

    public static void sendMessageToProxy(ToProxyChannels channel, JSONObject message, Consumer<JSONObject> response) {
        RedisAPI redis = awaitRedis(true);
        if (redis == null) {
            Logger.warn("Redis API was not ready for proxy message {}", channel.getChannelName());
            return;
        }

        UUID uuid = UUID.randomUUID();
        UUID filterID = UUID.fromString(redis.getFilterId());

        Consumer<String> consumer = (s) -> {
            response.accept(new JSONObject(s));
        };
        redisMessageListeners.put(uuid, consumer);

        redis.publishMessage("proxy",
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
        RedisAPI redis = awaitRedis(true);
        if (redis == null) {
            Logger.warn("Redis API was not ready for service message {}", specification.channel());
            return;
        }

        UUID requestId = UUID.randomUUID();
        String callbackId = redis.getFilterId();
        if (callbackId == null) return;

        redisMessageListeners.put(requestId, response);
        CompletableFuture.delayedExecutor(RESPONSE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .execute(() -> redisMessageListeners.remove(requestId));

        String message = specification.translateToString(rawMessage);

        redis.publishMessage(service.name(),
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
        RedisAPI redis = awaitRedis(false);
        if (redis == null) {
            Logger.warn("Redis API was not ready for fire-and-forget service message {}", specification.channel());
            return;
        }

        UUID requestId = UUID.randomUUID();
        String callback = null;
        try {
            callback = redis.getFilterId();
        } catch (Exception ignored) {
        }

        String message = specification.translateToString(rawMessage);
        redis.publishMessage(
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

    private static RedisAPI awaitRedis(boolean requireFilterId) {
        long deadline = System.currentTimeMillis() + REDIS_READY_TIMEOUT_MS;

        while (System.currentTimeMillis() < deadline) {
            try {
                RedisAPI redis = RedisAPI.getInstance();
                if (redis != null && (!requireFilterId || redis.getFilterId() != null)) {
                    return redis;
                }
            } catch (Exception ignored) {
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        return null;
    }
}
