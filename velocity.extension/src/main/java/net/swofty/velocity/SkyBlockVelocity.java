package net.swofty.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.AwaitingEventExecutor;
import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
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
import com.viaversion.vialoader.ViaLoader;
import com.viaversion.vialoader.impl.platform.ViaBackwardsPlatformImpl;
import com.viaversion.vialoader.impl.platform.ViaRewindPlatformImpl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.swofty.commons.Configuration;
import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.redisapi.api.RedisAPI;
import net.swofty.velocity.command.ServerStatusCommand;
import net.swofty.velocity.data.CoopDatabase;
import net.swofty.velocity.data.ProfilesDatabase;
import net.swofty.velocity.data.UserDatabase;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.BalanceConfigurations;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.gamemanager.TransferHandler;
import net.swofty.velocity.packet.PlayerChannelHandler;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.redis.RedisMessage;
import net.swofty.velocity.testflow.TestFlowManager;
import net.swofty.velocity.viaversion.injector.SkyBlockViaInjector;
import net.swofty.velocity.viaversion.loader.SkyBlockVLLoader;
import org.json.JSONObject;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
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
    @Getter
    private static boolean shouldAuthenticate = false;
    @Getter
    private static boolean supportCrossVersion = false;
    @Inject
    private ProxyServer proxy;

    @Inject
    public SkyBlockVelocity(ProxyServer tempServer, Logger tempLogger, @DataDirectory Path dataDirectory) {
        plugin = this;
        server = tempServer;

        limboServer = server.registerServer(new ServerInfo("limbo", new InetSocketAddress(Configuration.get("limbo-host-name"),
                Integer.parseInt(Configuration.get("limbo-port")))));
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server = proxy;
        shouldAuthenticate = Configuration.getOrDefault("require-authentication", false);
        supportCrossVersion = Configuration.getOrDefault("cross-version-support", false);

        /**
         * initialize cross version support
         */
        if (supportCrossVersion) {
            ViaLoader.init(null, new SkyBlockVLLoader(), new SkyBlockViaInjector(), null, ViaBackwardsPlatformImpl::new, ViaRewindPlatformImpl::new);
        }
        /**
         * Register packets
         */
        server.getEventManager().register(this, PostLoginEvent.class,
                (AwaitingEventExecutor<PostLoginEvent>) postLoginEvent -> EventTask.withContinuation(continuation -> {
                    injectPlayer(postLoginEvent.getPlayer());
                    TestFlowManager.handlePlayerJoin(postLoginEvent.getPlayer().getUsername());
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
                                : EventTask.async(() -> {
                                    // Handle test flow player leave
                                    TestFlowManager.handlePlayerLeave(disconnectEvent.getPlayer().getUsername());
                                    removePlayer(disconnectEvent.getPlayer());
                                })
        );

        /**
         * Register commands
         */

        CommandManager commandManager = proxy.getCommandManager();
        CommandMeta statusCommandMeta = commandManager.metaBuilder("serverstatus")
                .aliases("status")
                .plugin(this)
                .build();

        commandManager.register(statusCommandMeta, new ServerStatusCommand());


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
                        listener.getClass().getAnnotation(ChannelListener.class).channel().getChannelName(),
                            (event2) -> {
                                listener.onMessage(event2.channel, event2.message);
                            });
                });
        for (FromProxyChannels channel : FromProxyChannels.values()) {
            RedisMessage.registerProxyToServer(channel);
        }
        RedisAPI.getInstance().startListeners();

        /**
         * Setup GameManager
         */
        GameManager.loopServers(server);
    }

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();

        if (!GameManager.hasType(ServerType.PROTOTYPE_LOBBY) || !GameManager.isAnyEmpty(ServerType.PROTOTYPE_LOBBY)) {
            player.disconnect(
                    Component.text("§cThere are no Prototype Lobby servers available at the moment.")
            );
            return;
        }

        List<GameManager.GameServer> gameServers = GameManager.getFromType(ServerType.PROTOTYPE_LOBBY);
        if (TestFlowManager.isPlayerInTestFlow(player.getUsername())) {
            TestFlowManager.ProxyTestFlowInstance instance = TestFlowManager.getTestFlowForPlayer(player.getUsername());
            player.sendPlainMessage("§7You are currently in test flow " + instance.getName() + ".");
            player.sendPlainMessage("§7Servers involved include " + instance.getGameServers().stream().map(GameManager.GameServer::displayName).collect(Collectors.joining(", ")));
            player.sendPlainMessage("§7We are expecting " + instance.getTotalExpectedServers() + " servers to instantiate.");
            player.sendPlainMessage("§7Test flow has been running for " + instance.getUptime() / 1000 + " seconds.");

            gameServers.removeIf(server -> {
                TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                        server.internalID()
                );

                return testFlowInstance == null || !instance.hasServer(server.internalID());
            });
        } else {
            gameServers.removeIf(server -> {
                TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                        server.internalID()
                );

                return testFlowInstance != null;
            });
        }

        if (gameServers.isEmpty()) {
            player.disconnect(
                    Component.text("§cThere are no servers (type=PROTOTYPE_LOBBY) servers available at the moment.")
            );
            return;
        }

        List<BalanceConfiguration> configurations = BalanceConfigurations.configurations.get(ServerType.PROTOTYPE_LOBBY);
        GameManager.GameServer toSendTo = gameServers.getFirst();

        for (BalanceConfiguration configuration : configurations) {
            GameManager.GameServer server = configuration.getServer(player, gameServers);
            if (server != null) {
                toSendTo = server;
                break;
            }
        }

        // TODO: Force Resource Pack
        event.setInitialServer(toSendTo.registeredServer());

        if (shouldAuthenticate) {
            RedisMessage.sendMessageToServer(toSendTo.internalID(),
                    FromProxyChannels.PROMPT_PLAYER_FOR_AUTHENTICATION,
                    new JSONObject().put("uuid", player.getUniqueId().toString()));
        }
    }

    @Subscribe
    public void onServerCrash(KickedFromServerEvent event) {
        // Send the player to the limbo
        RegisteredServer originalServer = event.getServer();
        Component reason = event.getServerKickReason().orElse(Component.text(
                "§cYour connection to the server was lost. Please try again later."
        ));
        ServerType serverType = GameManager.getTypeFromRegisteredServer(originalServer);

        event.setResult(KickedFromServerEvent.RedirectPlayer.create(
                limboServer,
                null
        ));

        TransferHandler transferHandler = new TransferHandler(event.getPlayer());
        transferHandler.standardTransferTo(originalServer, serverType);

        Thread.startVirtualThread(() -> {
            // Determine if the registeredServer disconnect was due to a crash
            // if it was, then we send the player back to another registeredServer
            // of that type, otherwise we disconnect them for the same
            // reason as the original
            try {
                Thread.sleep(GameManager.SLEEP_TIME + 300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            /*boolean isOnline = GameManager.getFromRegisteredServer(originalServer) != null;
            if (isOnline) {
                transferHandler.forceRemoveFromLimbo();
                event.getPlayer().disconnect(reason);
                return;
            }*/

            try {
                ServerType serverTypeToTry = serverType;
                if (!GameManager.hasType(serverTypeToTry) || !GameManager.isAnyEmpty(serverTypeToTry)) {
                    serverTypeToTry = ServerType.PROTOTYPE_LOBBY;
                }

                GameManager.GameServer server = BalanceConfigurations.getServerFor(event.getPlayer(), serverTypeToTry);
                if (server == null) {
                    transferHandler.forceRemoveFromLimbo();
                    event.getPlayer().disconnect(reason);
                    return;
                }
                transferHandler.noLimboTransferTo(server.registeredServer());

                if (!serverTypeToTry.isSkyBlock()) {
                    event.getPlayer().sendPlainMessage("§cAn exception occurred in your connection, so you were put into the Prototype Lobby.");
                } else {
                    event.getPlayer().sendPlainMessage("§cAn exception occurred in your connection, so you were put into another SkyBlock server.");
                }
                event.getPlayer().sendPlainMessage("§7Sending to server " + server.displayName() + "...");
            } catch (Exception e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "An exception occurred while trying to transfer " + event.getPlayer().getUsername() + " to " + serverType, e);
                transferHandler.forceRemoveFromLimbo();
                event.getPlayer().disconnect(reason);
            }
        });
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        event.setPing(new ServerPing(
                event.getPing().getVersion(),
                null,
                Component.text("                §aHypixel Recreation §c[1.8-1.20]"),
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
