package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;

import java.util.List;

public abstract class BalanceConfiguration {
    public abstract GameManager.GameServer getServer(Player player, List<GameManager.GameServer> servers);
}
