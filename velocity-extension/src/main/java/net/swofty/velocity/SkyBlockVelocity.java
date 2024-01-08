package net.swofty.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.swofty.commons.Configuration;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Plugin(
        id = "skyblock",
        name = "SkyBlock",
        version = "1.0",
        description = "SkyBlock plugin for Velocity",
        authors = {"Swofty"}
)
public class SkyBlockVelocity {
    private static ProxyServer server = null;
    private static Logger logger = null;
    @Getter
    private static SkyBlockVelocity plugin;

    @Inject
    public SkyBlockVelocity(ProxyServer tempServer, Logger tempLogger, @DataDirectory Path dataDirectory) {
        plugin = this;
        server = tempServer;
        logger = tempLogger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        /**
         * Setup Redis
         */
        RedisAPI.generateInstance(Configuration.get("redis-uri"));
        RedisAPI.getInstance().setFilterID("proxy");
        loopThroughPackage("net.swofty.velocity.redis.listeners", RedisListener.class)
                .forEach(listener ->  {
                    RedisAPI.getInstance().registerChannel(
                        listener.getClass().getAnnotation(ChannelListener.class).channel(),
                            (event2) -> {
                                listener.onMessage(event2.channel, event2.message);
                            });
                });

        GameManager.loopServers(server);
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}
