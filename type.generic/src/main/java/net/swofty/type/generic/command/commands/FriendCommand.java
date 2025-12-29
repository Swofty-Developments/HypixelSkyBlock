package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.friend.FriendManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandParameters(aliases = "f",
        description = "Friend management commands",
        usage = "/friend <subcommand>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class FriendCommand extends HypixelCommand {
    public List<UUID> pendingCommands = new ArrayList<>();

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString subcommand = ArgumentType.String("subcommand");
        ArgumentString playerName = ArgumentType.String("player");
        ArgumentString extraArg = ArgumentType.String("extra");
        ProxyService friendService = new ProxyService(ServiceType.FRIEND);

        // No args - show help
        command.addSyntax((sender, context) -> {
            showHelp((HypixelPlayer) sender);
        });

        // Single arg (subcommand only)
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;

            if (pendingCommands.contains(player.getUuid())) {
                return;
            }

            pendingCommands.add(player.getUuid());

            if (!friendService.isOnline().join()) {
                sender.sendMessage("§cCould not connect to the friend service! Please try again later.");
                pendingCommands.remove(player.getUuid());
                return;
            }

            String sub = context.get(subcommand);

            switch (sub.toLowerCase()) {
                case "help" -> showHelp(player);
                case "list" -> FriendManager.listFriends(player, 1, false);
                case "notifications" -> FriendManager.toggleNotifications(player);
                case "toggle" -> FriendManager.toggleRequests(player);
                case "removeall" -> FriendManager.removeAllFriends(player);
                case "requests" -> FriendManager.listRequests(player, 1);
                default -> FriendManager.addFriend(player, sub);
            }

            pendingCommands.remove(player.getUuid());
        }, subcommand);

        // Two args (subcommand + player/page)
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;

            if (pendingCommands.contains(player.getUuid())) {
                return;
            }

            pendingCommands.add(player.getUuid());

            if (!friendService.isOnline().join()) {
                sender.sendMessage("§cCould not connect to the friend service! Please try again later.");
                pendingCommands.remove(player.getUuid());
                return;
            }

            String sub = context.get(subcommand);
            String arg = context.get(playerName);

            switch (sub.toLowerCase()) {
                case "accept" -> FriendManager.acceptRequest(player, arg);
                case "add" -> FriendManager.addFriend(player, arg);
                case "best" -> FriendManager.toggleBestFriend(player, arg);
                case "deny" -> FriendManager.denyRequest(player, arg);
                case "remove" -> FriendManager.removeFriend(player, arg);
                case "list" -> {
                    if (arg.equalsIgnoreCase("best")) {
                        FriendManager.listFriends(player, 1, true);
                    } else {
                        int page = parsePageNumber(arg);
                        FriendManager.listFriends(player, page, false);
                    }
                }
                case "requests" -> {
                    int page = parsePageNumber(arg);
                    FriendManager.listRequests(player, page);
                }
                default -> player.sendMessage("§cUnknown command. Use /friend for help.");
            }

            pendingCommands.remove(player.getUuid());
        }, subcommand, playerName);

        // Three args (nickname command)
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;

            if (pendingCommands.contains(player.getUuid())) {
                return;
            }

            pendingCommands.add(player.getUuid());

            if (!friendService.isOnline().join()) {
                sender.sendMessage("§cCould not connect to the friend service! Please try again later.");
                pendingCommands.remove(player.getUuid());
                return;
            }

            String sub = context.get(subcommand);
            String target = context.get(playerName);
            String extra = context.get(extraArg);

            if (sub.equalsIgnoreCase("nickname")) {
                FriendManager.setNickname(player, target, extra);
            } else {
                player.sendMessage("§cUnknown command. Use /friend for help.");
            }

            pendingCommands.remove(player.getUuid());
        }, subcommand, playerName, extraArg);
    }

    private void showHelp(HypixelPlayer player) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§6Friend Commands");
        player.sendMessage("§e/f add <player> §8- §7Add a player as a friend");
        player.sendMessage("§e/f accept <player> §8- §7Accept a friend request");
        player.sendMessage("§e/f deny <player> §8- §7Deny a friend request");
        player.sendMessage("§e/f remove <player> §8- §7Remove a friend");
        player.sendMessage("§e/f removeall §8- §7Remove all friends (keeps best friends)");
        player.sendMessage("§e/f best <player> §8- §7Toggle best friend status");
        player.sendMessage("§e/f nickname <player> <name> §8- §7Set a nickname for a friend");
        player.sendMessage("§e/f list [page/best] §8- §7List your friends");
        player.sendMessage("§e/f requests [page] §8- §7View pending friend requests");
        player.sendMessage("§e/f toggle §8- §7Toggle accepting friend requests");
        player.sendMessage("§e/f notifications §8- §7Toggle join/leave notifications");
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private int parsePageNumber(String arg) {
        try {
            return Math.max(1, Integer.parseInt(arg));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
