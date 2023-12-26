package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "resetmystocksnow",
        description = "Resets your shop stocks back to MAXIMUM",
        usage = "/resetmystocks",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ResetMyStocksCommand extends SkyBlockCommand
{
      @Override
      public void run(MinestomCommand command) {
            command.addSyntax((sender, context) -> {
                  SkyBlockPlayer player = (SkyBlockPlayer) sender;
                  player.getShoppingData().resetStocks();
                  player.sendMessage("§aWhoop!");
            });
      }
}
