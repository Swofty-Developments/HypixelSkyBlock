package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.utility.StringUtility;

@CommandParameters(description = "Sets your purse balance",
        usage = "/coins <amount>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class CoinsCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentNumber<Double> doubleArgument = ArgumentType.Double("amount").min(0D);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(context.get(doubleArgument));
            player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                    .ticksToLast(20)
                    .display(StringUtility.commaify(context.get(doubleArgument)))
                    .build(), StatisticDisplayReplacement.DisplayType.COINS);

            sender.sendMessage("§aSuccessfully set coins to to §e" + context.getRaw(doubleArgument) + "§a.");
        }, doubleArgument);
    }
}
