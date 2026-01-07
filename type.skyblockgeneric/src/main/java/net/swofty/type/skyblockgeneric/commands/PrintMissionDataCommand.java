package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "displaymissiondata",
        description = "Displays the mission data of a player",
        usage = "/displaymissiondata",
        permission = Rank.STAFF,
        allowsConsole = false)
public class PrintMissionDataCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            MissionData data = player.getMissionData();

            player.sendMessage("§aMission Data:");
            player.sendMessage("§7Active Missions:");
            data.getActiveMissions().forEach((mission) -> {
                player.sendMessage("§e" + mission.toString() + "§7:");
                mission.getCustomData().forEach((key, value) -> {
                    player.sendMessage("§7" + key + "§e: " + value);
                });
            });

            player.sendMessage("§7Completed Missions:");
            data.getCompletedMissions().forEach((mission) -> {
                player.sendMessage("§e" + mission.toString());
                mission.getCustomData().forEach((key, value) -> {
                    player.sendMessage("§7" + key + "§e: " + value);
                });
            });
        });
    }
}
