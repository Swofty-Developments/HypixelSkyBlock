package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.proxyapi.ProxyInformation;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.List;
import java.util.UUID;

@CommandParameters(
        aliases = "tpto|tunnel",
        description = "Teleport to a player across servers",
        usage = "/teleportto <player>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class TeleportToCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord playerArgument = ArgumentType.Word("player");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String targetName = context.get(playerArgument);

            sender.sendMessage("§2Searching for player §e" + targetName + "§2...");

            // Search for the target player across all servers
            Thread.startVirtualThread(() -> {
                try {
                    ProxyInformation proxyInfo = new ProxyInformation();
                    List<UnderstandableProxyServer> servers = proxyInfo.getAllServersInformation().join();

                    UUID targetUUID = null;
                    UnderstandableProxyServer targetServer = null;

                    // Find the server where the target player is
                    for (UnderstandableProxyServer server : servers) {
                        for (UUID playerUuid : server.players()) {
                            String playerName = HypixelPlayer.getRawName(playerUuid);
                            if (playerName.equalsIgnoreCase(targetName)) {
                                targetUUID = playerUuid;
                                targetServer = server;
                                break;
                            }
                        }
                        if (targetUUID != null) break;
                    }

                    if (targetUUID == null || targetServer == null) {
                        sender.sendMessage("§cPlayer §e" + targetName + " §cis not online.");
                        return;
                    }

                    UUID finalTargetUUID = targetUUID;
                    UnderstandableProxyServer finalTargetServer = targetServer;

                    // Check if player is on the same server
                    UUID currentServerUUID = proxyInfo.getServerUUID().join();

                    if (currentServerUUID.equals(finalTargetServer.uuid())) {
                        // Same server - just teleport directly
                        Player targetPlayer = HypixelGenericLoader.getLoadedPlayers().stream()
                                .filter(p -> p.getUuid().equals(finalTargetUUID))
                                .findFirst()
                                .orElse(null);

                        if (targetPlayer != null) {
                            player.teleport(targetPlayer.getPosition());
                            sender.sendMessage("§2Teleported to " + HypixelPlayer.getColouredDisplayName(finalTargetUUID) + "§2.");
                        } else {
                            sender.sendMessage("§cCould not find player on this server.");
                        }
                    } else {
                        // Different server - transfer and note that teleport will happen on join
                        String serverDisplay = finalTargetServer.shortName() != null && !finalTargetServer.shortName().isEmpty()
                                ? finalTargetServer.shortName()
                                : finalTargetServer.name();

                        sender.sendMessage("§aSending you to §e" + serverDisplay + " §awith " +
                                HypixelPlayer.getColouredDisplayName(finalTargetUUID) + "§a.");

                        // Store target UUID in hook manager for cross-server teleport
                        player.getHookManager().setHook("tpto_target", finalTargetUUID.toString());

                        // Transfer to target server
                        player.sendTo(finalTargetServer.uuid(), true);
                    }
                } catch (Exception e) {
                    sender.sendMessage("§cAn error occurred while searching for the player.");
                    e.printStackTrace();
                }
            });
        }, playerArgument);
    }
}

