package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.mission.MissionSet;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.Arrays;

@CommandParameters(aliases = "clearmissionset completemissionset",
        description = "Clears the mission set given",
        usage = "/finishmissionset <set>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class FinishMissionSetCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<MissionSet> set = ArgumentType.Enum("set", MissionSet.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            MissionSet missionSet = context.get(set);

            Arrays.stream(missionSet.getMissions()).forEach(mission -> {
                try {
                    ((SkyBlockPlayer) sender).getMissionData().endMission(mission);
                } catch (Exception e) {}
            });

            sender.sendMessage("§aSuccessfully finished mission set §e" + missionSet.name());
        }, set);
    }
}
