package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.commons.Configuration;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "sll",
        description = "Updates the name of a player's item",
        usage = "/setitemlore <line_number> <lore>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class SetItemLoreCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentInteger lineNumber = new ArgumentInteger("line_number");
        ArgumentStringArray lore = new ArgumentStringArray("lore");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (Configuration.get("sandbox-mode").equals("false")) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            int line = context.get(lineNumber);
            String[] newLore = context.get(lore);

            SkyBlockItem itemInHand = new SkyBlockItem(player.getItemInMainHand());
            ItemType type = itemInHand.getAttributeHandler().getItemTypeAsType();

            if (type != ItemType.SANDBOX_ITEM) {
                player.sendMessage("§cYou can only set the lore of sandbox items.");
                return;
            }

            if (line < 1) {
                player.sendMessage("§cThe line number must be greater than 0.");
                return;
            }

            if (line > 100) {
                player.sendMessage("§cThe line number must be less than or equal to 100.");
                return;
            }

            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                if (line >= item.getAttributeHandler().getSandboxData().getLore().size()) {
                    // Add blank number of lines leading up to line
                    for (int i = item.getAttributeHandler().getSandboxData().getLore().size(); i < line; i++) {
                        item.getAttributeHandler().getSandboxData().getLore().add("");
                    }
                }

                item.getAttributeHandler().getSandboxData().getLore().set(line - 1, String.join(" ", newLore));
            });

            player.sendMessage("§aUpdated the lore line at line §e" + line + "§a to §e" + String.join(" ", newLore) + "§a.");
        }, lineNumber, lore);
    }
}
