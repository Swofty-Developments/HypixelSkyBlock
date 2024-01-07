package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointMissionData;
import net.swofty.commons.skyblock.mission.missions.MissionBreakLog;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;
import net.swofty.commons.skyblock.mission.MissionData;

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
