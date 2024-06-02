package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.GUICreative;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "e",
        description = "Open the E menu",
        usage = "/e",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ItemListCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            new GUICreative().open((SkyBlockPlayer) sender);
        });

        ArgumentString lookup = new ArgumentString("lookup");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String lookupValue = context.get(lookup);
            new GUICreative().open((SkyBlockPlayer) sender, lookupValue, 1);
        }, lookup);
    }
}
