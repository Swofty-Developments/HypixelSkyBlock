package net.swofty.velocity.redis;

import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;
import org.json.JSONObject;

import java.util.UUID;

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
                e.printStackTrace();
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
