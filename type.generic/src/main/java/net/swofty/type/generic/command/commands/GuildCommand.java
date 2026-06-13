package net.swofty.type.generic.command.commands;

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
        ArgumentStringArray arguments = ArgumentType.StringArray("arguments");
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

                    String[] args = context.get(arguments);
                    String sub = args[0].toLowerCase();
                    String argument = args.length > 1 ? args[1] : null;
                    String message = args.length > 1
                        ? String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length))
                        : "";

                    dispatch(player, sub, argument, message, args);
                } finally {
                    pendingCommands.remove(player.getUuid());
                }
            });
        }, arguments);
    }

    private void dispatch(HypixelPlayer player, String sub, String argument, String message, String[] args) {
        switch (sub) {
            case "leave" -> GuildManager.leaveGuild(player);
            case "disband" -> GuildManager.disbandGuild(player);
            case "list", "info" -> showGuildInfo(player);
            case "online" -> showOnlineMembers(player);
            case "notifications" -> GuildManager.changeSetting(player, "notifications", "toggle");
            case "create" ->
                withArgument(player, argument, "/guild create <name>", value -> GuildManager.createGuild(player, value));
            case "invite" ->
                withArgument(player, argument, "/guild invite <player>", value -> GuildManager.invitePlayer(player, value));
            case "accept" ->
                withArgument(player, argument, "/guild accept <player>", value -> GuildManager.acceptInvite(player, value));
            case "promote" ->
                withArgument(player, argument, "/guild promote <player>", value -> GuildManager.promotePlayer(player, value));
            case "demote" ->
                withArgument(player, argument, "/guild demote <player>", value -> GuildManager.demotePlayer(player, value));
            case "transfer" ->
                withArgument(player, argument, "/guild transfer <player>", value -> GuildManager.transferOwnership(player, value));
            case "tag", "tagcolor", "rename", "slow", "finder" ->
                withArgument(player, argument, "/guild " + sub + " <value>", value -> GuildManager.changeSetting(player, sub, value));
            case "chat" ->
                withArgument(player, argument, "/guild chat <message>", value -> GuildManager.sendChat(player, message, false));
            case "officerchat", "oc" ->
                withArgument(player, argument, "/guild officerchat <message>", value -> GuildManager.sendChat(player, message, true));
            case "motd", "description", "discord" ->
                withArgument(player, argument, "/guild " + sub + " <message>", value -> GuildManager.changeSetting(player, sub, message));
            case "mute" -> GuildManager.mutePlayer(player, argument == null ? "everyone" : argument, 3600000);
            case "unmute" -> GuildManager.unmutePlayer(player, argument == null ? "everyone" : argument);
            case "kick" -> withArgument(player, argument, "/guild kick <player> [reason]", value -> {
                String reason = args.length > 2
                    ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length))
                    : "";
                GuildManager.kickPlayer(player, value, reason);
            });
            case "setrank" -> {
                if (args.length < 3) player.sendMessage("§cUsage: /guild setrank <player> <rank>");
                else GuildManager.setRank(player, args[1], args[2]);
            }
            default -> {
                if (args.length == 1) GuildManager.invitePlayer(player, args[0]);
                else player.sendMessage("§cUnknown guild command. Use /guild for help.");
            }
        }
    }

    private void withArgument(HypixelPlayer player, String argument, String usage, java.util.function.Consumer<String> action) {
        if (argument == null) {
            player.sendMessage("§cUsage: " + usage);
            return;
        }
        action.accept(argument);
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
