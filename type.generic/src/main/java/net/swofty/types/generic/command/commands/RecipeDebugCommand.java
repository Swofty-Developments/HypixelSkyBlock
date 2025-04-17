package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Prints recipe debug", usage = "", permission = Rank.ADMIN, allowsConsole = false)
public class RecipeDebugCommand extends SkyBlockCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;


        });
    }
}
