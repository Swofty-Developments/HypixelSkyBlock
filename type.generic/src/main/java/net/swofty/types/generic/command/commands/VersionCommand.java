package net.swofty.types.generic.command.commands;

import net.swofty.commons.MinecraftVersion;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Tells the player their version",
        usage = "/version",
        permission = Rank.DEFAULT,
        aliases = "playerversion",
        allowsConsole = false)
public class VersionCommand extends SkyBlockCommand {

    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            MinecraftVersion playerVersion = ((SkyBlockPlayer) sender).getVersion();
            sender.sendMessage("§aYou are currently running §e" + playerVersion.name() + "§a!");
        });
    }
}
