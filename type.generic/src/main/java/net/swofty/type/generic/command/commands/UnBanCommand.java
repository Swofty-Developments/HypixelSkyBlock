package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.utils.mojang.MojangUtils;
import net.swofty.commons.punishment.PunishmentRedis;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

import java.io.IOException;
import java.util.UUID;

@CommandParameters(
        description = "Unban a player from the server.",
        usage = "/unban <player>",
        aliases = "unban pardon unbanip pardonip",
        permission = Rank.STAFF,
        allowsConsole = true
)
public class UnBanCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        Argument<String> argument = ArgumentType.String("player").setSuggestionCallback((sender, context, suggestion) -> {
            PunishmentRedis.getAllBannedPlayerIds().thenAccept((id) -> {
                for (String playerName : id) {
                    UUID playerUuid = UUID.fromString(playerName);
                    suggestion.addEntry(new SuggestionEntry(playerUuid.toString()));
                }
            });
        });

        command.addSyntax((sender, context) -> {
            String playerName = context.get(argument);

            sender.sendMessage("§8Processing unban for player " + playerName + "...");
            try {
                PunishmentRedis.revoke(MojangUtils.getUUID(playerName)).thenRun(() -> {
                    sender.sendMessage("§aSuccessfully unbanned player: " + playerName);
                });
            } catch (IOException e) {
                sender.sendMessage("§cCould not find player: " + playerName);
            }
        }, argument);
    }
}
