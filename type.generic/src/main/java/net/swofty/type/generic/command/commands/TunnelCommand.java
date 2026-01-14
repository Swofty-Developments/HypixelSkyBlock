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
        aliases = "tpto teleportto",
        description = "Teleport to a player across servers",
        usage = "/tunnel <player>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class TunnelCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord playerArgument = ArgumentType.Word("player");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String targetName = context.get(playerArgument);

            sender.sendMessage("§2Searching for player §e" + targetName + "§2...");

            // Do proxy/redis lookup off-thread
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
                        UUID finalTargetUUID = null;
                        player.scheduler().buildTask(() ->
                                sender.sendMessage("§cPlayer §e" + targetName + " §cis not online.")
                        ).schedule();
                        return;
                    }

                    UUID finalTargetUUID = targetUUID;
                    UnderstandableProxyServer finalTargetServer = targetServer;

                    // SAME SERVER CHECK (safe off-thread; just reads local list)
                    Player localTarget = HypixelGenericLoader.getLoadedPlayers().stream()
                            .filter(p -> p.getUuid().equals(finalTargetUUID))
                            .findFirst()
                            .orElse(null);

                    if (localTarget != null) {
                        // Hop back onto Minestom thread for teleport + messaging
                        Player finalLocalTarget = localTarget;
                        player.scheduler().buildTask(() -> {
                            player.teleport(finalLocalTarget.getPosition());
                            sender.sendMessage("§2Teleported to " + HypixelPlayer.getColouredDisplayName(finalTargetUUID) + "§2.");
                        }).schedule();
                        return;
                    }

                    // Different server - transfer (do Minestom operations on scheduler)
                    String serverDisplay = finalTargetServer.shortName() != null && !finalTargetServer.shortName().isEmpty()
                            ? finalTargetServer.shortName()
                            : finalTargetServer.name();

                    player.scheduler().buildTask(() -> {
                        sender.sendMessage("§aSending you to §e" + serverDisplay + " §awith " +
                                HypixelPlayer.getColouredDisplayName(finalTargetUUID) + "§a.");

                        player.getHookManager().setHook("tpto_target", finalTargetUUID.toString());
                        player.sendTo(finalTargetServer.uuid(), true);
                    }).schedule();

                } catch (Exception e) {
                    player.scheduler().buildTask(() ->
                            sender.sendMessage("§cAn error occurred while searching for the player.")
                    ).schedule();
                    e.printStackTrace();
                }
            });
        }, playerArgument);
    }
}
