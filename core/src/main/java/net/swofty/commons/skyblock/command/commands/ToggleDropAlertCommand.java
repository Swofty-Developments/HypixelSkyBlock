package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointBoolean;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(description = "Toggles drop alert",
        usage = "/toggledropalert",
        permission = Rank.DEFAULT,
        aliases = "dropalert",
        allowsConsole = false)
public class ToggleDropAlertCommand extends SkyBlockCommand {

    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            DatapointBoolean isDisabled = ((SkyBlockPlayer) sender).getDataHandler()
                    .get(DataHandler.Data.DISABLE_DROP_MESSAGE, DatapointBoolean.class);

            isDisabled.setValue(!isDisabled.getValue());
            sender.sendMessage("§aDrop alerts toggled " + (isDisabled.getValue() ? "§cOFF" : "§aON") + "§a!");
        });
    }
}
