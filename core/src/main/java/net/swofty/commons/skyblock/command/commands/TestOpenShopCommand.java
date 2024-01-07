package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.gui.inventory.inventories.shop.GUIShopFarmer;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(description = "Debug cmd",
        usage = "/testopenshop",
        permission = Rank.ADMIN,
        aliases = "testopenshopgui",
        allowsConsole = false)
public class TestOpenShopCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            new GUIShopFarmer().open(player);
        });
    }
}
