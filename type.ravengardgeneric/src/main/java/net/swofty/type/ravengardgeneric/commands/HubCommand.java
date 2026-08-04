package net.swofty.type.ravengardgeneric.commands;

import net.swofty.commons.ServerType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

@CommandParameters(labels = "h hub", description = "Sends the player to the Ravengard hub",
        usage = "/hub", permission = Rank.DEFAULT, allowsConsole = false)
public class HubCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            ((RavengardPlayer) sender).sendTo(ServerType.RAVENGARD_LOBBY);
        });
    }
}
