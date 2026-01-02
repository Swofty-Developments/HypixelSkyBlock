package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Toggles whether or not you are in build mode",
        usage = "/build",
        permission = Rank.STAFF,
        aliases = "buildmode",
        allowsConsole = false)
public class BuildCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.setBypassBuild(!player.isBypassBuild());

            sender.sendMessage("§aBuild mode has been " + (player.isBypassBuild() ? "§aENABLED" : "§cDISABLED") + "§a.");
        });
    }
}
