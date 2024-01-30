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
            player.createConnectionRequest(limboServer).connectWithIndication();
            playersInLimbo.add(player);

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            playersInLimbo.remove(player);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            player.createConnectionRequest(server).fireAndForget();
        }).start();
    }
}
