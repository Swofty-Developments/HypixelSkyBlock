package net.swofty.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import com.velocitypowered.proxy.network.Connections;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.raphimc.vialoader.ViaLoader;
import net.raphimc.vialoader.impl.platform.ViaBackwardsPlatformImpl;
import net.raphimc.vialoader.impl.platform.ViaRewindPlatformImpl;
import net.swofty.commons.Configuration;
import net.swofty.commons.ServerType;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.velocity.data.CoopDatabase;
import net.swofty.velocity.data.ProfilesDatabase;
import net.swofty.velocity.data.UserDatabase;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.BalanceConfigurations;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.packet.PlayerChannelHandler;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.redis.RedisMessage;
import net.swofty.velocity.via.injector.SkyBlockViaInjector;
import net.swofty.velocity.via.loader.SkyBlockVLLoader;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.List;
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
    @Getter
    private static ProxyServer server = null;
    @Getter
    private static SkyBlockVelocity plugin;
    @Getter
    private static RegisteredServer limboServer;
    @Getter
    private static boolean shouldAuthenticate = false;
    @Inject
    private ProxyServer proxy;

    @Inject
    public SkyBlockVelocity(ProxyServer tempServer, Logger tempLogger, @DataDirectory Path dataDirectory) {
        plugin = this;
        server = tempServer;

        limboServer = server.registerServer(new ServerInfo("limbo", new InetSocketAddress(Configuration.get("host-name"),
                Integer.parseInt(Configuration.get("limbo-port")))));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server = proxy;
        shouldAuthenticate = Configuration.getOrDefault("require-authentication", false);
        /**
         * Cross version support!
         */
        ViaLoader.init(null , new SkyBlockVLLoader() , new SkyBlockViaInjector(), null , ViaBackwardsPlatformImpl::new , ViaRewindPlatformImpl::new);

        /**
         * Register packets
         */
        server.getEventManager().register(this, PostLoginEvent.class,
                (AwaitingEventExecutor<PostLoginEvent>) postLoginEvent -> EventTask.withContinuation(continuation -> {
                    injectPlayer(postLoginEvent.getPlayer());
                    continuation.resume();
                }));
        server.getEventManager().register(this, PermissionsSetupEvent.class,
                (AwaitingEventExecutor<PermissionsSetupEvent>) permissionsEvent -> EventTask.withContinuation(continuation -> {
                    permissionsEvent.setProvider(permissionSubject -> PermissionFunction.ALWAYS_FALSE);
                    continuation.resume();
                }));
        server.getEventManager().register(this, DisconnectEvent.class, PostOrder.LAST,
                (AwaitingEventExecutor<DisconnectEvent>) disconnectEvent ->
                        disconnectEvent.getLoginStatus() == DisconnectEvent.LoginStatus.CONFLICTING_LOGIN
                                ? null
                                : EventTask.async(() -> removePlayer(disconnectEvent.getPlayer()))
        );

        /**
         * Handle database
         */
        new ProfilesDatabase("_placeHolder").connect(Configuration.get("mongodb"));
        UserDatabase.connect(Configuration.get("mongodb"));
        CoopDatabase.connect(Configuration.get("mongodb"));

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
        RedisMessage.registerProxyToServer("ping");
        RedisMessage.registerProxyToServer("run-event");
        RedisMessage.registerProxyToServer("refresh-data");
        RedisMessage.registerProxyToServer("has-island");
        RedisMessage.registerProxyToServer("bank-hash");
        RedisMessage.registerProxyToServer("origin-server");
        RedisMessage.registerProxyToServer("finished-transfer");
        RedisMessage.registerProxyToServer("teleport");
        RedisAPI.getInstance().startListeners();

        /**
         * Setup GameManager
         */
        GameManager.loopServers(server);
    }

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();

        if (!GameManager.hasType(ServerType.ISLAND)) {
            player.disconnect(
                    Component.text("§cThere are no SkyBlock (type=ISLAND) servers available at the moment.")
            );
            return;
        }

        List<GameManager.GameServer> gameServers = GameManager.getFromType(ServerType.ISLAND);
        List<BalanceConfiguration> configurations = BalanceConfigurations.configurations.get(ServerType.ISLAND);
        GameManager.GameServer toSendTo = gameServers.get(0);

        for (BalanceConfiguration configuration : configurations) {
            GameManager.GameServer server = configuration.getServer(player, gameServers);
            if (server != null) {
                toSendTo = server;
                break;
            }
        }

        // TODO: Force Resource Pack
        event.setInitialServer(toSendTo.server());

        if (shouldAuthenticate) {
            RedisMessage.sendMessageToServer(toSendTo.internalID(),
                    "authenticate",
                    player.getUniqueId().toString());
        }
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        event.setPing(new ServerPing(
                event.getPing().getVersion(),
                null,
                Component.text("                §aSkyBlock Recreation §c[1.8-1.20]"),
                event.getPing().getFavicon().orElse(null)
        ));
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

    private void injectPlayer(Player player) {
        final ConnectedPlayer connectedPlayer = (ConnectedPlayer) player;
        Channel channel = connectedPlayer.getConnection().getChannel();
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addBefore(Connections.HANDLER, "PACKET", new PlayerChannelHandler(player));
    }

    private void removePlayer(final Player player) {
        final ConnectedPlayer connectedPlayer = (ConnectedPlayer) player;
        final Channel channel = connectedPlayer.getConnection().getChannel();

        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("PACKET");
        });
    }
}
