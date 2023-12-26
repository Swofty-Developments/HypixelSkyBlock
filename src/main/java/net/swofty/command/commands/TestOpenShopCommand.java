package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.gui.inventory.inventories.shop.GUIShopFarmer;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(description = "Debug cmd",
        usage = "/testopenshop",
        permission = Rank.ADMIN,
        aliases = "testopenshopgui",
        allowsConsole = false)
public class TestOpenShopCommand extends SkyBlockCommand
{
      @Override
      public void run(MinestomCommand command) {
            command.addSyntax((sender, context) -> {
                  SkyBlockPlayer player = (SkyBlockPlayer) sender;
                  new GUIShopFarmer().open(player);
            });
      }
}
