package net.swofty.loader;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.velocity.VelocityProxy;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.anticheat.flag.FlagType;
import net.swofty.anticheat.loader.PunishmentHandler;
import net.swofty.anticheat.loader.SwoftyAnticheat;
import net.swofty.anticheat.loader.SwoftyValues;
import net.swofty.anticheat.loader.minestom.MinestomLoader;
import net.swofty.commons.Configuration;
import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.proxyapi.ProxyAPI;
import net.swofty.proxyapi.ProxyService;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.commons.protocol.ProtocolSpecification;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.commons.protocol.protocols.ProtocolPingSpecification;
import net.swofty.types.generic.redis.*;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class SkyBlock {
    @Getter
    @Setter
    private static UUID serverUUID;
    @Getter
    @Setter
    private static SkyBlockTypeLoader typeLoader;

    private static final boolean ENABLE_SPARK = Configuration.getOrDefault("spark" , false);

    @SneakyThrows
    public static void main(String[] args) {
        if (args.length == 0 || !ServerType.isServerType(args[0])) {
            Logger.error("Please specify a server type.");
            Arrays.stream(ServerType.values()).forEach(serverType -> Logger.error(serverType.name()));
            System.exit(0);
            return;
        }

        ServerType serverType = ServerType.valueOf(args[0].toUpperCase());
        long startTime = System.currentTimeMillis();

        boolean isPterodactyl = Configuration.getOrDefault("pterodactyl-mode" , false);

        if (isPterodactyl && args.length < 2) {
            Logger.error("Please specify server port.");
            System.exit(0);
        }

        int pterodactylPort = isPterodactyl ? Integer.parseInt(args[1]) : -1;

        /**
         * Initialize TypeLoader
         */
        Reflections reflections = new Reflections("net.swofty.type");
        Set<Class<? extends SkyBlockTypeLoader>> subTypes = reflections.getSubTypesOf(SkyBlockTypeLoader.class);
        if (subTypes.isEmpty()) {
            Logger.error("No TypeLoader found!");
            System.exit(0);
            return;
        }
        typeLoader = subTypes.stream().filter(clazz -> {
            try {
                ServerType type = clazz.getDeclaredConstructor().newInstance().getType();
                Logger.info("Found TypeLoader: " + type.name());
                return type == serverType;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                return false;
            }
        }).findFirst().orElse(null).getDeclaredConstructor().newInstance();

        /**
         * Initialize the server
         */
        MinecraftServer minecraftServer = MinecraftServer.init();
        serverUUID = UUID.randomUUID();

        new SkyBlockGenericLoader(typeLoader).initialize(minecraftServer);
        typeLoader.onInitialize(minecraftServer);

        /**
         * Initialize Proxy support
         */
        Logger.info("Initializing proxy support...");

        ProxyAPI proxyAPI = new ProxyAPI(Configuration.get("redis-uri"), serverUUID);
        SkyBlockGenericLoader.loopThroughPackage("net.swofty.types.generic.redis", ProxyToClient.class)
                .forEach(proxyAPI::registerFromProxyHandler);
        Arrays.stream(ToProxyChannels.values()).forEach(
                ServerOutboundMessage::registerServerToProxy
        );
        proxyAPI.start();

        VelocityProxy.enable(Configuration.get("velocity-secret"));

        /**
         * Start spark if enabled
         */
        if (ENABLE_SPARK) {
            // Spark.enable(Files.createTempDirectory("spark"));
        }

        /**
         * Ensure all services are running
         */
        typeLoader.getRequiredServices().forEach(serviceType -> {
            new ProxyService(serviceType).isOnline(new ProtocolPingSpecification()).thenAccept(online -> {
                if (!online) {
                    Logger.error("Service " + serviceType.name() + " is not online!");
                }
            });
        });
        typeLoader.afterInitialize(minecraftServer);

        /**
         * Start the server
         */
        MinecraftServer.setBrandName("SkyBlock");
        CompletableFuture<Integer> startServer = new CompletableFuture<>();
        startServer.whenComplete((port, throwable) -> {
            minecraftServer.start(Configuration.get("host-name"), port);

            long endTime = System.currentTimeMillis();
            Logger.info("Started server on port " + port + " in " + (endTime - startTime) + "ms");
            Logger.info("Server Type: " + serverType.name());
            Logger.info("Internal ID: " + serverUUID.toString());

            ServerOutboundMessage.sendMessageToProxy(
                    ToProxyChannels.REQUEST_SERVERS_NAME, new JSONObject(),
                    (response) -> {
                        SkyBlockConst.setServerName((String) response.get("server-name"));
                    });
            checkProxyConnected(MinecraftServer.getSchedulerManager());

            /**
             * Initialize the anticheat
             */
            if (Configuration.getOrDefault("anticheat", true)) {
                Thread.startVirtualThread(() -> {
                    Logger.info("Initializing anticheat...");

                    MinestomLoader minestomLoader = new MinestomLoader();
                    minestomLoader.registerListeners(MinecraftServer.getGlobalEventHandler());

                    SwoftyAnticheat.loader(minestomLoader);
                    SwoftyAnticheat.values(new SwoftyValues());
                    SwoftyAnticheat.punishmentHandler(new PunishmentHandler() {
                        @Override
                        public void onFlag(UUID uuid, FlagType flagType) {
                            Logger.info("Player " + uuid + " flagged for " + flagType.name());
                        }
                    });
                    SwoftyAnticheat.start();

                    Logger.info("Anticheat initialized");
                });
            }
        });
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (startServer.isDone()) return;
            Logger.error("Couldn't connect to proxy. Shutting down...");
            System.exit(0);
        }).start();

        ServerOutboundMessage.sendMessageToProxy(
                ToProxyChannels.REGISTER_SERVER,
                new JSONObject().put("type", serverType.name())
                        .put("port" , pterodactylPort),
                (response) -> startServer.complete(Integer.parseInt(response.get("port").toString())));
    }

    public static List<String> initOutgoingRedisChannels() {
        List<String> requiredChannels = new ArrayList<>(Arrays.asList(
                "proxy-online",
                "server-initialized",
                "server-name",
                "player-handler",
                "player-count"
        ));
        Reflections protocolSpecifications = new Reflections("net.swofty.commons.protocol.protocols");
        Set<Class<? extends ProtocolSpecification>> subTypesOfProtocol = protocolSpecifications.getSubTypesOf(ProtocolSpecification.class);
        subTypesOfProtocol.forEach(protocol -> {
            try {
                ProtocolSpecification specification = protocol.getDeclaredConstructor().newInstance();
                requiredChannels.add(specification.getEndpoint());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        });

        return requiredChannels;
    }

    private static void checkProxyConnected(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            AtomicBoolean responded = new AtomicBoolean(false);

            try {
                ServerOutboundMessage.sendMessageToProxy(
                        ToProxyChannels.PROXY_IS_ONLINE, new JSONObject(), (response) -> {
                    if (response.get("online").equals(true)) {
                        responded.set(true);
                    }
                });
            } catch (Exception e) {
                MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> player.kick("§cServer has lost connection to the proxy, please rejoin"));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }

            scheduler.scheduleTask(() -> {
                if (!responded.get()) {
                    Logger.error("Proxy did not respond to alive check. Shutting down...");
                    System.exit(0);
                }
            }, TaskSchedule.tick(4), TaskSchedule.stop());

            return TaskSchedule.seconds(1);
        } , ExecutionType.TICK_END);
    }
}
