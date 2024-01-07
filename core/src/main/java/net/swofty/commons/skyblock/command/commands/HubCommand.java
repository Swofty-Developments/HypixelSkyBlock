package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;
import net.swofty.commons.skyblock.mission.MissionSet;

@CommandParameters(aliases = "h",
        description = "Sends the player to their hub",
        usage = "/hub",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class HubCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = ((SkyBlockPlayer) sender);

            if (!MissionSet.GETTING_STARTED.hasCompleted(player)) {
                player.sendMessage("§cYou must complete your starting missions!");
                return;
            }

            player.sendToHub();
        });
    }
}
