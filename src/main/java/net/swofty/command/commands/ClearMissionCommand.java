package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointMissionData;
import net.swofty.mission.MissionData;
import net.swofty.mission.missions.MissionBreakLog;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(description = "Clears your missions",
        usage = "/missionempty",
        permission = Rank.ADMIN,
        aliases = "clearmission",
        allowsConsole = false)
public class ClearMissionCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            sender.sendMessage("§aYour missions have been cleared.");
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getDataHandler().get(DataHandler.Data.MISSION_DATA, DatapointMissionData.class)
                    .setValue(new MissionData());
            player.getMissionData().startMission(MissionBreakLog.class);
        });
    }
}
