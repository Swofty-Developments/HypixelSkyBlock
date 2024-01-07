package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(description = "Toggles whether or not you are in build mode",
        usage = "/build",
        permission = Rank.ADMIN,
        aliases = "buildmode",
        allowsConsole = false)
public class BuildCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.setBypassBuild(!player.isBypassBuild());

            sender.sendMessage("§aBuild mode has been " + (player.isBypassBuild() ? "§aENABLED" : "§cDISABLED") + "§a.");
        });
    }
}
