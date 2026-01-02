package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "transfer",
        description = "Transfers a player to another server",
        usage = "/sendto <server_type>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class SendToCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ServerType> serverType = new ArgumentEnum<>("server_type", ServerType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            ServerType type = context.get(serverType);
            HypixelPlayer player = (HypixelPlayer) sender;

            player.sendTo(type, true);
        }, serverType);
    }
}
