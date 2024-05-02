package net.swofty.types.generic.redis;

import net.minestom.server.event.Event;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class RedisRunEvent implements ProxyToClient {
    @Override
    public String onMessage(String message) {
        String playerUuid = message.split(",")[0];
        String eventClassName = message.split(",")[1];

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(UUID.fromString(playerUuid));

        String[] eventArgs = message.split(",");
        String[] eventArgsWithoutPlayerName = new String[eventArgs.length - 2];
        System.arraycopy(eventArgs, 2, eventArgsWithoutPlayerName, 0, eventArgs.length - 2);

        String finalArgs = String.join(",", eventArgsWithoutPlayerName);

        // Access static method
        // public static CollectionUpdateEvent fromProxyUnderstandable(SkyBlockPlayer player, String string) {
        // with the arguments player and eventArgsWithoutPlayerName

        Class<?> eventClass = null;
        try {
            eventClass = Class.forName(eventClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Event event = null;
        try {
            event = (Event) eventClass.getMethod("fromProxyUnderstandable", SkyBlockPlayer.class, String.class)
                    .invoke(null, player, finalArgs);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        SkyBlockEventHandler.callSkyBlockEvent(event);

        return "ok";
    }
}
