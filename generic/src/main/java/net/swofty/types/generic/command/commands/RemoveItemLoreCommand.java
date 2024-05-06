package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.commons.Configuration;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "rll",
        description = "Removes a line of lore from a sandbox item",
        usage = "/removeitemlore <line_number>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class RemoveItemLoreCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentInteger lineNumber = new ArgumentInteger("line_number");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (Configuration.get("sandbox-mode").equals("false")) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            int line = context.get(lineNumber);

            SkyBlockItem itemInHand = new SkyBlockItem(player.getItemInMainHand());
            ItemType type = itemInHand.getAttributeHandler().getItemTypeAsType();

            if (type != ItemType.SANDBOX_ITEM) {
                player.sendMessage("§cYou can only remove the lore of sandbox items.");
                return;
            }

            if (line < 1) {
                player.sendMessage("§cThe line number must be greater than 0.");
                return;
            }

            if (line > itemInHand.getAttributeHandler().getSandboxData().getLore().size()) {
                player.sendMessage("§cThe line number must be less than or equal to the number of lore lines.");
                return;
            }

            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                item.getAttributeHandler().getSandboxData().getLore().remove(line - 1);
            });

            player.sendMessage("§aRemoved the lore line at line §e" + line + "§a.");
        }, lineNumber);
    }
}
