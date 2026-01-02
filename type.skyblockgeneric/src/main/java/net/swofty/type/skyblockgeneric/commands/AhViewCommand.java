package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.minecraft.ArgumentUUID;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionBrowser;
import net.swofty.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.UUID;

@CommandParameters(aliases = "auctionview",
        description = "Views an auction",
        usage = "/ahview <uuid>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class AhViewCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentUUID uuid = new ArgumentUUID("uuid");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            UUID ah = context.get(uuid);

            new GUIAuctionViewItem(ah, new GUIAuctionBrowser()).open((SkyBlockPlayer) sender);
        }, uuid);
    }
}
