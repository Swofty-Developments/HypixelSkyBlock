package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(aliases = "cf factory",
        description = "Opens the Chocolate Factory menu",
        usage = "/chocolatefactory",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class ChocolateFactoryCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            new GUIChocolateFactory().open(player);
        });
    }
}
