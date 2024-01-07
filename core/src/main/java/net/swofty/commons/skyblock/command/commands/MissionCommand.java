package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.mission.MissionData;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(aliases = "missiondata",
        description = "Collects mission information",
        usage = "/mission",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class MissionCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
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