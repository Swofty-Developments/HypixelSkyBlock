package net.swofty.velocity.redis;

import net.swofty.redisapi.api.ChannelRegistry;
import org.tinylog.Logger;
import net.swofty.redisapi.api.RedisAPI;
import org.tinylog.Logger;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.UUID;
import org.tinylog.Logger;

public abstract class RedisListener {

    public RedisListener() {
        ChannelListener annotation = this.getClass().getAnnotation(ChannelListener.class);
        if (annotation == null) {
            throw new RuntimeException("Class " + this.getClass().getName() + " does not have a @ChannelListener annotation!");
        }
    }

    public void onMessage(String channel, String message) {
        String[] split = message.split("}=-=-=\\{");
        String rawMessage = split[0];
        UUID uuid = UUID.fromString(split[1]);
        UUID filterID = UUID.fromString(split[2]);

        String messageWithoutFilter = rawMessage.substring(rawMessage.indexOf(";") + 1);
        JSONObject json = new JSONObject(messageWithoutFilter);

        Thread.startVirtualThread(() -> {
            JSONObject response;
            try {
                response = receivedMessage(json, filterID);
            } catch (Exception e) {
                System.out.println("Error on channel " + channel + " with message " + messageWithoutFilter);
                Logger.error(e, "Error in Redis listener");
                return;
            }

            RedisAPI.getInstance().publishMessage(
                    filterID.toString(),
                    ChannelRegistry.getFromName(channel),
                    uuid + "}=-=-={" + response.toString());
        });
    }

    public abstract JSONObject receivedMessage(JSONObject message, UUID serverUUID);
}
