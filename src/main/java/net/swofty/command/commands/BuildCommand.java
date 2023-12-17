package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointDouble;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

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
