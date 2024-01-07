package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(aliases = "resetmystocksnow",
        description = "Resets your shop stocks back to MAXIMUM",
        usage = "/resetmystocks",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ResetMyStocksCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getShoppingData().resetStocks();
            player.sendMessage("§aWhoop!");
        });
    }
}
