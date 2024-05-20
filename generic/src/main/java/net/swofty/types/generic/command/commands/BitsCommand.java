package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointInteger;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Sets your bits balance",
        usage = "/bits <amount>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class BitsCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentNumber<Integer> intArgument = ArgumentType.Integer("amount").min(0);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.getDataHandler().get(DataHandler.Data.BITS, DatapointInteger.class).setValue(context.get(intArgument));
            sender.sendMessage("§aSuccessfully set bits to to §e" + context.getRaw(intArgument) + "§a.");
        }, intArgument);
    }
}
