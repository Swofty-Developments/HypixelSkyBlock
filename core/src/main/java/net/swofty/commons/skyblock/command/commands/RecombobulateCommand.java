package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.item.attribute.AttributeHandler;
import net.swofty.commons.skyblock.item.updater.PlayerItemOrigin;
import net.swofty.commons.skyblock.item.updater.PlayerItemUpdater;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

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