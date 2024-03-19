package net.swofty.velocity.redis;

import net.swofty.redisapi.api.ChannelRegistry;
import net.swofty.redisapi.api.RedisAPI;

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

        Thread.startVirtualThread(() -> {
            String response;
            try {
                response = receivedMessage(messageWithoutFilter, filterID);
            } catch (Exception e) {
                System.out.println("Error on channel " + channel + " with message " + messageWithoutFilter);
                e.printStackTrace();
                response = "error";
            }

            RedisAPI.getInstance().publishMessage(
                    filterID.toString(),
                    ChannelRegistry.getFromName(channel),
                    uuid + "}=-=-={" + response);
        });
    }

    public abstract String receivedMessage(String message, UUID serverUUID);
}
