package net.swofty.velocity.gamemanager.balanceconfigurations;

import com.velocitypowered.api.proxy.Player;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.GameManager;

import java.util.List;

public class LowestPlayerCount extends BalanceConfiguration {

    @Override
    public GameManager.GameServer getServer(Player player, List<GameManager.GameServer> servers) {
        return servers.stream().min((server1, server2) -> {
            int server1Players = server1.server().getPlayersConnected().size();
            int server2Players = server2.server().getPlayersConnected().size();

            return Integer.compare(server1Players, server2Players);
        }).orElse(null);
    }
}
