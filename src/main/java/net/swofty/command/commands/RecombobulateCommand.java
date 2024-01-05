package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.attribute.AttributeHandler;
import net.swofty.item.updater.PlayerItemOrigin;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "recomb",
        description = "Recombobulates the item in the players hand",
        usage = "/recombobulate",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class RecombobulateCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {

            new PlayerItemUpdater((player, item) -> {
                AttributeHandler attributeHandler = item.getAttributeHandler();
                attributeHandler.setRecombobulated(!attributeHandler.isRecombobulated());
                return attributeHandler.asSkyBlockItem();
            }).queueUpdate((SkyBlockPlayer) sender, PlayerItemOrigin.MAIN_HAND).thenAccept((item) -> {
                AttributeHandler attributeHandler = new AttributeHandler(item);
                sender.sendMessage("§aRecombobulated: §d" + attributeHandler.isRecombobulated());
            });

        });
    }
}