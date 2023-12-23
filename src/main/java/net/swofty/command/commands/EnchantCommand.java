package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "ench",
        description = "Enchants the contents o the players hand",
        usage = "/enchant",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class EnchantCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        // TODO
    }
}
