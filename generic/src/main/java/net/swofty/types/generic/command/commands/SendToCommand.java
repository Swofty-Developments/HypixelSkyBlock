package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "transfer",
        description = "Transfers a player to another server",
        usage = "/sendto <server_type>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class SendToCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ServerType> serverType = new ArgumentEnum<>("server_type", ServerType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            ServerType type = context.get(serverType);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.sendTo(type, true);
        }, serverType);
    }
}
