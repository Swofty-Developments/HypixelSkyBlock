package net.swofty.type.generic.redis.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.swofty.commons.guild.GuildEvent;
import net.swofty.commons.guild.events.response.*;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedisPropagateGuildEvent implements ServiceToClient {

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.PROPAGATE_GUILD_EVENT;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        try {
            String eventType = message.getString("eventType");
            String eventData = message.getString("eventData");
            JSONArray participantsArray = message.getJSONArray("participants");

            List<UUID> participants = participantsArray.toList().stream()
                    .map(obj -> UUID.fromString(obj.toString()))
                    .toList();

            GuildEvent event = parseEvent(eventType, eventData);
            if (event == null) {
                return createFailureResponse("Failed to parse event of type: " + eventType);
            }

            List<UUID> playersHandled = handleEventForPlayers(event, participants);
            return createSuccessResponse(playersHandled.size(), playersHandled);
        } catch (Exception e) {
            Logger.error("Failed to handle guild event: " + e.getMessage());
            return createFailureResponse("Exception occurred: " + e.getMessage());
        }
    }

    private GuildEvent parseEvent(String eventType, String eventData) {
        try {
            GuildEvent templateEvent = GuildEvent.findFromType(eventType);
            return (GuildEvent) templateEvent.getSerializer().deserialize(eventData);
        } catch (Exception e) {
            Logger.error(e, "Failed to parse guild event of type: {}", eventType);
            return null;
        }
    }

    private List<UUID> handleEventForPlayers(GuildEvent event, List<UUID> participants) {
        List<UUID> playersHandled = new ArrayList<>();

        for (UUID participantUUID : participants) {
            HypixelPlayer player = HypixelGenericLoader.getLoadedPlayers().stream()
                    .filter(p -> p.getUuid().equals(participantUUID))
                    .findFirst()
                    .orElse(null);

            if (player != null) {
                try {
                    handleEventForPlayer(player, event);
                    playersHandled.add(participantUUID);
                } catch (Exception e) {
                    Logger.error("Failed to handle guild event for player " + participantUUID + ": " + e.getMessage());
                }
            }
        }

        return playersHandled;
    }

    private void handleEventForPlayer(HypixelPlayer player, GuildEvent event) {
        switch (event) {
            case GuildCreatedResponseEvent e -> handleCreated(player, e);
            case GuildInviteSentResponseEvent e -> handleInviteSent(player, e);
            case GuildMemberJoinedResponseEvent e -> handleMemberJoined(player, e);
            case GuildMemberLeftResponseEvent e -> handleMemberLeft(player, e);
            case GuildMemberKickedResponseEvent e -> handleMemberKicked(player, e);
            case GuildDisbandedResponseEvent e -> handleDisbanded(player, e);
            case GuildRankChangedResponseEvent e -> handleRankChanged(player, e);
            case GuildTransferredResponseEvent e -> handleTransferred(player, e);
            case GuildChatResponseEvent e -> handleChat(player, e);
            case GuildSettingChangedResponseEvent e -> handleSettingChanged(player, e);
            case GuildMuteChangedResponseEvent e -> handleMuteChanged(player, e);
            case GuildInviteExpiredResponseEvent e -> handleInviteExpired(player, e);
            default -> Logger.warn("Unhandled guild event type: " + event.getClass().getSimpleName());
        }
    }

    private void handleCreated(HypixelPlayer player, GuildCreatedResponseEvent event) {
        sendMessage(player, "§aYou created the guild §6" + event.getGuild().getName() + "§a!");
    }

    private void handleInviteSent(HypixelPlayer player, GuildInviteSentResponseEvent event) {
        if (event.getInvitee().equals(player.getUuid())) {
            sendMessage(player, HypixelPlayer.getDisplayName(event.getInviter()) + " §ehas invited you to join their guild §6" + event.getGuild().getName() + "§e!");
            TextComponent component = LegacyComponentSerializer.legacySection().deserialize("§eYou have §c60 §eseconds to accept. §6Click here to join!");
            component = component.hoverEvent(Component.text("§eClick to accept!"));
            component = component.clickEvent(ClickEvent.runCommand("/guild accept " + HypixelPlayer.getRawName(event.getInviter())));
            player.sendMessage(component);
        } else if (event.getInviter().equals(player.getUuid())) {
            sendMessage(player, "§eYou invited " + HypixelPlayer.getDisplayName(event.getInvitee()) + " §eto your guild. They have §c60 §eseconds to accept.");
        }
    }

    private void handleMemberJoined(HypixelPlayer player, GuildMemberJoinedResponseEvent event) {
        if (event.getJoiner().equals(player.getUuid())) {
            sendMessage(player, "§aYou joined the guild §6" + event.getGuild().getName() + "§a!");
        } else {
            sendMessage(player, HypixelPlayer.getDisplayName(event.getJoiner()) + " §ejoined the guild!");
        }
    }

    private void handleMemberLeft(HypixelPlayer player, GuildMemberLeftResponseEvent event) {
        if (event.getLeaver().equals(player.getUuid())) {
            sendMessage(player, "§eYou left the guild.");
        } else {
            sendMessage(player, HypixelPlayer.getDisplayName(event.getLeaver()) + " §eleft the guild.");
        }
    }

    private void handleMemberKicked(HypixelPlayer player, GuildMemberKickedResponseEvent event) {
        if (event.getKicked().equals(player.getUuid())) {
            String reason = event.getReason() != null && !event.getReason().isEmpty()
                    ? " §7Reason: §f" + event.getReason() : "";
            sendMessage(player, "§cYou have been kicked from the guild!" + reason);
        } else {
            sendMessage(player, HypixelPlayer.getDisplayName(event.getKicker()) + " §ekicked " + HypixelPlayer.getDisplayName(event.getKicked()) + " §efrom the guild!");
        }
    }

    private void handleDisbanded(HypixelPlayer player, GuildDisbandedResponseEvent event) {
        sendMessage(player, HypixelPlayer.getDisplayName(event.getDisbander()) + " §edisbanded the guild!");
    }

    private void handleRankChanged(HypixelPlayer player, GuildRankChangedResponseEvent event) {
        if (event.getTarget().equals(player.getUuid())) {
            boolean promoted = isPrioritized(event.getFromRank(), event.getToRank());
            String action = promoted ? "§apromoted" : "§cdemoted";
            sendMessage(player, "§eYou were " + action + " §eto §6" + event.getToRank() + "§e!");
        } else {
            sendMessage(player, HypixelPlayer.getDisplayName(event.getChanger()) + " §echanged " + HypixelPlayer.getDisplayName(event.getTarget()) + "'s §erank from §6" + event.getFromRank() + " §eto §6" + event.getToRank() + "§e.");
        }
    }

    private void handleTransferred(HypixelPlayer player, GuildTransferredResponseEvent event) {
        if (event.getNewOwner().equals(player.getUuid())) {
            sendMessage(player, "§aYou are now the Guild Master!");
        } else {
            sendMessage(player, HypixelPlayer.getDisplayName(event.getOldOwner()) + " §etransferred guild ownership to " + HypixelPlayer.getDisplayName(event.getNewOwner()) + "§e!");
        }
    }

    private void handleChat(HypixelPlayer player, GuildChatResponseEvent event) {
        String prefix = event.isOfficerChat() ? "§3Officer" : "§2Guild";
        String senderName = HypixelPlayer.getDisplayName(event.getSender());
        player.sendMessage(prefix + " > " + senderName + "§f: " + event.getMessage());
    }

    private void handleSettingChanged(HypixelPlayer player, GuildSettingChangedResponseEvent event) {
        String changerName = HypixelPlayer.getDisplayName(event.getChanger());
        String settingDisplay = switch (event.getSetting().toLowerCase()) {
            case "tag" -> "guild tag to §6" + event.getValue();
            case "tagcolor" -> "tag color to §6" + event.getValue();
            case "motd" -> "the MOTD";
            case "description" -> "the description";
            case "discord" -> "the Discord link";
            case "rename" -> "the guild name to §6" + event.getValue();
            case "slow" -> "slow chat to §6" + event.getValue();
            case "finder" -> "guild finder to §6" + event.getValue();
            default -> event.getSetting() + " to " + event.getValue();
        };
        sendMessage(player, changerName + " §eupdated " + settingDisplay + "§e.");
    }

    private void handleMuteChanged(HypixelPlayer player, GuildMuteChangedResponseEvent event) {
        String muterName = HypixelPlayer.getDisplayName(event.getMuter());
        if (event.isUnmute()) {
            if (event.getTarget().equalsIgnoreCase("everyone")) {
                sendMessage(player, muterName + " §eunmuted the guild chat.");
            } else {
                sendMessage(player, muterName + " §eunmuted " + HypixelPlayer.getDisplayName(UUID.fromString(event.getTarget())) + "§e.");
            }
        } else {
            long minutes = event.getDuration() / 60000;
            if (event.getTarget().equalsIgnoreCase("everyone")) {
                sendMessage(player, muterName + " §emuted the guild chat for §c" + minutes + " minutes§e.");
            } else {
                sendMessage(player, muterName + " §emuted " + HypixelPlayer.getDisplayName(UUID.fromString(event.getTarget())) + " §efor §c" + minutes + " minutes§e.");
            }
        }
    }

    private void handleInviteExpired(HypixelPlayer player, GuildInviteExpiredResponseEvent event) {
        if (event.getInvitee().equals(player.getUuid())) {
            sendMessage(player, "§eThe guild invite from " + HypixelPlayer.getDisplayName(event.getInviter()) + " §ehas expired.");
        } else if (event.getInviter().equals(player.getUuid())) {
            sendMessage(player, "§eThe guild invite to " + HypixelPlayer.getDisplayName(event.getInvitee()) + " §ehas expired.");
        }
    }

    private boolean isPrioritized(String fromRank, String toRank) {
        return switch (toRank) {
            case "Guild Master" -> true;
            case "Officer" -> fromRank.equals("Member");
            default -> false;
        };
    }

    private void sendMessage(HypixelPlayer player, String message) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage(message);
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private JSONObject createSuccessResponse(int playersHandled, List<UUID> playersHandledUuids) {
        JSONObject response = new JSONObject();
        response.put("success", true);
        response.put("playersHandled", playersHandled);
        JSONArray participantsArray = new JSONArray();
        for (UUID uuid : playersHandledUuids) {
            participantsArray.put(uuid.toString());
        }
        response.put("playersHandledUUIDs", participantsArray);
        return response;
    }

    private JSONObject createFailureResponse(String reason) {
        JSONObject response = new JSONObject();
        response.put("success", false);
        response.put("error", reason);
        return response;
    }
}
