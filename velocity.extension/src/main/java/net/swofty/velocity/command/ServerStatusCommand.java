package net.swofty.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.gamemanager.GameManager.GameServer;

import java.util.List;
import java.util.Map;

public class ServerStatusCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        invocation.source().sendMessage(Component.text("***** Server Status *****"));

        Map<ServerType, ? extends List<GameServer>> serverMap = GameManager.getServers();
        if (serverMap.isEmpty()) {
            invocation.source().sendMessage(Component.text("No servers are currently registered."));
            return;
        }

        for (Map.Entry<ServerType, ? extends List<GameServer>> entry : serverMap.entrySet()) {
            ServerType type = entry.getKey();
            List<GameServer> gameServers = entry.getValue();

            invocation.source().sendMessage(Component.text(type.name() + ":"));

            if (gameServers.isEmpty()) {
                invocation.source().sendMessage(Component.text(" - No servers of this type are currently online."));
                continue;
            }

            for (GameServer server : gameServers) {
                String msg = String.format(" - %s (ID: %s, Port: %d, PlayerCount: %d)",
                        server.displayName(),
                        server.internalID(),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().size()
                );
                invocation.source().sendMessage(Component.text(msg));
            }
        }
    }
}
