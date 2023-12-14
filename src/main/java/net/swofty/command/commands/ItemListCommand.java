package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.gui.inventory.inventories.GUICreative;
import net.swofty.user.Rank;
import net.swofty.user.SkyBlockPlayer;

@CommandParameters(aliases = "e", description = "Open the E menu", usage = "/e", permission = Rank.ADMIN, allowsConsole = false)
public class ItemListCommand extends SkyBlockCommand
{
      @Override
      public void run(MinestomCommand command) {
            command.addSyntax((sender, context) -> {
                  new GUICreative("", 1).open((SkyBlockPlayer) sender);
            });
      }
}
