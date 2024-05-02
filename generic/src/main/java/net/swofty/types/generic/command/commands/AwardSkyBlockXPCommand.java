package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "giveskyblockxp",
        description = "Gives yourself skyblock xp",
        usage = "/giveskyblockxp <cause>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AwardSkyBlockXPCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentString causeArgument = ArgumentType.String("cause");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            String causeString = context.get(causeArgument);
            SkyBlockLevelCauseAbstr cause = SkyBlockLevelCause.getCause(causeString);

            if (cause == null) {
                sender.sendMessage("§cInvalid cause.");
                SkyBlockLevelCause.getCauses().forEach((causeKey, causeAbstr) -> {
                    sender.sendMessage("§7- §a" + causeKey);
                });
                return;
            }

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getSkyBlockExperience().addExperience(cause);
        }, causeArgument);
    }
}
