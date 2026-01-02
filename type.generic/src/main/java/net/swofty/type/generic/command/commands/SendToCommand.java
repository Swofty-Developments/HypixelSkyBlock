package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

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
                    GameManager.getServers().values().forEach(list -> {
                        list.forEach(gs -> {
                            suggestion.addEntry(new SuggestionEntry(gs.shortDisplayName()));
                            suggestion.addEntry(new SuggestionEntry(gs.displayName()));
                        });
                    });
                });


        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            ServerType type = context.get(serverType);

            player.sendTo(type, true);
        }, serverType);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String input = context.get(serverId);


            GameManager.GameServer gs = GameManager.getFromDisplayName(input);
            if (gs != null) {
                player.sendTo(gs.internalID(), true);
                return;
            }


            try {
                UUID uuid = UUID.fromString(input);
                gs = GameManager.getFromUUID(uuid);
                if (gs != null) {
                    player.sendTo(gs.internalID(), true);
                    return;
                }
            } catch (IllegalArgumentException ignored) {}

            player.sendMessage("§cServer not found.");
        }, serverId);
    }
}
