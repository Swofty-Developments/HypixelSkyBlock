package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.swofty.commons.Configuration;
import net.swofty.commons.ServerType;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.redis.RedisMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record TransferHandler(Player player) {
    public static ArrayList<Player> playersInLimbo = new ArrayList<>();

    public void transferTo(RegisteredServer currentServer, RegisteredServer toTransferTo) {
        new Thread(() -> {
            RegisteredServer limboServer = SkyBlockVelocity.getLimboServer();

            player.createConnectionRequest(limboServer).connectWithIndication();
            playersInLimbo.add(player);

            try {
                Thread.sleep(Long.parseLong(Configuration.get("transfer-timeout")));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            playersInLimbo.remove(player);

            try {
                Thread.sleep(Long.parseLong(Configuration.get("transfer-timeout")) / 5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            player.createConnectionRequest(toTransferTo).connectWithIndication();
            RedisMessage.sendMessageToServer(UUID.fromString(currentServer.getServerInfo().getName()),
                    "finished-transfer",
                    player.getUniqueId().toString());
        }).start();
    }
}
