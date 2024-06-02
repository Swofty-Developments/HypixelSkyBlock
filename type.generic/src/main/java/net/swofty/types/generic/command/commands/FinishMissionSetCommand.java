package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.mission.MissionSet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.Arrays;

@CommandParameters(aliases = "clearmissionset completemissionset",
        description = "Clears the mission set given",
        usage = "/finishmissionset <set>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class FinishMissionSetCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
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
