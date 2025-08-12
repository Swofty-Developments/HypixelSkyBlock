package net.swofty.type.skyblockgeneric.commands;

import net.swofty.commons.MinecraftVersion;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Tells the player their version",
        usage = "/version",
        permission = Rank.DEFAULT,
        aliases = "playerversion",
        allowsConsole = false)
public class VersionCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            MinecraftVersion playerVersion = ((HypixelPlayer) sender).getVersion();
            sender.sendMessage("§aYou are currently running §e" + playerVersion.name() + "§a!");
        });
    }
}
