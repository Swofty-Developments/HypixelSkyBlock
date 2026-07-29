package net.swofty.type.skyblockgeneric.redis;

import net.minestom.server.event.Event;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.from.RunEventProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class RedisRunEvent implements RedisMessageHandler<RunEventProtocol.Request, RunEventProtocol.Response> {
    @Override
    public RedisProtocol<RunEventProtocol.Request, RunEventProtocol.Response> protocol() {
        return new RunEventProtocol();
    }

    @Override
    public RunEventProtocol.Response handle(RunEventProtocol.Request message, RedisMessageContext context) {
        UUID uuid = UUID.fromString(message.uuid());
        String eventClassName = message.event();
        String eventArgs = message.data();

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(uuid);
        if (player == null) return new RunEventProtocol.Response();

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

        return new RunEventProtocol.Response();
    }
}
