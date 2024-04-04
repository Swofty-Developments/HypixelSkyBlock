package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.minecraft.ArgumentUUID;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionBrowser;
import net.swofty.types.generic.gui.inventory.inventories.auction.GUIAuctionViewItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.UUID;

@CommandParameters(aliases = "auctionview",
        description = "Views an auction",
        usage = "/ahview <uuid>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AhViewCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentUUID uuid = new ArgumentUUID("uuid");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            UUID ah = context.get(uuid);

            new GUIAuctionViewItem(ah, new GUIAuctionBrowser()).open((SkyBlockPlayer) sender);
        }, uuid);
    }
}
