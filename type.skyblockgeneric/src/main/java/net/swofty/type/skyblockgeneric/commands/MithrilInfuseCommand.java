package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.item.ItemComponent;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.item.ItemAttributeHandler;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.components.MinionComponent;
import net.swofty.type.generic.item.updater.PlayerItemOrigin;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "mithrilinfuse",
        description = "Mithril Infuses the minion in the players hand",
        usage = "/mithrilinfuse",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class MithrilInfuseCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = ((HypixelPlayer) sender);
            if(!(new SkyBlockItem(player.getItemInMainHand()).hasComponent(MinionComponent.class))){
                player.sendMessage("§cMithril Infusions can only be applied to minions.");
                return;
            }
            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                try {
                    itemAttributeHandler.setMithrilInfused(true);
                    player.sendMessage("§aMithril Infusion applied to " + player.getItemInMainHand().get(
                            ItemComponent.CUSTOM_NAME
                    ).toString() + ".");
                } catch (IllegalArgumentException e) {
                    player.sendMessage("§c" + e.getMessage());
                }
            });
        });
    }
}
