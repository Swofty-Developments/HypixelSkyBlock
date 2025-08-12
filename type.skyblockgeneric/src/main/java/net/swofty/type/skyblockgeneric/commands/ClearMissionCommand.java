package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.datapoints.DatapointMissionData;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.missions.MissionBreakLog;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Clears your missions",
        usage = "/missionempty",
        permission = Rank.ADMIN,
        aliases = "clearmission",
        allowsConsole = false)
public class ClearMissionCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("§aYour missions have been cleared.");
            HypixelPlayer player = (HypixelPlayer) sender;
            player.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.MISSION_DATA, DatapointMissionData.class)
                    .setValue(new MissionData());
            player.getMissionData().startMission(MissionBreakLog.class);
        });
    }
}
