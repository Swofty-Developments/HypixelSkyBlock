package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMissionData;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.missions.MissionBreakLog;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Clears your missions",
        usage = "/missionempty",
        permission = Rank.ADMIN,
        aliases = "clearmission",
        allowsConsole = false)
public class ClearMissionCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("§aYour missions have been cleared.");
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getDataHandler().get(DataHandler.Data.MISSION_DATA, DatapointMissionData.class)
                    .setValue(new MissionData());
            player.getMissionData().startMission(MissionBreakLog.class);
        });
    }
}
