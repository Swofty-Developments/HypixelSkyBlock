package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.swofty.commons.ServerType;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.redis.RedisMessage;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameManager {
    private static Map<ServerType, ArrayList<GameServer>> servers = new HashMap<>();

    public static GameServer addServer(ServerType type, UUID serverID) {
        int port = getNextAvailablePort();
        RegisteredServer registeredServer = SkyBlockVelocity.getServer().registerServer(
                new ServerInfo(serverID.toString(), new InetSocketAddress(port))
        );

        GameServer server = new GameServer(type.toString(), serverID, registeredServer);
        if (!servers.containsKey(type)) servers.put(type, new ArrayList<>(List.of(server)));
        else servers.get(type).add(server);

        return server;
    }

    public static boolean hasType(ServerType type) {
        return servers.containsKey(type) && !servers.get(type).isEmpty();
    }

    public static List<GameServer> getFromType(ServerType type){
        return servers.get(type);
    }

    public static void loopServers(ProxyServer server) {
        server.getScheduler().buildTask(SkyBlockVelocity.getPlugin(), () -> {
            servers.forEach((serverType, registeredServers) -> {
                registeredServers.forEach(registeredServer -> {
                    RegisteredServer givenServer = registeredServer.server();
                    AtomicBoolean pingSuccess = new AtomicBoolean(false);

                    RedisMessage.sendMessageToServer(registeredServer.internalID(), "ping", "check", (response) -> {
                        pingSuccess.set(true);
                    });

                    server.getScheduler().buildTask(SkyBlockVelocity.getPlugin(), () -> {
                        if (!pingSuccess.get()) {
                            System.out.println("Server " + givenServer.getServerInfo().getName() + " is offline! Removing from list...");
                            servers.get(serverType).remove(registeredServer);
                        }
                    }).delay(Duration.ofMillis(15)).schedule();
                });
            });
        }).repeat(Duration.ofMillis(300)).schedule();
    }

    private static int getNextAvailablePort() {
        if (servers.isEmpty()) return 20000;
        if (servers.values().stream().allMatch(ArrayList::isEmpty)) return 20000;

        int highestPort = servers.values().stream().mapToInt(servers -> servers.stream().mapToInt(server -> server.server().getServerInfo().getAddress().getPort()).max().getAsInt()).max().getAsInt();
        return highestPort + 1;
    }

    public record GameServer(String displayName, UUID internalID, RegisteredServer server) { }
}
