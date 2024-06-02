package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

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
            if (!permissionCheck(sender)) return;

            int level = context.get(levelArgument);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.setLevel(level);
        }, levelArgument);
    }
}
