package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.ItemAttributeHandler;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "mithrilinfuse",
        description = "Mithril Infuses the minion in the players hand",
        usage = "/mithrilinfuse",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class MithrilInfuseCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            if(!(new SkyBlockItem(player.getInventory().getItemInMainHand()).getGenericInstance() instanceof Minion)){
                player.sendMessage("§cMithril Infusions can only be applied to minions.");
                return;
            }
            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                try {
                    itemAttributeHandler.setMithrilInfused(true);
                    player.sendMessage("§aMithril Infusion applied to " + player.getInventory().getItemInMainHand().getDisplayName().toString() + ".");
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c" + e.getMessage());
                }
            });
        });
    }
}
