package net.swofty.loader;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minestom.server.Auth;
import net.minestom.server.MinecraftServer;
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
import net.swofty.commons.TestFlow;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.proxyapi.ProxyAPI;
import net.swofty.proxyapi.ProxyService;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.HypixelTypeLoader;
import net.swofty.type.generic.SkyBlockTypeLoader;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.net.InetAddress;


public class Hypixel {
    @Getter
    @Setter
    private static UUID serverUUID;

    private static final boolean ENABLE_SPARK = Configuration.getOrDefault("spark", false);

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

        Map<String, String> options = parseOptionalArgs(args);
        Integer maxPlayers = options.containsKey("--max-players") ?
                Integer.parseInt(options.get("--max-players")) : 20;

        // Test flow configuration
        String testFlowName = options.get("--test-flow");
        String testFlowHandler = options.get("--test-flow-handler");
        String testFlowPlayers = options.get("--test-flow-players");
        String testFlowIndex = options.get("--test-flow-index");
        String testFlowTotal = options.get("--test-flow-total");
        String testFlowTotalExpected = options.get("--test-flow-total-expected");
        String testFlowServerConfigs = options.get("--test-flow-server-configs");

        boolean isTestFlow = testFlowName != null;

        if (isTestFlow) {
            Logger.info("Starting server as part of test flow: " + testFlowName);
            Logger.info("Handler: " + testFlowHandler);
            Logger.info("Players: " + testFlowPlayers);
            Logger.info("Server index: " + testFlowIndex + " of " + testFlowTotal);
        }

        /**
         * Initialize the server
         */
        MinecraftServer minecraftServer = MinecraftServer.init(
                new Auth.Velocity(Configuration.get("velocity-secret"))
        );
        serverUUID = UUID.randomUUID();

        /**
         * Initialize GenericLoader
         */
        Reflections reflections = new Reflections("net.swofty.type");
        Set<Class<? extends HypixelTypeLoader>> subTypes = reflections.getSubTypesOf(HypixelTypeLoader.class);
        if (subTypes.isEmpty()) {
            Logger.error("No TypeLoader found!");
            System.exit(0);
            return;
        }
        HypixelTypeLoader typeLoader = subTypes.stream().filter(clazz -> {
            try {
                ServerType type = clazz.getDeclaredConstructor().newInstance().getType();
                Logger.info("Found TypeLoader: " + type.name());
                return type == serverType;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                return false;
            }
        }).findFirst().orElse(null).getDeclaredConstructor().newInstance();
        new HypixelGenericLoader(typeLoader).initialize(minecraftServer);

        /**
         * Initialize TypeLoader
         */
        if (typeLoader instanceof SkyBlockTypeLoader) {
            new SkyBlockGenericLoader(typeLoader).initialize(minecraftServer);
        }

        /**
         * Initialize the server
         */
        typeLoader.onInitialize(minecraftServer);

        /**
         * Initialize Proxy support
         */
        Logger.info("Initializing proxy support...");

        ProxyAPI proxyAPI = new ProxyAPI(Configuration.get("redis-uri"), serverUUID);
        SkyBlockGenericLoader.loopThroughPackage("net.swofty.type.generic.redis", ProxyToClient.class)
                .forEach(proxyAPI::registerFromProxyHandler);
        SkyBlockGenericLoader.loopThroughPackage("net.swofty.type.generic.redis.service", ServiceToClient.class)
                .forEach(proxyAPI::registerFromServiceHandler);
        typeLoader.getProxyRedisListeners().forEach(proxyAPI::registerFromProxyHandler);
        typeLoader.getServiceRedisListeners().forEach(proxyAPI::registerFromServiceHandler);
        if (typeLoader instanceof SkyBlockTypeLoader) {
            SkyBlockGenericLoader.loopThroughPackage("net.swofty.type.skyblockgeneric.redis", ProxyToClient.class)
                    .forEach(proxyAPI::registerFromProxyHandler);
            SkyBlockGenericLoader.loopThroughPackage("net.swofty.type.skyblockgeneric.redis.service", ServiceToClient.class)
                    .forEach(proxyAPI::registerFromServiceHandler);
        }
        Arrays.stream(ToProxyChannels.values()).forEach(
                ServerOutboundMessage::registerServerToProxy
        );
        List<ProtocolObject> protocolObjects = SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.commons.protocol.objects", ProtocolObject.class).toList();
        protocolObjects.forEach(ServerOutboundMessage::registerFromProtocolObject);
        proxyAPI.start();

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
            new ProxyService(serviceType).isOnline().thenAccept(online -> {
                if (!online) {
                    Logger.error("Service " + serviceType.name() + " is not online!");
                }
            });
        });
        typeLoader.afterInitialize(minecraftServer);

        /**
         * Start the server
         */
        MinecraftServer.setBrandName("Hypixel");
        CompletableFuture<Integer> startServer = new CompletableFuture<>();
        startServer.whenComplete((port, throwable) -> {
            minecraftServer.start(Configuration.get("host-name"), port);

            long endTime = System.currentTimeMillis();
            Logger.info("Started server on port " + port + " in " + (endTime - startTime) + "ms");
            Logger.info("Server Type: " + serverType.name());
            Logger.info("Internal ID: " + serverUUID.toString());
            HypixelConst.setPort(port);
            HypixelConst.setMaxPlayers(maxPlayers);
            HypixelConst.setServerUUID(serverUUID);

            ServerOutboundMessage.sendMessageToProxy(
                    ToProxyChannels.REQUEST_SERVERS_NAME, new JSONObject(),
                    (response) -> {
                        if (isTestFlow) {
                            String serverNameRaw = ((String) response.get("shortened-server-name"))
                                    .substring(1);
                            String serverName = "isolated" + serverNameRaw;
                            String shortenedServerName = "i" + serverNameRaw;

                            HypixelConst.setServerName(serverName);
                            HypixelConst.setShortenedServerName(shortenedServerName);

                            handleTestFlowRegistration(testFlowName, testFlowHandler, testFlowPlayers,
                                    serverType, testFlowIndex, testFlowTotal, testFlowServerConfigs);
                        } else {
                            HypixelConst.setServerName((String) response.get("server-name"));
                            HypixelConst.setShortenedServerName((String) response.get("shortened-server-name"));
                        }

                        Logger.info("Received server name: " + HypixelConst.getServerName());
                    });
            checkProxyConnected(MinecraftServer.getSchedulerManager());

            /**
             * Initialize the anticheat
             */
            if (Configuration.getOrDefault("anticheat", true)) {
                Thread.startVirtualThread(() -> {
                    Logger.info("Initializing anticheat...");

                    MinestomLoader minestomLoader = new MinestomLoader();
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

        JSONObject registerMessage = new JSONObject()
                .put("type", serverType.name())
                .put("max_players", maxPlayers)
                .put("host", InetAddress.getLocalHost().getHostName());

        // Add test flow information if this is a test flow server
        if (isTestFlow) {
            registerMessage.put("is_test_flow", true)
                    .put("test_flow_name", testFlowName)
                    .put("test_flow_index", testFlowIndex)
                    .put("test_flow_total", testFlowTotal);
        }

        ServerOutboundMessage.sendMessageToProxy(
                ToProxyChannels.REGISTER_SERVER,
                registerMessage,
                (response) -> startServer.complete(Integer.parseInt(response.get("port").toString())));
    }

    private static void handleTestFlowRegistration(String testFlowName, String handler, String players,
                                                   ServerType serverType, String index, String total, String serverConfigs) {
        // Parse players list and set up local test flow state
        List<String> playerList = Arrays.stream(players.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Set the current test flow for this server
        TestFlow.setCurrentTestFlow(testFlowName, playerList);

        // Only the first server (global index 0) should register the test flow with the proxy
        if ("0".equals(index)) {
            Logger.info("Registering test flow with proxy: " + testFlowName);

            // Parse server configs and create JSON array
            JSONArray serverConfigsArray = new JSONArray();
            String[] configs = serverConfigs.split(",");

            for (String config : configs) {
                String[] parts = config.trim().split(":");
                String type = parts[0];
                int count = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;

                serverConfigsArray.put(new JSONObject()
                        .put("type", type)
                        .put("count", count));
            }

            JSONObject testFlowMessage = new JSONObject()
                    .put("test_flow_name", testFlowName)
                    .put("handler", handler)
                    .put("players", new JSONArray(playerList))
                    .put("server_configs", serverConfigsArray);

            ServerOutboundMessage.sendMessageToProxy(
                    ToProxyChannels.REGISTER_TEST_FLOW,
                    testFlowMessage,
                    (response) -> {
                        Logger.info("Test flow registered successfully with proxy");
                        // Mark this server as ready
                        notifyTestFlowServerReady(testFlowName, serverType, index);
                    });
        } else {
            // For other servers, just notify they're ready
            notifyTestFlowServerReady(testFlowName, serverType, index);
        }
    }

    private static void notifyTestFlowServerReady(String testFlowName, ServerType serverType, String index) {
        JSONObject readyMessage = new JSONObject()
                .put("test_flow_name", testFlowName)
                .put("server_type", serverType.name())
                .put("server_index", index);

        ServerOutboundMessage.sendMessageToProxy(
                ToProxyChannels.TEST_FLOW_SERVER_READY,
                readyMessage,
                (response) -> {
                    Logger.info("Notified proxy that " + serverType.name() + " server " + index + " is ready for test flow: " + testFlowName);
                });
    }

    private static Map<String, String> parseOptionalArgs(String[] args) {
        Map<String, String> options = new HashMap<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("--")) {
                options.put(args[i], args[i + 1]);
            }
        }
        return options;
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
        }, ExecutionType.TICK_END);
    }
}