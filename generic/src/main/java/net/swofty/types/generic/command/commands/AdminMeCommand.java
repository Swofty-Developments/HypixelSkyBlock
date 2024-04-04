package net.swofty.types.generic.command.commands;

import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "forceadmin",
        description = "Literally just gives me admin",
        usage = "/adminme",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class AdminMeCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            if (!player.getUsername().equals("Swofty") && !player.getUsername().equals("Foodzz")) {
                sender.sendMessage("§cNope.");
                return;
            }

            player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).setValue(Rank.ADMIN);

            sender.sendMessage("§aSuccessfully set rank to §c[ADMIN]§a.");
        });
    }
}
