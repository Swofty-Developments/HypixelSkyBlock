package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.DamageIndicator;

@CommandParameters(aliases = "dmgindicdisplaydebugcmd",
        description = "Display damage indic",
        usage = "/dmgindicdisplaydebugcmd <dmg> <crit>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class DebugDisplayDICommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentBoolean crit = ArgumentType.Boolean("critical");
        ArgumentInteger damage = ArgumentType.Integer("damage");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            new DamageIndicator()
                    .damage(context.get(damage))
                    .pos(player.getPosition())
                    .critical(context.get(crit))
                    .display(player.getInstance());

        }, damage, crit);
    }
}
