package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointDouble;
import net.swofty.user.categories.Rank;
import net.swofty.user.SkyBlockPlayer;

@CommandParameters(description = "Sets your purse balance", usage = "/coins <amount>", permission = Rank.ADMIN, allowsConsole = false)
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
