package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.party.FullParty;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandParameters(aliases = "p",
        description = "Party management commands",
        usage = "/party <subcommand>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class PartyCommand extends HypixelCommand {
    public List<UUID> pendingCommands = new ArrayList<>();

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString subcommand = ArgumentType.String("subcommand");
        ArgumentString playerName = ArgumentType.String("player");
        ProxyService partyService = new ProxyService(ServiceType.PARTY);

        command.addSyntax((sender, context) -> {
            showHelp((HypixelPlayer) sender);
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;

            if (pendingCommands.contains(player.getUuid())) {
                return;
            }

            pendingCommands.add(player.getUuid());

            if (!partyService.isOnline().join()) {
                sender.sendMessage("§cCould not connect to the party service! Please try again later.");
                pendingCommands.remove(player.getUuid());
                return;
            }

            String sub = context.get(subcommand);

            switch (sub.toLowerCase()) {
                case "list" -> {
                    if (!PartyManager.isInParty(player)) {
                        player.sendMessage("§9§m-----------------------------------------------------");
                        player.sendMessage("§cYou are not in a party!");
                        player.sendMessage("§9§m-----------------------------------------------------");
                        return;
                    }
                    FullParty party = PartyManager.getPartyFromPlayer(player);

                    int partySize = party.getMembers().size();
                    player.sendMessage("§9§m-----------------------------------------------------");
                    player.sendMessage("§6Party Members (" + partySize + ")");
                    player.sendMessage("§f ");

                    FullParty.Member leader = party.getLeader();
                    player.sendMessage("§eParty Leader: " + HypixelPlayer.getDisplayName(leader.getUuid()));

                    boolean hasMods = false;
                    for (FullParty.Member member : party.getMembers()) {
                        if (member.getRole() == FullParty.Role.MODERATOR) {
                            hasMods = true;
                            break;
                        }
                    }

                    if (hasMods) {
                        player.sendMessage("§f ");
                        String modList = party.getMembers().stream()
                                .filter(member -> member.getRole() == FullParty.Role.MODERATOR)
                                .map(member -> HypixelPlayer.getDisplayName(member.getUuid()))
                                .collect(Collectors.joining(", "));
                        player.sendMessage("§eParty Moderators: " + modList);
                    }

                    boolean hasMembers = false;
                    for (FullParty.Member member : party.getMembers()) {
                        if (member.getRole() != FullParty.Role.LEADER && member.getRole() != FullParty.Role.MODERATOR) {
                            hasMembers = true;
                            break;
                        }
                    }

                    String memberList = party.getMembers().stream()
                            .filter(member -> member.getRole() != FullParty.Role.LEADER && member.getRole() != FullParty.Role.MODERATOR)
                            .map(member -> HypixelPlayer.getDisplayName(member.getUuid()))
                            .collect(Collectors.joining(", "));
                    if (hasMembers) {
                        player.sendMessage("§f ");
                        player.sendMessage("§eParty Members: " + memberList);
                    }
                    player.sendMessage("§9§m-----------------------------------------------------");
                }
                case "leave" -> PartyManager.leaveParty(player);
                case "disband" -> PartyManager.disbandParty(player);
                case "warp" -> PartyManager.warpParty(player);
                default -> PartyManager.invitePlayer(player, sub);
            }

            pendingCommands.remove(player.getUuid());
        }, subcommand);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;

            if (pendingCommands.contains(player.getUuid())) {
                return;
            }

            pendingCommands.add(player.getUuid());

            if (!partyService.isOnline().join()) {
                sender.sendMessage("§cCould not connect to the party service! Please try again later.");
                pendingCommands.remove(player.getUuid());
                return;
            }

            String sub = context.get(subcommand);
            String target = context.get(playerName);

            switch (sub.toLowerCase()) {
                case "invite" -> PartyManager.invitePlayer(player, target);
                case "accept" -> PartyManager.acceptInvite(player, target);
                case "kick" -> PartyManager.kickPlayer(player, target);
                case "transfer" -> PartyManager.transferLeadership(player, target);
                case "promote" -> PartyManager.promotePlayer(player, target);
                case "demote" -> PartyManager.demotePlayer(player, target);
                case "hijack" -> PartyManager.hijackParty(player, target);
                case "chat" -> PartyManager.sendChat(player, target);
                case "movetoserver" -> {
                    UUID targetServer = UUID.fromString(target);
                    player.asProxyPlayer().transferToWithIndication(targetServer);
                }
                default -> player.sendMessage("§cUnknown command. Use /party for help.");
            }

            pendingCommands.remove(player.getUuid());
        }, subcommand, playerName);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            HypixelPlayer player = (HypixelPlayer) sender;

            if (pendingCommands.contains(player.getUuid())) {
                return;
            }

            pendingCommands.add(player.getUuid());

            if (!partyService.isOnline().join()) {
                sender.sendMessage("§cCouldn't find a party service! Please try again later.");
                pendingCommands.remove(player.getUuid());
                return;
            }

            String sub = context.get(subcommand);
            String message = Arrays.toString(context.get(new ArgumentStringArray("message")));

            switch (sub.toLowerCase()) {
                case "chat" -> PartyManager.sendChat(player, message);
                default -> player.sendMessage("§cUnknown command. Use /party for help.");
            }

            pendingCommands.remove(player.getUuid());
        }, subcommand, new ArgumentStringArray("message"));
    }

    private void showHelp(HypixelPlayer player) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§6Party Commands");
        player.sendMessage("§e/p accept §8- §7§oAccept a party invite from a player");
        player.sendMessage("§e/p invite <player> §8- §7§oInvite another player to your party");
        player.sendMessage("§e/p list §8- §7§oLists the players in your current party");
        player.sendMessage("§e/p leave §8- §7§oLeaves your current party");
        player.sendMessage("§e/p warp §8- §7§oWarps the members of a party to your current server");
        player.sendMessage("§e/p disband §8- §7§oDisbands the party");
        player.sendMessage("§e/p transfer <player> §8- §7§oTransfers the party to another player");
        player.sendMessage("§e/p kick <player> §8- §7§oRemove a player from your party");
        player.sendMessage("§e/p promote <player> §8- §7§oPromote a player to moderator");
        player.sendMessage("§e/p demote <player> §8- §7§oDemote a player from moderator");
        player.sendMessage("§e/p chat §8- §7§oSends a chat message to the entire party");
        if (player.getRank().isEqualOrHigherThan(Rank.STAFF)) {
            player.sendMessage("§e/p hijack <player> §8- §7§oHijacks a party (Admin only)");
        }
        player.sendMessage("§9§m-----------------------------------------------------");
    }
}