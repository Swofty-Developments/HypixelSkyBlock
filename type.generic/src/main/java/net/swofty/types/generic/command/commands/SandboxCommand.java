package net.swofty.types.generic.command.commands;

import net.swofty.commons.Configuration;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "getsandboxitem",
        description = "Gets a sandbox item",
        usage = "/getsandboxitem",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class SandboxCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (Configuration.get("sandbox-mode").equals("false")) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.addAndUpdateItem(ItemType.SANDBOX_ITEM);
            player.sendMessage("§aAdded a sandbox item to your inventory.");
        });
    }
}
