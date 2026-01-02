package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "missiondata",
        description = "Collects mission information",
        usage = "/mission",
        permission = Rank.STAFF,
        allowsConsole = false)
public class MissionCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
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