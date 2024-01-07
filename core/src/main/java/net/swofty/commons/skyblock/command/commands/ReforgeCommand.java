package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.item.ReforgeType;
import net.swofty.commons.skyblock.item.attribute.AttributeHandler;
import net.swofty.commons.skyblock.item.impl.Reforgable;
import net.swofty.commons.skyblock.item.updater.PlayerItemOrigin;
import net.swofty.commons.skyblock.item.updater.PlayerItemUpdater;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;
import net.swofty.commons.skyblock.utility.MathUtility;

@CommandParameters(aliases = "reforgeitem",
        description = "Reforges the item in the players hand",
        usage = "/reforge",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ReforgeCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            new PlayerItemUpdater((player, item) -> {
                AttributeHandler attributeHandler = item.getAttributeHandler();
                ReforgeType reforgeType = ((Reforgable) item.getGenericInstance()).getReforgeType();
                ReforgeType.Reforge reforge = reforgeType.getReforges().get(MathUtility.random(0, reforgeType.getReforges().size() - 1));
                try {
                    attributeHandler.setReforge(reforge);
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c" + e.getMessage());
                }
                return attributeHandler.asSkyBlockItem();
            }).queueUpdate((SkyBlockPlayer) sender, PlayerItemOrigin.MAIN_HAND).thenAccept((item) -> {
                AttributeHandler attributeHandler = new AttributeHandler(item);
                sender.sendMessage("§aReforge: §d" + attributeHandler.getReforge().prefix());
            });
        });
    }
}
