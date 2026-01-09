package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.proxyapi.ProxyInformation;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointHypixelExperience;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.experience.HypixelExperience;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@CommandParameters(
        aliases = "userinfo|uinfo",
        description = "Display detailed information about a player",
        usage = "/userinfo <player>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class UserInfoCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord playerArgument = ArgumentType.Word("player");
        ArgumentWord flagArgument = ArgumentType.Word("flag");

        // Syntax 1: /userinfo -f <player>
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String flag = context.get(flagArgument);
            String targetName = context.get(playerArgument);

            // Validate flag is "-f"
            if (!flag.equals("-f")) {
                sender.sendMessage("§cInvalid flag. Use: /userinfo [-f] <player>");
                return;
            }

            sender.sendMessage("§7Fetching information for §e" + targetName + "§7...");
            fetchAndDisplayUserInfo(sender, targetName);
        }, flagArgument, playerArgument);

        // Syntax 2: /userinfo <player> (without flag)
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String targetName = context.get(playerArgument);
            sender.sendMessage("§7Fetching information for §e" + targetName + "§7...");
            fetchAndDisplayUserInfo(sender, targetName);
        }, playerArgument);
    }

    private void fetchAndDisplayUserInfo(net.minestom.server.command.CommandSender sender, String targetName) {
        // Search for player across all servers
        Thread.startVirtualThread(() -> {
            try {
                ProxyInformation proxyInfo = new ProxyInformation();
                List<UnderstandableProxyServer> servers = proxyInfo.getAllServersInformation().join();

                UUID targetUUID = null;
                UnderstandableProxyServer currentServer = null;
                boolean isOnline = true;

                // Find the player online
                for (UnderstandableProxyServer server : servers) {
                    for (UUID playerUuid : server.players()) {
                        String playerName = HypixelPlayer.getRawName(playerUuid);
                        if (playerName.equalsIgnoreCase(targetName)) {
                            targetUUID = playerUuid;
                            currentServer = server;
                            break;
                        }
                    }
                    if (targetUUID != null) break;
                }

                // If not online, try to get from database
                if (targetUUID == null) {
                    sender.sendMessage("§7Player not online, fetching data from database...");

                    try {
                        // Look up UUID from database by username
                        targetUUID = HypixelDataHandler.getPotentialUUIDFromName(targetName);

                        if (targetUUID == null) {
                            sender.sendMessage("§cPlayer §e" + targetName + " §cnot found in database.");
                            return;
                        }

                        isOnline = false;
                        sender.sendMessage("§7Found player in database, loading information...");
                    } catch (Exception e) {
                        sender.sendMessage("§cError looking up player in database: " + e.getMessage());
                        return;
                    }
                }

                UUID finalTargetUUID = targetUUID;
                UnderstandableProxyServer finalCurrentServer = currentServer;
                boolean finalIsOnline = isOnline;

                    // Load player data
                    HypixelDataHandler dataHandler = HypixelDataHandler.getOfOfflinePlayer(finalTargetUUID);

                    // Get player information
                    String displayName = HypixelPlayer.getDisplayName(finalTargetUUID);
                    Rank rank = dataHandler.get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue();

                    // Get level information
                    HypixelPlayer onlinePlayer = null;
                    int networkLevel = 1;
                    long networkExp = 0;

                    // Try to get from online player first
                    try {
                        onlinePlayer = (HypixelPlayer) net.swofty.type.generic.HypixelGenericLoader.getLoadedPlayers().stream()
                                .filter(p -> p.getUuid().equals(finalTargetUUID))
                                .findFirst()
                                .orElse(null);

                        if (onlinePlayer != null) {
                            networkLevel = onlinePlayer.getExperienceHandler().getLevel();
                            networkExp = onlinePlayer.getExperienceHandler().getExperience();
                        }
                    } catch (Exception ignored) {
                        // Player not online on this server
                    }

                    // If player not online on this server, get from database
                    if (onlinePlayer == null) {
                        try {
                            long storedExp = dataHandler.get(HypixelDataHandler.Data.HYPIXEL_EXPERIENCE, DatapointHypixelExperience.class).getValue();
                            networkExp = storedExp;
                            networkLevel = net.swofty.type.generic.experience.HypixelExperience.xpToLevel(storedExp);
                        } catch (Exception e) {
                            // Default to 1 if error
                            networkLevel = 1;
                            networkExp = 0;
                        }
                    }

                    // Get server information - handle both online and offline players
                    String serverDisplay;
                    if (finalIsOnline && finalCurrentServer != null) {
                        // Player is online, show their current server
                        serverDisplay = finalCurrentServer.name();
                    } else {
                        // Player is offline
                        serverDisplay = "§cOffline";
                    }

                    // Get first login timestamp from database
                    long firstLoginTime = dataHandler.get(HypixelDataHandler.Data.FIRST_LOGIN, DatapointLong.class).getValue();
                    String firstLoginDisplay;
                    if (firstLoginTime > 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        firstLoginDisplay = dateFormat.format(new Date(firstLoginTime));
                    } else {
                        firstLoginDisplay = "Unknown";
                    }

                    // Get last login from database and always show timestamp
                    long lastLoginTime = dataHandler.get(HypixelDataHandler.Data.LAST_LOGIN, DatapointLong.class).getValue();
                    String lastLoginDisplay;
                    if (lastLoginTime > 0) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        lastLoginDisplay = dateFormat.format(new Date(lastLoginTime));
                    } else {
                        lastLoginDisplay = "Unknown";
                    }


                    // Build and send the user info display (matching Hypixel staff mod format)
                    sender.sendMessage("§6--- Info about " + HypixelPlayer.getColouredDisplayName(finalTargetUUID) + "§6 ---");
                    sender.sendMessage("§6Most Recent Name: §f" + HypixelPlayer.getRawName(finalTargetUUID));
                    sender.sendMessage("§6UUID: §f" + finalTargetUUID);
                    sender.sendMessage("§6Rank: §f" + rank.name());
                    sender.sendMessage("§6PackageRank: §fNone");
                    sender.sendMessage("§6OldPackageRank: §fNone");
                    sender.sendMessage("§6Network Level: §f" + networkLevel);
                    sender.sendMessage("§6Network EXP: §f" + networkExp);
                    sender.sendMessage("§6Guild: §fNone"); // TODO: Implement guild system
                    sender.sendMessage("§6Current Server: §f" + serverDisplay);
                    sender.sendMessage("§6First Login: §f" + firstLoginDisplay);
                    sender.sendMessage("§6Last Login: §f" + lastLoginDisplay);
                    sender.sendMessage("§6Packages: §fNone"); // TODO: Implement package system
                    sender.sendMessage("§6Boosters: §fNone"); // TODO: Implement booster system
                    sender.sendMessage("§6Punishments: §a§lBans §f0 §6- §a§lMutes §f0 §6- §a§lKicks §f0");
                    sender.sendMessage("§6Actions: §b[TPTO] [BAN] [TEMPBAN] [KICK] [MUTE]"); // Staff action buttons

                } catch (Exception e) {
                    sender.sendMessage("§cAn error occurred while fetching player information.");
                    e.printStackTrace();
                }
            });
    }
}

