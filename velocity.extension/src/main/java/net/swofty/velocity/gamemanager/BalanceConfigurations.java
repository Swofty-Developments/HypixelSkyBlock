package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.balanceconfigurations.IslandCheck;
import net.swofty.velocity.gamemanager.balanceconfigurations.LowestPlayerCount;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class BalanceConfigurations {
    public static Map<ServerType, List<BalanceConfiguration>> configurations = Map.of(
            ServerType.HUB, List.of(
                    new LowestPlayerCount()
            ),
            ServerType.THE_FARMING_ISLANDS, List.of(
                    new LowestPlayerCount()
            ),
            ServerType.ISLAND, List.of(
                    new IslandCheck(),
                    new LowestPlayerCount()
            )
    );

    public static @Nullable GameManager.GameServer getServerFor(Player player, ServerType type) {
        for (BalanceConfiguration configuration : configurations.get(type)) {
            GameManager.GameServer server = configuration.getServer(player, GameManager.getFromType(type));
            if (server != null) return server;
        }
        return null;
    }
}
