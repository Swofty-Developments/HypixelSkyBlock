package net.swofty.velocity.testflow;

import lombok.Getter;
import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages test flows on the proxy side
 */
public class TestFlowManager {
    @Getter
    private static final ConcurrentMap<String, ProxyTestFlowInstance> activeTestFlows = new ConcurrentHashMap<>();

    /**
     * Registers a new test flow
     */
    public static void registerTestFlow(String name, String handler, List<String> players, List<ServerConfig> serverConfigs) {
        if (activeTestFlows.containsKey(name)) {
            System.out.println("[WARN] Test flow " + name + " is already registered, ignoring duplicate registration");
            return;
        }

        ProxyTestFlowInstance instance = new ProxyTestFlowInstance(name, handler, players, serverConfigs);
        activeTestFlows.put(name, instance);

        System.out.println("[INFO] Registered test flow: " + name);
        System.out.println("[INFO] Handler: " + handler);
        System.out.println("[INFO] Players: " + players);
        System.out.println("[INFO] Expected servers: " + instance.getTotalExpectedServers());

        // Try to load and instantiate the handler
        loadTestFlowHandler(instance);
    }

    /**
     * Marks a server as ready for a test flow
     */
    public static void markServerReady(String testFlowName, ServerType serverType, int serverIndex, UUID serverUUID) {
        ProxyTestFlowInstance instance = activeTestFlows.get(testFlowName);
        if (instance == null) {
            System.out.println("[WARN] Received server ready notification for unknown test flow: " + testFlowName);
            return;
        }

        instance.markServerReady(serverType, serverIndex, serverUUID);
        System.out.println("[INFO] Server " + serverType.name() + " #" + serverIndex + " is ready for test flow: " + testFlowName);

        // Only trigger "all servers ready" once when we actually reach the total count
        if (instance.areAllServersReady() && !instance.isAllServersReadyTriggered()) {
            instance.setAllServersReadyTriggered(true);
            System.out.println("[INFO] All servers are ready for test flow: " + testFlowName);
            if (instance.getHandler() != null) {
                instance.getHandler().onAllServersReady(instance);
            }
        }
    }

    /**
     * Checks if a server is part of any test flow and handles cleanup if needed
     * Call this method from GameManager when a server goes offline
     */
    public static void handleServerDisconnect(UUID serverUUID) {
        activeTestFlows.values().forEach(instance -> {
            if (instance.getServerUUIDs().contains(serverUUID)) {
                System.out.println("[INFO] Test flow server disconnected: " + serverUUID + " from test flow: " + instance.getName());

                // Remove the server from the ready servers list
                instance.removeServer(serverUUID);

                // Check if any servers are still online for this test flow
                if (instance.hasAnyServersOnline()) {
                    System.out.println("[INFO] Test flow " + instance.getName() + " still has " +
                            instance.getOnlineServerCount() + " servers online");
                } else {
                    System.out.println("[WARN] All servers for test flow " + instance.getName() + " are offline, cleaning up...");
                    cleanupTestFlow(instance.getName());
                }
            }
        });
    }

    /**
     * Cleans up a test flow and notifies handlers
     * This is called automatically when all servers in a test flow go offline
     */
    private static void cleanupTestFlow(String testFlowName) {
        ProxyTestFlowInstance instance = activeTestFlows.get(testFlowName);
        if (instance != null) {
            System.out.println("[INFO] Cleaning up test flow: " + testFlowName);

            // Notify handler that test flow is ending
            if (instance.getHandler() != null) {
                try {
                    instance.getHandler().onTestFlowEnd(instance);
                } catch (Exception e) {
                    System.out.println("[ERROR] Error in test flow handler onTestFlowEnd: " + e.getMessage());
                }
            }

            // Remove from active test flows
            activeTestFlows.remove(testFlowName);
            System.out.println("[INFO] Test flow " + testFlowName + " has been cleaned up and removed");
        }
    }

    public static boolean isServerInTestFlow(UUID serverName) {
        return activeTestFlows.values().stream()
                .anyMatch(instance -> instance.hasServer(serverName));
    }

    /**
     * Checks if a player is part of any active test flow
     */
    public static boolean isPlayerInTestFlow(String playerName) {
        return activeTestFlows.values().stream()
                .anyMatch(instance -> instance.hasPlayer(playerName));
    }

    /**
     * Gets the test flow instance for a specific player
     */
    public static ProxyTestFlowInstance getTestFlowForPlayer(String playerName) {
        return activeTestFlows.values().stream()
                .filter(instance -> instance.hasPlayer(playerName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a test flow instance by name
     */
    public static ProxyTestFlowInstance getTestFlow(String name) {
        return activeTestFlows.get(name);
    }

    public static ProxyTestFlowInstance getFromServerUUID(UUID serverUUID) {
        return activeTestFlows.values().stream()
                .filter(instance -> instance.hasServer(serverUUID))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes a test flow
     */
    public static void unregisterTestFlow(String name) {
        ProxyTestFlowInstance instance = activeTestFlows.remove(name);
        if (instance != null && instance.getHandler() != null) {
            instance.getHandler().onTestFlowEnd(instance);
            System.out.println("[INFO] Unregistered test flow: " + name);
        }
    }

    /**
     * Handles player join events
     */
    public static void handlePlayerJoin(String playerName) {
        activeTestFlows.values().forEach(instance -> {
            if (instance.hasPlayer(playerName) && instance.getHandler() != null) {
                instance.getHandler().onPlayerJoin(playerName, instance);
            }
        });
    }

    /**
     * Handles player leave events
     */
    public static void handlePlayerLeave(String playerName) {
        activeTestFlows.values().forEach(instance -> {
            if (instance.hasPlayer(playerName) && instance.getHandler() != null) {
                instance.getHandler().onPlayerLeave(playerName, instance);
            }
        });
    }

    /**
     * Loads and instantiates a test flow handler
     */
    @SuppressWarnings("unchecked")
    private static void loadTestFlowHandler(ProxyTestFlowInstance instance) {
        try {
            String handlerName = instance.getHandlerClassName();
            if (handlerName.endsWith(".java")) {
                handlerName = handlerName.substring(0, handlerName.length() - 5);
            }

            // Try to load from handlers package
            String fullClassName = "net.swofty.velocity.testflow.handlers." + handlerName;

            Class<?> handlerClass = Class.forName(fullClassName);
            if (ProxyTestFlowHandler.class.isAssignableFrom(handlerClass)) {
                Constructor<?> constructor = handlerClass.getDeclaredConstructor();
                ProxyTestFlowHandler handler = (ProxyTestFlowHandler) constructor.newInstance();
                instance.setHandler(handler);
                handler.onTestFlowStart(instance);
                System.out.println("[INFO] Successfully loaded test flow handler: " + handlerName);
            } else {
                System.out.println("[ERROR] Handler " + handlerName + " does not extend ProxyTestFlowHandler");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to load test flow handler: " + instance.getHandlerClassName() + " - " + e.getMessage());
        }
    }

    /**
     * Server configuration for test flows
     */
    @Getter
    public static class ServerConfig {
        private final ServerType serverType;
        private final int count;

        public ServerConfig(ServerType serverType, int count) {
            this.serverType = serverType;
            this.count = count;
        }

        public static ServerConfig fromJson(org.json.JSONObject json) {
            ServerType type = ServerType.valueOf(json.getString("type"));
            int count = json.getInt("count");
            return new ServerConfig(type, count);
        }
    }

    /**
     * Proxy-side test flow instance
     */
    @Getter
    public static class ProxyTestFlowInstance {
        private final String name;
        private final String handlerClassName;
        private final List<String> players;
        private final List<ServerConfig> serverConfigs;
        private final long startTime;
        private final Map<String, UUID> readyServers = new ConcurrentHashMap<>();

        private ProxyTestFlowHandler handler;
        private boolean allServersReadyTriggered = false;

        public ProxyTestFlowInstance(String name, String handlerClassName, List<String> players, List<ServerConfig> serverConfigs) {
            this.name = name;
            this.handlerClassName = handlerClassName;
            this.players = new ArrayList<>(players);
            this.serverConfigs = new ArrayList<>(serverConfigs);
            this.startTime = System.currentTimeMillis();
        }

        public void setHandler(ProxyTestFlowHandler handler) {
            this.handler = handler;
        }

        public boolean isAllServersReadyTriggered() {
            return allServersReadyTriggered;
        }

        public void setAllServersReadyTriggered(boolean triggered) {
            this.allServersReadyTriggered = triggered;
        }

        public void removeServer(UUID serverUUID) {
            readyServers.entrySet().removeIf(entry -> entry.getValue().equals(serverUUID));
        }

        public boolean hasAnyServersOnline() {
            return readyServers.values().stream()
                    .anyMatch(uuid -> GameManager.getFromUUID(uuid) != null);
        }

        public int getOnlineServerCount() {
            return (int) readyServers.values().stream()
                    .mapToLong(uuid -> GameManager.getFromUUID(uuid) != null ? 1 : 0)
                    .sum();
        }

        public boolean hasPlayer(String playerName) {
            return players.contains(playerName);
        }

        public int getTotalExpectedServers() {
            return serverConfigs.stream()
                    .mapToInt(ServerConfig::getCount)
                    .sum();
        }

        public void markServerReady(ServerType serverType, int serverIndex, UUID serverUUID) {
            String key = serverType.name() + ":" + serverIndex;
            readyServers.put(key, serverUUID);
        }

        public boolean areAllServersReady() {
            return readyServers.size() >= getTotalExpectedServers();
        }

        public long getUptime() {
            return System.currentTimeMillis() - startTime;
        }

        public Collection<UUID> getServerUUIDs() {
            return readyServers.values();
        }

        public List<GameManager.GameServer> getGameServers() {
            return readyServers.values().stream()
                    .map(GameManager::getFromUUID)
                    .filter(Objects::nonNull)
                    .toList();
        }

        public boolean hasServer(UUID serverUUID) {
            return readyServers.containsValue(serverUUID) && GameManager.getFromUUID(serverUUID) != null;
        }
    }
}