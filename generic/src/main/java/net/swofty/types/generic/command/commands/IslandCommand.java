package net.swofty.types.generic.command.commands;

import net.swofty.commons.ServerType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "is",
        description = "Sends the player to their island",
        usage = "/is",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class IslandCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            player.sendTo(ServerType.ISLAND);
        });
    }
}
