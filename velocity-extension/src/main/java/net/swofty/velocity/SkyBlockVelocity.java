package net.swofty.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.*;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import com.velocitypowered.proxy.network.Connections;
import com.velocitypowered.proxy.protocol.VelocityConnectionEvent;
import com.viaversion.viaversion.VelocityPlugin;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import com.viaversion.viaversion.protocols.base.BaseVersionProvider;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import net.raphimc.vialoader.ViaLoader;
import net.raphimc.vialoader.netty.*;
import net.swofty.commons.Configuration;
import net.swofty.commons.ServerType;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.velocity.data.CoopDatabase;
import net.swofty.velocity.data.ProfilesDatabase;
import net.swofty.velocity.data.UserDatabase;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.BalanceConfigurations;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.packet.PacketSetup;
import net.swofty.velocity.packet.PlayerChannelHandler;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.redis.RedisMessage;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
    @Inject
    private ProxyServer proxy;

    @Inject
    public SkyBlockVelocity(ProxyServer tempServer, Logger tempLogger, @DataDirectory Path dataDirectory) {
        plugin = this;
        server = tempServer;

        limboServer = server.registerServer(new ServerInfo("limbo", new InetSocketAddress(
                Integer.parseInt(Configuration.get("limbo-port")))));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server = proxy;

        /**
         * Register cross-version support
         */
        ViaLoader.init(null, new SkyBlockVLoader(), null, null);
        Via.getManager().debugHandler().setEnabled(true);

        /**
         * Register packets
         */
        server.getEventManager().register(this, PostLoginEvent.class,
                (AwaitingEventExecutor<PostLoginEvent>) postLoginEvent -> EventTask.withContinuation(continuation -> {
                    injectPlayer(postLoginEvent.getPlayer());
                    continuation.resume();
                }));
        server.getEventManager().register(this, DisconnectEvent.class, PostOrder.LAST,
                (AwaitingEventExecutor<DisconnectEvent>) disconnectEvent ->
                        disconnectEvent.getLoginStatus() == DisconnectEvent.LoginStatus.CONFLICTING_LOGIN
                                ? null
                                : EventTask.async(() -> removePlayer(disconnectEvent.getPlayer()))
        );
        PacketSetup.inject();

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
        RedisAPI.getInstance().startListeners();

        /**
         * Setup GameManager
         */
        GameManager.loopServers(server);
    }

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent event) {
        if (!GameManager.hasType(ServerType.ISLAND)) {
            event.getPlayer().disconnect(
                    Component.text("§cThere are no SkyBlock (type=ISLAND) servers available at the moment.")
            );
            return;
        }

        List<GameManager.GameServer> gameServers = GameManager.getFromType(ServerType.ISLAND);
        List<BalanceConfiguration> configurations = BalanceConfigurations.configurations.get(ServerType.ISLAND);
        GameManager.GameServer toSendTo = gameServers.get(0);

        for (BalanceConfiguration configuration : configurations) {
            GameManager.GameServer server = configuration.getServer(event.getPlayer(), gameServers);
            if (server != null) {
                toSendTo = server;
                break;
            }
        }

        // TODO: Force Resource Pack

        event.setInitialServer(toSendTo.server());

        event.setInitialServer(limboServer);
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

        // pipeline.addBefore(Connections.MINECRAFT_DECODER, "PACKET", new PlayerChannelHandler(player));
    }

    private void removePlayer(final Player player) {
        final ConnectedPlayer connectedPlayer = (ConnectedPlayer) player;
        final Channel channel = connectedPlayer.getConnection().getChannel();
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("PACKET");
            channel.pipeline().remove(VLLegacyPipeline.VIA_DECODER_NAME);
            channel.pipeline().remove(VLLegacyPipeline.VIA_ENCODER_NAME);
        });
    }
}
