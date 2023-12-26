package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.item.ReforgeType;
import net.swofty.item.attribute.AttributeHandler;
import net.swofty.item.impl.Reforgable;
import net.swofty.item.updater.PlayerItemOrigin;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;
import net.swofty.utility.MathUtility;

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
