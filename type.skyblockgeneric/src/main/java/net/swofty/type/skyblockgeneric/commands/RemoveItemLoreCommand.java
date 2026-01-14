package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "rll",
        description = "Removes a line of lore from a sandbox item",
        usage = "/removeitemlore <line_number>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class RemoveItemLoreCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentInteger lineNumber = new ArgumentInteger("line_number");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!ConfigProvider.settings().isSandbox()) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            int line = context.get(lineNumber);

            SkyBlockItem itemInHand = new SkyBlockItem(player.getItemInMainHand());
            ItemType type = itemInHand.getAttributeHandler().getPotentialType();

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
