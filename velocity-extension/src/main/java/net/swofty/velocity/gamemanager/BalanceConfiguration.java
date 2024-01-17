package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;

import java.util.List;
import java.util.Map;

public abstract class BalanceConfiguration {
    public abstract GameManager.GameServer getServer(Player player, List<GameManager.GameServer> servers);
}
