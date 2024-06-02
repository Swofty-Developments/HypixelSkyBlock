package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "resetmystocksnow",
        description = "Resets your shop stocks back to MAXIMUM",
        usage = "/resetmystocks",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ResetMyStocksCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getShoppingData().resetStocks();
            player.sendMessage("§aWhoop!");
        });
    }
}
