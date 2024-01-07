package net.swofty.commons.skyblock.command.commands;

import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointRank;
import net.swofty.commons.skyblock.command.CommandParameters;
import net.swofty.commons.skyblock.command.SkyBlockCommand;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.categories.Rank;

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
