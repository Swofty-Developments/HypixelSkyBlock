package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "experience",
        description = "Sets your experience levels",
        usage = "/xp <level>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class XpCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentInteger levelArgument = ArgumentType.Integer("level");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            int level = context.get(levelArgument);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.setLevel(level);
        }, levelArgument);
    }
}
