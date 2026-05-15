package net.swofty.proxyapi.redis;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisEndpoint;
import net.swofty.commons.redis.RedisMessageBus;
import net.swofty.redisapi.api.RedisAPI;
import org.tinylog.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ServerOutboundMessage {
    public static final Map<String, RedisProtocol> protocols = new HashMap<>();

    public static <T, R> void sendToProxy(RedisProtocol<T, R> protocol, T request, Consumer<R> response) {
        RedisMessageBus.request(
                localEndpoint(),
                RedisChannels.PROXY_RESPONSE,
                RedisChannels.protocol(protocol),
                RedisChannels.protocol(protocol),
                protocol,
                request
        ).thenAccept(response);
    }

    public static void registerToProxyProtocol(RedisProtocol<?, ?> protocol) {
        RedisMessageBus.registerResponseChannel(RedisChannels.protocol(protocol));
    }

    public static void registerResponseProtocol(RedisProtocol protocol) {
        String requestTypeName = getRequestTypeName(protocol);
        protocols.put(requestTypeName, protocol);

        RedisMessageBus.registerResponseChannel(RedisChannels.protocol(protocol));
    }

    public static void sendMessageToService(ServiceType service,
                                            RedisProtocol specification,
                                            Object rawMessage,
                                            Consumer<String> response) {
        RedisMessageBus.requestRaw(
                localEndpoint(),
                service.name(),
                specification.channel(),
                specification.channel(),
                specification,
                rawMessage
        ).thenAccept(response).exceptionally(throwable -> {
            Logger.error(throwable, "Failed to receive response from service {}", service.name());
            return null;
        });
    }

    public static void sendMessageToServiceFireAndForget(ServiceType service,
                                                         RedisProtocol specification,
                                                         Object rawMessage) {
        RedisMessageBus.publish(
                localEndpoint(),
                service.name(),
                specification.channel(),
                specification,
                rawMessage
        );
    }

    public static void sendMessageToAllServicesFireAndForget(RedisProtocol specification,
                                                             Object rawMessage) {
        for (ServiceType serviceType : ServiceType.values()) {
            sendMessageToServiceFireAndForget(serviceType, specification, rawMessage);
        }
    }

    private static String getRequestTypeName(RedisProtocol<?, ?> protocolObject) {
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

        throw new IllegalArgumentException("Could not determine the type T for the given RedisProtocol");
    }

    private static RedisEndpoint localEndpoint() {
        String filterId = RedisAPI.getInstance().getFilterId();
        if (RedisEndpoint.proxy().id().equals(filterId)) {
            return RedisEndpoint.proxy();
        }
        return RedisEndpoint.server(filterId);
    }
}
