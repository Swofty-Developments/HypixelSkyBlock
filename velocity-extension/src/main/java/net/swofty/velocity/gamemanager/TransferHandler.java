package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.packet.PlayerChannelHandler;

import java.util.ArrayList;

public record TransferHandler(Player player) {
    public static ArrayList<Player> playersInLimbo = new ArrayList<>();
    public void transferTo(RegisteredServer server) {
        new Thread(() -> {
            RegisteredServer limboServer = SkyBlockVelocity.getLimboServer();
            playersInLimbo.add(player);
            player.createConnectionRequest(limboServer).connectWithIndication().join();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            playersInLimbo.remove(player);
            player.createConnectionRequest(server).connectWithIndication().join();
        }).start();
    }
}
