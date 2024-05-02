package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.swofty.commons.Configuration;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "updatename",
        description = "Updates the name of a player's item",
        usage = "/setitemname <name>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class SetItemNameCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentString name = new ArgumentString("name");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (Configuration.get("sandbox-mode").equals("false")) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String newName = context.get(name);

            SkyBlockItem itemInHand = new SkyBlockItem(player.getItemInMainHand());
            ItemType type = itemInHand.getAttributeHandler().getItemTypeAsType();

            if (type != ItemType.SANDBOX_ITEM) {
                player.sendMessage("§cYou can only set the name of sandbox items.");
                return;
            }

            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                item.getAttributeHandler().getSandboxData().setDisplayName(newName);
            });

            player.sendMessage("§aUpdated the name of the item in your hand to §e" + newName + "§a.");
        }, name);
    }
}
