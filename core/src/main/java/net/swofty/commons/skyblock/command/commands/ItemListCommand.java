package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.gui.inventory.inventories.GUICreative;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(aliases = "e",
        description = "Open the E menu",
        usage = "/e",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ItemListCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            new GUICreative().open((SkyBlockPlayer) sender);
        });
    }
}
