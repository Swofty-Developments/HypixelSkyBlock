package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(aliases = "is",
        description = "Sends the player to their island",
        usage = "/is",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class IslandCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            player.sendToIsland();
        });
    }
}
