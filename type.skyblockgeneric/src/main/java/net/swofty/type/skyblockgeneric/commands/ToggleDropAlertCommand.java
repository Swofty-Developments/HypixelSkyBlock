package net.swofty.type.skyblockgeneric.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Toggles drop alert",
        usage = "/toggledropalert",
        permission = Rank.DEFAULT,
        aliases = "dropalert",
        allowsConsole = false)
public class ToggleDropAlertCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            DatapointToggles.Toggles toggles = ((HypixelPlayer) sender).getToggles();

            toggles.inverse(DatapointToggles.Toggles.ToggleType.DISABLE_DROP_MESSAGES);

            sender.sendMessage("§aDrop alerts toggled " + (toggles.get(DatapointToggles.Toggles.ToggleType.DISABLE_DROP_MESSAGES)
                    ? "§cOFF" : "§aON") + "§a!");
        });
    }
}
