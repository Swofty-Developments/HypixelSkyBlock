package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.balanceconfigurations.IslandCheck;
import net.swofty.velocity.gamemanager.balanceconfigurations.LowestPlayerCount;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceConfigurations {
    public static HashMap<ServerType, List<BalanceConfiguration>> configurations = new HashMap<>(Map.of(
            ServerType.HUB, List.of(
                    new LowestPlayerCount()
            ),
            ServerType.THE_FARMING_ISLANDS, List.of(
                    new LowestPlayerCount()
            ),
            ServerType.ISLAND, List.of(
                    new IslandCheck(),
                    new LowestPlayerCount()
            ))
    );

    public static @Nullable GameManager.GameServer getServerFor(Player player, ServerType type) {
        for (BalanceConfiguration configuration : configurations.get(type)) {
            List<GameManager.GameServer> serversToConsider = GameManager.getFromType(type);
            serversToConsider.removeIf(server -> {
                return server.maxPlayers() <= server.registeredServer().getPlayersConnected().size();
            });

            GameManager.GameServer server = configuration.getServer(player, serversToConsider);
            if (server != null) return server;
        }
        return null;
    }
}
