package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.swofty.commons.ServerType;
import net.swofty.proxyapi.ProxyInformation;
import net.swofty.type.generic.HypixelConst;
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

    private static final long SUGGESTION_TTL_MS = 5_000L;

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString serverArgument = new ArgumentString("server");

        // Set up suggestions to show both ServerType enums and server IDs
        serverArgument.setSuggestionCallback((sender, context, suggestion) -> {
            // Add all ServerType enum values first (these are the main server types)
            for (ServerType type : ServerType.values()) {
                suggestion.addEntry(new SuggestionEntry(type.name()));
            }

            // Then add server IDs (like l1a, m3g, etc.) from cache
            if (ProxyServersCache.shouldRefresh(SUGGESTION_TTL_MS)) {
                new ProxyInformation().getAllServersInformation()
                        .thenAccept(ProxyServersCache::update)
                        .exceptionally(ex -> null);
            }
            for (String s : ProxyServersCache.getSuggestions()) {
                suggestion.addEntry(new SuggestionEntry(s));
            }
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String input = context.get(serverArgument);

            // Try to parse as ServerType enum first
            try {
                ServerType targetType = ServerType.valueOf(input.toUpperCase());
                ServerType currentType = HypixelConst.getTypeLoader().getType();

                // Check if already on this server type
                if (currentType == targetType) {
                    sender.sendMessage("§cYou are already on a " + targetType.name() + " server.");
                    return;
                }

                player.sendTo(targetType, true);
                return;
            } catch (IllegalArgumentException ignored) {
                // Not a valid ServerType, continue to check server IDs
            }

            // Try to resolve as a server ID (l1a, m3g, etc.)
            UUID targetUUID = ProxyServersCache.resolve(input);
            if (targetUUID != null) {
                UUID currentUUID = HypixelConst.getServerUUID();

                // Check if already on this specific server
                if (currentUUID != null && currentUUID.equals(targetUUID)) {
                    sender.sendMessage("§cYou are already on this server.");
                    return;
                }

                player.sendTo(targetUUID, true);
            } else {
                sender.sendMessage("§cServer not found. Use tab to see available servers.");
            }
        }, serverArgument);
    }
}
