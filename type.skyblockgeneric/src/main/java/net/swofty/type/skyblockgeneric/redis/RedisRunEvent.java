package net.swofty.type.skyblockgeneric.redis;

import net.minestom.server.event.Event;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.json.JSONObject;
import org.tinylog.Logger;

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

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(uuid);
        if (player == null) return new JSONObject();

        // Access static method
        // public static CollectionUpdateEvent fromProxyUnderstandable(SkyBlockPlayer player, String string) {
        // with the arguments player and eventArgsWithoutPlayerName

        Class<?> eventClass = null;
        try {
            eventClass = Class.forName(eventClassName);
        } catch (ClassNotFoundException e) {
            Logger.error(e, "Failed to find event class: {}", eventClassName);
        }

        Event event = null;
        try {
            event = (Event) eventClass.getMethod("fromProxyUnderstandable", SkyBlockPlayer.class, String.class)
                    .invoke(null, player, eventArgs);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.error(e, "Failed to invoke fromProxyUnderstandable method on event class: {}", eventClassName);
        }

        HypixelEventHandler.callCustomEvent(event);

        return new JSONObject();
    }
}
