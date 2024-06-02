package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "missiondata",
        description = "Collects mission information",
        usage = "/mission",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class MissionCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            MissionData data = ((SkyBlockPlayer) sender).getMissionData();

            sender.sendMessage("Active Missions: " + data.getActiveMissions().size());
            sender.sendMessage("Completed Missions: " + data.getCompletedMissions().size());

            data.getCompletedMissions().forEach(mission -> {
                sender.sendMessage("Completed Mission: " + mission.getMissionID());
            });
            data.getActiveMissions().forEach(mission -> {
                sender.sendMessage("Active Mission: " + mission.getMissionID());
            });
        });
    }
}