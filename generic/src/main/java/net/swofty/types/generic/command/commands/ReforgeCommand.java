package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.attribute.AttributeHandler;
import net.swofty.types.generic.item.impl.Reforgable;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.MathUtility;

@CommandParameters(aliases = "reforgeitem",
        description = "Reforges the item in the players hand",
        usage = "/reforge",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class ReforgeCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            ((SkyBlockPlayer) sender).updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                AttributeHandler attributeHandler = item.getAttributeHandler();
                ReforgeType reforgeType = ((Reforgable) item.getGenericInstance()).getReforgeType();
                ReforgeType.Reforge reforge = reforgeType.getReforges().get(MathUtility.random(0, reforgeType.getReforges().size() - 1));
                try {
                    attributeHandler.setReforge(reforge);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§c" + e.getMessage());
                }
            });
        });
    }
}
