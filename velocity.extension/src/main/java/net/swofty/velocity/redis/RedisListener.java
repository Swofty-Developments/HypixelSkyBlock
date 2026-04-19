package net.swofty.velocity.redis;

import net.swofty.commons.redis.RedisEnvelope;
import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.UUID;

public abstract class RedisListener {

    public RedisListener() {
        ChannelListener annotation = this.getClass().getAnnotation(ChannelListener.class);
        if (annotation == null) {
            throw new RuntimeException("Class " + this.getClass().getName() + " does not have a @ChannelListener annotation!");
        }
    }

    public void onMessage(String channel, String message) {
        String messageWithoutFilter = message.substring(message.indexOf(";") + 1);
        RedisEnvelope envelope = RedisEnvelope.deserialize(messageWithoutFilter);
        UUID uuid = UUID.fromString(envelope.id());
        UUID filterID = UUID.fromString(envelope.from());
        JSONObject json = new JSONObject(envelope.payload());

        Thread.startVirtualThread(() -> {
            JSONObject response;
            try {
                response = receivedMessage(json, filterID);
            } catch (Exception e) {
                System.out.println("Error on channel " + channel + " with message " + envelope.payload());
                Logger.error(e, "Error in Redis listener");
                return;
            }

            RedisAPI.getInstance().publishMessage(
                    filterID.toString(),
                    ChannelRegistry.getFromName(channel),
                    new RedisEnvelope(envelope.id(), "proxy", response.toString()).serialize());
        });
    }

    public abstract JSONObject receivedMessage(JSONObject message, UUID serverUUID);
}
