package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.shop.GUIShopFarmMerchant;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

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
            new GUIShopFarmMerchant().open(player);
        });
    }
}
