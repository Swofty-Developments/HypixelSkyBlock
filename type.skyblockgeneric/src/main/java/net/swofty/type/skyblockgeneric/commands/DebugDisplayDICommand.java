package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.utility.DamageIndicator;

@CommandParameters(aliases = "dmgindicdisplaydebugcmd",
        description = "Display damage indic",
        usage = "/dmgindicdisplaydebugcmd <dmg> <crit>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class DebugDisplayDICommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
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
