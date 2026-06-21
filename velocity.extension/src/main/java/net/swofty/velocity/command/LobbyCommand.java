package net.swofty.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.swofty.commons.LobbyDestination;
import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.gamemanager.TransferHandler;

public class LobbyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) {
            source.sendMessage(Component.text("§cThis command can only be used by players."));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length > 1) {
            player.sendMessage(Component.text("§cUsage: /lobby [game]"));
            return;
        }

        ServerType currentType = player.getCurrentServer()
            .map(connection -> GameManager.getTypeFromRegisteredServer(connection.getServer()))
            .orElse(null);

        ServerType destination;
        if (args.length == 0) {
            destination = LobbyDestination.resolveDefaultDestination(currentType);
        } else {
            destination = LobbyDestination.resolveFromAlias(args[0]);
            if (destination == null) {
                player.sendMessage(Component.text("§cUnknown lobby destination. Try /lobby bw, /lobby sw, /lobby bedwars, /lobby skywars."));
                return;
            }
        }

        new TransferHandler(player).transferTo(destination);
    }
}
