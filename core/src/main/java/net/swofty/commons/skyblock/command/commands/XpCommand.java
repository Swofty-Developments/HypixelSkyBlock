package net.swofty.commons.skyblock.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(aliases = "experience",
        description = "Sets your experience levels",
        usage = "/xp <level>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class XpCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentInteger levelArgument = ArgumentType.Integer("level");

        command.addSyntax((sender, context) -> {
            int level = context.get(levelArgument);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.setLevel(level);
        }, levelArgument);
    }
}
