package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointDouble;
import net.swofty.data.datapoints.DatapointRank;
import net.swofty.user.Rank;
import net.swofty.user.SkyBlockPlayer;

@CommandParameters(aliases = "forceadmin", description = "Literally just gives me admin", usage = "/adminme", permission = Rank.DEFAULT, allowsConsole = false)
public class AdminMeCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            if (!player.getUsername().equals("Swofty") && !player.getUsername().equals("Maploop")) {
                sender.sendMessage("§cNope.");
                return;
            }

            player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).setValue(Rank.ADMIN);

            sender.sendMessage("§aSuccessfully set rank to §c[ADMIN]§a.");
        });
    }
}
