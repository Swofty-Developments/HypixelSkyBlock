package net.swofty.commons.skyblock.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointDouble;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

@CommandParameters(description = "Sets your purse balance",
        usage = "/coins <amount>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class CoinsCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentNumber<Double> doubleArgument = ArgumentType.Double("amount").min(0D);

        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(context.get(doubleArgument));

            sender.sendMessage("§aSuccessfully set coins to to §e" + context.getRaw(doubleArgument) + "§a.");
        }, doubleArgument);
    }
}
