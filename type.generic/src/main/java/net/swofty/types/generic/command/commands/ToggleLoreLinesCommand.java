package net.swofty.types.generic.command.commands;

import net.swofty.commons.Configuration;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "togglelines",
        description = "Toggles the lore lines of a sandbox item",
        usage = "/togglelines",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class ToggleLoreLinesCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (Configuration.get("sandbox-mode").equals("false")) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            SkyBlockItem itemInHand = new SkyBlockItem(player.getItemInMainHand());
            ItemType type = itemInHand.getAttributeHandler().getItemTypeAsType();

            if (type != ItemType.SANDBOX_ITEM) {
                player.sendMessage("§cYou can only toggle the lore lines of sandbox items.");
                return;
            }

            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                item.getAttributeHandler().getSandboxData().setShowLoreLinesToggle(!item.getAttributeHandler().getSandboxData().isShowLoreLinesToggle());
            });

            player.sendMessage("§aToggled the lore lines of the item in your hand.");
        });
    }
}
