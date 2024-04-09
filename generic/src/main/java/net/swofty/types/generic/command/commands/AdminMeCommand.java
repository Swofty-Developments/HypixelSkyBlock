package net.swofty.types.generic.command.commands;

import net.minestom.server.permission.Permission;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.ArrayList;
import java.util.List;

@CommandParameters(aliases = "forceadmin",
        description = "Literally just gives me admin",
        usage = "/adminme",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class AdminMeCommand extends SkyBlockCommand {

    private static final List<String> ADMIN_LIST = List.of("8fc7011b-e7a7-4e51-a80d-29d7b6dd7952",
            "770138a6-dccf-4b97-9c50-3c2c731e1ae8",
            "00a7e044-cae5-408b-8a82-87716f15dd9c",
            "53caa0f5-f549-4896-88d0-3d52f9554443"
    );

    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            if (!ADMIN_LIST.contains(player.getUuid().toString())) {
                sender.sendMessage("§cNope.");
                return;
            }

            player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).setValue(Rank.ADMIN);

            sender.sendMessage("§aSuccessfully set rank to §c[ADMIN]§a.");
        });
    }
}
