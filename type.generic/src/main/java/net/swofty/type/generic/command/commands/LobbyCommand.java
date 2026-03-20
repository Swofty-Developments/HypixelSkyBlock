package net.swofty.type.generic.command.commands;

import net.swofty.commons.ServerType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "l",
        description = "Takes the player to the lobby",
        usage = "/lobby",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class LobbyCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            player.sendTo(ServerType.PROTOTYPE_LOBBY);
        });
    }

}
