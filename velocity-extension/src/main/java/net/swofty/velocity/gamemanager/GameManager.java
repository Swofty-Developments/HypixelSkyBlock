package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.swofty.commons.ServerType;
import net.swofty.velocity.SkyBlockVelocity;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
    private static Map<ServerType, List<GameServer>> servers = new HashMap<>();

    public static void loopServers(ProxyServer server) {
        server.getScheduler().buildTask(SkyBlockVelocity.getPlugin(), () -> {
            servers.forEach((serverType, registeredServers) -> {
                registeredServers.forEach(registeredServer -> {
                    RegisteredServer server1 = registeredServer.server();

                    server1.ping().whenComplete((serverPing, throwable) -> {
                        if (throwable != null) {
                            System.out.println("Error pinging server " + server1.getServerInfo().getName() + "!");
                        }
                    });
                });
            });
        }).repeat(Duration.ofMillis(300)).schedule();
    }

    public record GameServer(String displayName, RegisteredServer server) { }
}
