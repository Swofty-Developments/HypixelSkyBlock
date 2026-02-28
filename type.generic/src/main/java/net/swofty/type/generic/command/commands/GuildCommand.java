package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.guild.GuildData;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.guild.GuildManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandParameters(aliases = "g",
        description = "Guild management commands",
        usage = "/guild <subcommand>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class GuildCommand extends HypixelCommand {
    private final List<UUID> pendingCommands = new ArrayList<>();

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString subcommand = ArgumentType.String("subcommand");
        ArgumentString argument = ArgumentType.String("argument");
        ProxyService guildService = new ProxyService(ServiceType.GUILD);

        command.addSyntax((sender, context) -> {
            showHelp((HypixelPlayer) sender);
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;
            if (pendingCommands.contains(player.getUuid())) return;
            pendingCommands.add(player.getUuid());

            Thread.startVirtualThread(() -> {
                try {
                    if (!guildService.isOnline().join()) {
                        player.sendMessage("§cGuild service is currently offline!");
                        return;
                    }

                    String sub = context.get(subcommand);

                    switch (sub.toLowerCase()) {
                        case "leave" -> GuildManager.leaveGuild(player);
                        case "disband" -> GuildManager.disbandGuild(player);
                        case "list", "info" -> showGuildInfo(player);
                        case "online" -> showOnlineMembers(player);
                        case "notifications" -> GuildManager.changeSetting(player, "notifications", "toggle");
                        case "mute" -> GuildManager.mutePlayer(player, "everyone", 3600000);
                        case "unmute" -> GuildManager.unmutePlayer(player, "everyone");
                        default -> GuildManager.invitePlayer(player, sub);
                    }
                } finally {
                    pendingCommands.remove(player.getUuid());
                }
            });
        }, subcommand);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;
            if (pendingCommands.contains(player.getUuid())) return;
            pendingCommands.add(player.getUuid());

            Thread.startVirtualThread(() -> {
                try {
                    if (!guildService.isOnline().join()) {
                        player.sendMessage("§cGuild service is currently offline!");
                        return;
                    }

                    String sub = context.get(subcommand);
                    String arg = context.get(argument);

                    switch (sub.toLowerCase()) {
                        case "create" -> GuildManager.createGuild(player, arg);
                        case "invite" -> GuildManager.invitePlayer(player, arg);
                        case "accept" -> GuildManager.acceptInvite(player, arg);
                        case "kick" -> GuildManager.kickPlayer(player, arg, "");
                        case "promote" -> GuildManager.promotePlayer(player, arg);
                        case "demote" -> GuildManager.demotePlayer(player, arg);
                        case "transfer" -> GuildManager.transferOwnership(player, arg);
                        case "tag" -> GuildManager.changeSetting(player, "tag", arg);
                        case "tagcolor" -> GuildManager.changeSetting(player, "tagcolor", arg);
                        case "rename" -> GuildManager.changeSetting(player, "rename", arg);
                        case "slow" -> GuildManager.changeSetting(player, "slow", arg);
                        case "finder" -> GuildManager.changeSetting(player, "finder", arg);
                        case "chat" -> GuildManager.sendChat(player, arg, false);
                        case "officerchat", "oc" -> GuildManager.sendChat(player, arg, true);
                        case "mute" -> GuildManager.mutePlayer(player, arg, 3600000);
                        case "unmute" -> GuildManager.unmutePlayer(player, arg);
                        default -> player.sendMessage("§cUnknown guild command. Use /guild for help.");
                    }
                } finally {
                    pendingCommands.remove(player.getUuid());
                }
            });
        }, subcommand, argument);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;
            if (pendingCommands.contains(player.getUuid())) return;
            pendingCommands.add(player.getUuid());

            Thread.startVirtualThread(() -> {
                try {
                    if (!guildService.isOnline().join()) {
                        player.sendMessage("§cGuild service is currently offline!");
                        return;
                    }

                    String sub = context.get(subcommand);
                    String[] args = context.get(new ArgumentStringArray("args"));
                    String message = String.join(" ", args);

                    switch (sub.toLowerCase()) {
                        case "chat" -> GuildManager.sendChat(player, message, false);
                        case "officerchat", "oc" -> GuildManager.sendChat(player, message, true);
                        case "motd" -> GuildManager.changeSetting(player, "motd", message);
                        case "description" -> GuildManager.changeSetting(player, "description", message);
                        case "discord" -> GuildManager.changeSetting(player, "discord", message);
                        case "kick" -> {
                            if (args.length >= 1) {
                                String targetName = args[0];
                                String reason = args.length > 1 ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "";
                                GuildManager.kickPlayer(player, targetName, reason);
                            }
                        }
                        case "setrank" -> {
                            if (args.length >= 2) {
                                GuildManager.setRank(player, args[0], args[1]);
                            } else {
                                player.sendMessage("§cUsage: /guild setrank <player> <rank>");
                            }
                        }
                        default -> player.sendMessage("§cUnknown guild command. Use /guild for help.");
                    }
                } finally {
                    pendingCommands.remove(player.getUuid());
                }
            });
        }, subcommand, new ArgumentStringArray("args"));
    }

    private void showGuildInfo(HypixelPlayer player) {
        GuildData guild = GuildManager.getGuildFromPlayer(player);
        if (guild == null) {
            player.sendMessage("§9§m-----------------------------------------------------");
            player.sendMessage("§cYou are not in a guild!");
            player.sendMessage("§9§m-----------------------------------------------------");
            return;
        }

        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§6§l" + guild.getName() + (guild.getTag() != null ? " §7[" + guild.getTagColor() + guild.getTag() + "§7]" : ""));
        player.sendMessage("§7Level: §a" + guild.getLevel());
        player.sendMessage("§7Members: §a" + guild.getMembers().size() + "/" + GuildData.MAX_MEMBERS);
        player.sendMessage("§7GEXP: §a" + guild.getTotalGexp());
        if (guild.getMotd() != null && !guild.getMotd().isEmpty()) {
            player.sendMessage("§7MOTD: §f" + guild.getMotd());
        }
        if (guild.getDescription() != null && !guild.getDescription().isEmpty()) {
            player.sendMessage("§7Description: §f" + guild.getDescription());
        }
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private void showOnlineMembers(HypixelPlayer player) {
        GuildData guild = GuildManager.getGuildFromPlayer(player);
        if (guild == null) {
            player.sendMessage("§9§m-----------------------------------------------------");
            player.sendMessage("§cYou are not in a guild!");
            player.sendMessage("§9§m-----------------------------------------------------");
            return;
        }

        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§6Guild Members - §a" + guild.getMembers().size());
        String memberList = guild.getMembers().stream()
                .map(m -> HypixelPlayer.getDisplayName(m.getUuid()))
                .collect(Collectors.joining("§7, "));
        player.sendMessage(memberList);
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private void showHelp(HypixelPlayer player) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§b§lGuild Commands");
        player.sendMessage("§e/guild create <name> §7- Create a guild");
        player.sendMessage("§e/guild invite <player> §7- Invite a player");
        player.sendMessage("§e/guild accept <player> §7- Accept an invite");
        player.sendMessage("§e/guild leave §7- Leave your guild");
        player.sendMessage("§e/guild kick <player> [reason] §7- Kick a member");
        player.sendMessage("§e/guild promote <player> §7- Promote a member");
        player.sendMessage("§e/guild demote <player> §7- Demote a member");
        player.sendMessage("§e/guild transfer <player> §7- Transfer ownership");
        player.sendMessage("§e/guild setrank <player> <rank> §7- Set member rank");
        player.sendMessage("§e/guild disband §7- Disband the guild");
        player.sendMessage("§e/guild chat <message> §7- Send guild chat");
        player.sendMessage("§e/guild officerchat <message> §7- Officer chat");
        player.sendMessage("§e/guild tag <tag> §7- Set guild tag");
        player.sendMessage("§e/guild motd <message> §7- Set MOTD");
        player.sendMessage("§e/guild list §7- View guild info");
        player.sendMessage("§e/guild online §7- View online members");
        player.sendMessage("§e/guild mute [player] §7- Mute chat/player");
        player.sendMessage("§e/guild unmute [player] §7- Unmute chat/player");
        player.sendMessage("§9§m-----------------------------------------------------");
    }
}
