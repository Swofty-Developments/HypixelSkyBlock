package net.swofty.velocity.gamemanager.impl;

import com.velocitypowered.api.proxy.Player;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.GameManager;

import java.util.Comparator;
import java.util.List;

public final class LowestPlayerCount extends BalanceConfiguration {

    private static final Comparator<GameManager.GameServer> BY_PLAYER_COUNT =
            Comparator.comparingInt(server -> server.registeredServer().getPlayersConnected().size());

    @Override
    public GameManager.GameServer getServer(Player player, List<GameManager.GameServer> servers) {
        return servers.stream().min(BY_PLAYER_COUNT).orElse(null);
    }
}
