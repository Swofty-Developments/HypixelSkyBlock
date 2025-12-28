package net.swofty.velocity.gamemanager.balanceconfigurations;

import com.velocitypowered.api.proxy.Player;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.GameManager;

import java.util.List;

public class ReadyGames extends BalanceConfiguration {

    @Override
    public GameManager.GameServer getServer(Player player, List<GameManager.GameServer> servers) {
        return servers.stream().max((server1, server2) -> { // TODO: currently checks for which has the most players
            int server1Players = server1.registeredServer().getPlayersConnected().size();
            int server2Players = server2.registeredServer().getPlayersConnected().size();

            return Integer.compare(server1Players, server2Players);
        }).orElse(null);
    }
}
