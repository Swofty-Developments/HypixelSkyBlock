package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "giveskyblockxp",
        description = "Gives yourself skyblock xp",
        usage = "/giveskyblockxp <cause>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class AwardHypixelXPCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
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
