package net.swofty.velocity.redis;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.redis.RedisChannels;
import net.swofty.commons.redis.RedisEnvelope;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.tinylog.Logger;

import java.util.UUID;

public abstract class RedisListener<T, R> {
    private final RedisProtocol<T, R> protocol;

    public RedisListener() {
        this.protocol = protocol();
    }

    public abstract RedisProtocol<T, R> protocol();
    public abstract R receivedMessage(T message, UUID serverUUID);

    public String getChannelName() {
        return RedisChannels.protocol(protocol);
    }

    public void onMessage(String channel, String message) {
        String messageWithoutFilter = message.substring(message.indexOf(";") + 1);
        RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
        UUID uuid = UUID.fromString(envelope.id());
        UUID filterID = UUID.fromString(envelope.from());

        T typedMessage = protocol.translateFromString(envelope.payload());

        Thread.startVirtualThread(() -> {
            R response;
            try {
                response = receivedMessage(typedMessage, filterID);
            } catch (Exception e) {
                Logger.error(e, "Error on channel {} with message {}", channel, envelope.payload());
                return;
            }

            String serializedResponse = protocol.translateReturnToString(response);

            RedisAPI.getInstance().publishMessage(
                    filterID.toString(),
                    ChannelRegistry.getFromName(channel),
                    new RedisEnvelope(envelope.id(), "proxy", serializedResponse).serialize());
        });
    }
}
