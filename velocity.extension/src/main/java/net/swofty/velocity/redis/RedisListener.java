package net.swofty.velocity.redis;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.redis.RedisEnvelope;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.tinylog.Logger;

import java.util.UUID;

public abstract class RedisListener<T, R> {
    private final ProtocolObject<T, R> protocol;

    public RedisListener() {
        this.protocol = getProtocol();
    }

    public abstract ProtocolObject<T, R> getProtocol();
    public abstract R receivedMessage(T message, UUID serverUUID);

    public String getChannelName() {
        return protocol.channel();
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
                System.out.println("Error on channel " + channel + " with message " + envelope.payload());
                Logger.error(e, "Error in Redis listener");
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
