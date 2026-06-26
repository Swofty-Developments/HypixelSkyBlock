package net.swofty.type.skyblockgeneric.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "awardskyblockxp giveskyblockxp",
        description = "Gives yourself skyblock xp",
        usage = "/giveskyblockxp <cause>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class AwardHypixelXPCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString causeArgument = ArgumentType.String("cause");
        causeArgument.setSuggestionCallback((_, _, suggestion) -> {
            SkyBlockLevelCause.getCauses().forEach((causeKey, causeAbstr) -> {
                suggestion.addEntry(new SuggestionEntry(causeKey, MiniMessage.miniMessage().deserialize("<gold>" + causeAbstr.xpReward() + "<reset>")));
            });
        });

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
            sender.sendMessage("§aAwarded " + cause + " with " + cause.xpReward() + " xp.");
        }, causeArgument);
    }
}
