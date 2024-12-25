package net.swofty.types.generic.command.commands;

import net.swofty.commons.item.ReforgeType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemAttributeHandler;
import net.swofty.types.generic.item.components.ReforgableComponent;
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
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ((SkyBlockPlayer) sender).updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                ReforgeType reforgeType = item.getComponent(ReforgableComponent.class).getReforgeType();
                ReforgeType.Reforge reforge = reforgeType.getReforges().get(MathUtility.random(0, reforgeType.getReforges().size() - 1));
                try {
                    itemAttributeHandler.setReforge(reforge);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§c" + e.getMessage());
                }
            });
        });
    }
}
