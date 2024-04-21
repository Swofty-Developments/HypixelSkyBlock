package net.swofty.types.generic.command.commands;

import net.swofty.commons.ServerType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.mission.MissionSet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "h",
        description = "Sends the player to their hub",
        usage = "/hub",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class HubCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            DataHandler dataHandler = player.getDataHandler();

            if (!MissionSet.GETTING_STARTED.hasCompleted(player) && !dataHandler.get(DataHandler.Data.RANK, DatapointRank.class).getValue().isEqualOrHigherThan(Rank.ADMIN)) {
                player.sendMessage("§cYou must complete your starting missions!");
                return;
            }
            player.sendTo(ServerType.VILLAGE);
        });
    }
}
