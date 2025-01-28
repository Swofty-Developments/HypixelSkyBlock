package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.GUIInventoryCreative;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "e",
        description = "Open the E menu",
        usage = "/e",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ItemListCommand extends SkyBlockCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            new GUIInventoryCreative().open((SkyBlockPlayer) sender);
        });

        ArgumentString lookup = new ArgumentString("lookup");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String lookupValue = context.get(lookup);
            new GUIInventoryCreative().open((SkyBlockPlayer) sender, lookupValue, 1);
        }, lookup);
    }
}
