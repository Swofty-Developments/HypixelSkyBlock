package net.swofty.type.skyblockgeneric.redis;

import net.minestom.server.event.Event;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.type.generic.SkyBlockGenericLoader;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class RedisRunEvent implements ProxyToClient {
    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.RUN_EVENT_ON_SERVER;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        String eventClassName = message.getString("event");
        String eventArgs = message.getString("data");

        HypixelPlayer player = SkyBlockGenericLoader.getFromUUID(uuid);
        if (player == null) return new JSONObject();

        // Access static method
        // public static CollectionUpdateEvent fromProxyUnderstandable(HypixelPlayer player, String string) {
        // with the arguments player and eventArgsWithoutPlayerName

        Class<?> eventClass = null;
        try {
            eventClass = Class.forName(eventClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Event event = null;
        try {
            event = (Event) eventClass.getMethod("fromProxyUnderstandable", HypixelPlayer.class, String.class)
                    .invoke(null, player, eventArgs);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        HypixelEventHandler.callCustomEvent(event);

        return new JSONObject();
    }
}
