package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.Wiki;

@CommandParameters(
    aliases = "wikithis wikihand wikiinhand",
    description = "Shows page link of the item held towards the official Hypixel Wiki.",
    usage = "/wikithis",
    permission = Rank.DEFAULT,
    allowsConsole = false
)
public class WikiThisCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            Wiki.wikiThis(player);
        });
    }
}
