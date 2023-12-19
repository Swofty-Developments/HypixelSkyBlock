package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.gui.inventory.inventories.GUICreative;
import net.swofty.mission.MissionData;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "missiondata",
        description = "Collects mission information",
        usage = "/mission",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class MissionCommand extends SkyBlockCommand
{
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