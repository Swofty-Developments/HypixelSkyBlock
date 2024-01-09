package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBoolean;
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
            DatapointBoolean isDisabled = ((SkyBlockPlayer) sender).getDataHandler()
                    .get(DataHandler.Data.DISABLE_DROP_MESSAGE, DatapointBoolean.class);

            isDisabled.setValue(!isDisabled.getValue());
            sender.sendMessage("§aDrop alerts toggled " + (isDisabled.getValue() ? "§cOFF" : "§aON") + "§a!");
        });
    }
}
