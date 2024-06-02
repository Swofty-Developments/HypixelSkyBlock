package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Toggles drop alert",
        usage = "/toggledropalert",
        permission = Rank.DEFAULT,
        aliases = "dropalert",
        allowsConsole = false)
public class ToggleDropAlertCommand extends SkyBlockCommand {

    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            DatapointToggles.Toggles toggles = ((SkyBlockPlayer) sender).getToggles();

            toggles.inverse(DatapointToggles.Toggles.ToggleType.DISABLE_DROP_MESSAGES);

            sender.sendMessage("§aDrop alerts toggled " + (toggles.get(DatapointToggles.Toggles.ToggleType.DISABLE_DROP_MESSAGES)
                    ? "§cOFF" : "§aON") + "§a!");
        });
    }
}
