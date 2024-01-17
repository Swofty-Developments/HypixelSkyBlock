package net.swofty.velocity.gamemanager;

import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.balanceconfigurations.HasCoopIsland;
import net.swofty.velocity.gamemanager.balanceconfigurations.LowestPlayerCount;

import java.util.List;
import java.util.Map;

public class BalanceConfigurations {
    public static Map<ServerType, List<BalanceConfiguration>> configurations = Map.of(
            ServerType.VILLAGE, List.of(
                    new LowestPlayerCount()
            ),
            ServerType.ISLAND, List.of(
                    new HasCoopIsland(),
                    new LowestPlayerCount()
            )
    );
}
