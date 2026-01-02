package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.utility.ProxyServersCache;

import java.util.UUID;

@CommandParameters(
        aliases = "transfer",
        description = "Transfers a player to another server",
        usage = "/sendto <server_type|server_id>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class SendToCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {


        ArgumentEnum<ServerType> serverType =
                new ArgumentEnum<>("server_type", ServerType.class);


        ArgumentString serverId = new ArgumentString("server_id")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    for (String s : ProxyServersCache.getSuggestions()) {
                        suggestion.addEntry(new SuggestionEntry(s));
                    }
                });


        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            player.sendTo(context.get(serverType), true);
        }, serverType);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String input = context.get(serverId);

            UUID uuid = ProxyServersCache.resolve(input);
            if (uuid != null) {
                player.sendTo(uuid, true);
            } else {
                player.sendMessage("§cServer not found.");
            }
        }, serverId);
    }
}
