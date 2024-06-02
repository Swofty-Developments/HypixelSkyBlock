package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Toggles whether or not you are in build mode",
        usage = "/build",
        permission = Rank.ADMIN,
        aliases = "buildmode",
        allowsConsole = false)
public class BuildCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.setBypassBuild(!player.isBypassBuild());

            sender.sendMessage("§aBuild mode has been " + (player.isBypassBuild() ? "§aENABLED" : "§cDISABLED") + "§a.");
        });
    }
}
