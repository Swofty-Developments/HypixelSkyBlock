package net.swofty.type.generic.redis.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.swofty.commons.StringUtility;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.friend.FriendSettingType;
import net.swofty.commons.friend.events.response.*;
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

public class RedisPropagateFriendEvent implements ServiceToClient {

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.PROPAGATE_FRIEND_EVENT;
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

            FriendEvent event = parseEvent(eventType, eventData);
            if (event == null) {
                Logger.error("Failed to parse friend event of type: " + eventType);
                return createFailureResponse("Failed to parse event of type: " + eventType);
            }

            List<UUID> playersHandled = handleEventForPlayers(event, participants);
            return createSuccessResponse(playersHandled.size(), playersHandled);
        } catch (Exception e) {
            Logger.error("Failed to handle friend event: " + e.getMessage());
            return createFailureResponse("Exception occurred: " + e.getMessage());
        }
    }

    private FriendEvent parseEvent(String eventType, String eventData) {
        try {
            FriendEvent templateEvent = FriendEvent.findFromType(eventType);
            return (FriendEvent) templateEvent.getSerializer().deserialize(eventData);
        } catch (Exception e) {
            Logger.error(e, "Failed to parse friend event of type: {}", eventType);
            return null;
        }
    }

    private List<UUID> handleEventForPlayers(FriendEvent event, List<UUID> participants) {
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
                    Logger.error("Failed to handle friend event for player " + participantUUID + ": " + e.getMessage());
                }
            }
        }

        return playersHandled;
    }

    private void handleEventForPlayer(HypixelPlayer player, FriendEvent event) {
        switch (event) {
            case FriendRequestSentResponseEvent e -> handleRequestSent(player, e);
            case FriendRequestReceivedResponseEvent e -> handleRequestReceived(player, e);
            case FriendAddedResponseEvent e -> handleFriendAdded(player, e);
            case FriendDeniedResponseEvent e -> handleRequestDenied(player, e);
            case FriendRemovedResponseEvent e -> handleFriendRemoved(player, e);
            case FriendRemoveAllResponseEvent e -> handleRemoveAll(player, e);
            case FriendBestToggledResponseEvent e -> handleBestToggled(player, e);
            case FriendNicknameSetResponseEvent e -> handleNicknameSet(player, e);
            case FriendSettingToggledResponseEvent e -> handleSettingToggled(player, e);
            case FriendJoinNotificationEvent e -> handleJoinNotification(player, e);
            case FriendLeaveNotificationEvent e -> handleLeaveNotification(player, e);
            case FriendRequestExpiredResponseEvent e -> handleRequestExpired(player, e);
            case FriendListResponseEvent e -> handleFriendList(player, e);
            case FriendRequestsListResponseEvent e -> handleRequestsList(player, e);
            default -> Logger.warn("Unhandled friend event type: " + event.getClass().getSimpleName());
        }
    }

    private void handleRequestSent(HypixelPlayer player, FriendRequestSentResponseEvent event) {
        sendMessage(player, "§aFriend request sent to §e" + event.getTargetName() + "§a!");
    }

    private void handleRequestReceived(HypixelPlayer player, FriendRequestReceivedResponseEvent event) {
        String senderName = event.getSenderName();

        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§aFriend request from §e" + senderName);

        TextComponent component = LegacyComponentSerializer.legacySection().deserialize("§eClick §6§lACCEPT §eor §c§lDENY");

        TextComponent acceptButton = LegacyComponentSerializer.legacySection().deserialize(" §a§l[ACCEPT]");
        acceptButton = acceptButton.hoverEvent(Component.text("§eClick to accept friend request"));
        acceptButton = acceptButton.clickEvent(ClickEvent.runCommand("/f accept " + senderName));

        TextComponent denyButton = LegacyComponentSerializer.legacySection().deserialize(" §c§l[DENY]");
        denyButton = denyButton.hoverEvent(Component.text("§eClick to deny friend request"));
        denyButton = denyButton.clickEvent(ClickEvent.runCommand("/f deny " + senderName));

        player.sendMessage(component.append(acceptButton).append(denyButton));
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private void handleFriendAdded(HypixelPlayer player, FriendAddedResponseEvent event) {
        String otherName = player.getUuid().equals(event.getPlayer1())
                ? event.getPlayer2Name()
                : event.getPlayer1Name();
        sendMessage(player, "§aYou are now friends with §e" + otherName + "§a!");
    }

    private void handleRequestDenied(HypixelPlayer player, FriendDeniedResponseEvent event) {
        sendMessage(player, "§e" + event.getDenierName() + " §cdenied your friend request.");
    }

    private void handleFriendRemoved(HypixelPlayer player, FriendRemovedResponseEvent event) {
        if (player.getUuid().equals(event.getRemover())) {
            sendMessage(player, "§cRemoved §e" + HypixelPlayer.getDisplayName(event.getRemoved()) + " §cfrom your friends list.");
        } else {
            sendMessage(player, "§e" + event.getRemoverName() + " §cremoved you from their friends list.");
        }
    }

    private void handleRemoveAll(HypixelPlayer player, FriendRemoveAllResponseEvent event) {
        if (event.getRemovedCount() == 0) {
            sendMessage(player, "§cYou have no friends to remove! (Best friends are kept)");
        } else {
            sendMessage(player, "§cRemoved §e" + event.getRemovedCount() + " §cfriends from your list. Best friends were kept.");
        }
    }

    private void handleBestToggled(HypixelPlayer player, FriendBestToggledResponseEvent event) {
        if (event.isBest()) {
            sendMessage(player, "§e" + event.getTargetName() + " §ais now your best friend!");
        } else {
            sendMessage(player, "§e" + event.getTargetName() + " §cis no longer your best friend.");
        }
    }

    private void handleNicknameSet(HypixelPlayer player, FriendNicknameSetResponseEvent event) {
        if (event.getNickname() == null || event.getNickname().isEmpty()) {
            sendMessage(player, "§aCleared nickname for §e" + event.getTargetName() + "§a.");
        } else {
            sendMessage(player, "§aSet nickname for §e" + event.getTargetName() + " §ato §e" + event.getNickname() + "§a.");
        }
    }

    private void handleSettingToggled(HypixelPlayer player, FriendSettingToggledResponseEvent event) {
        String settingName = event.getSettingType() == FriendSettingType.ACCEPTING_REQUESTS
                ? "Friend requests"
                : "Friend join/leave notifications";
        String state = event.isNewValue() ? "§aenabled" : "§cdisabled";
        sendMessage(player, "§e" + settingName + " §7are now " + state + "§7.");
    }

    private void handleJoinNotification(HypixelPlayer player, FriendJoinNotificationEvent event) {
        String displayName = HypixelPlayer.getDisplayName(event.getFriend());
        player.sendMessage("§aFriend > " + displayName + " §7joined.");
    }

    private void handleLeaveNotification(HypixelPlayer player, FriendLeaveNotificationEvent event) {
        String displayName = HypixelPlayer.getDisplayName(event.getFriend());
        player.sendMessage("§aFriend > " + displayName + " §7left.");
    }

    private void handleRequestExpired(HypixelPlayer player, FriendRequestExpiredResponseEvent event) {
        if (player.getUuid().equals(event.getSender())) {
            // Message to sender
            String targetDisplay = HypixelPlayer.getDisplayName(event.getTarget());
            sendMessage(player, "§eYour friend request to " + targetDisplay + " §ehas expired.");
        } else {
            // Message to recipient
            String senderDisplay = HypixelPlayer.getDisplayName(event.getSender());
            sendMessage(player, "§eThe friend request from " + senderDisplay + " §ehas expired.");
        }
    }

    private void handleFriendList(HypixelPlayer player, FriendListResponseEvent event) {
        String title = event.isBestOnly() ? "Best Friends" : "Friends";

        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§6" + title + " §7(Page " + event.getPage() + "/" + event.getTotalPages() + ")");

        if (event.getFriends().isEmpty()) {
            player.sendMessage("§7You have no " + (event.isBestOnly() ? "best " : "") + "friends to display.");
        } else {
            for (FriendListResponseEvent.FriendListEntry friend : event.getFriends()) {
                StringBuilder sb = new StringBuilder();

                // Online status indicator
                if (friend.isOnline()) {
                    sb.append("§a● ");
                } else {
                    sb.append("§c● ");
                }

                // Best friend marker
                if (friend.isBest()) {
                    sb.append("§6✦ ");
                }

                // Name (rank-colored, matches tablist). Fallback to plain name if unavailable or failure.
                String displayName;
                try {
                    displayName = HypixelPlayer.getDisplayName(friend.getUuid());
                } catch (Exception e) {
                    displayName = "§e" + friend.getName();
                }
                if (displayName == null || displayName.isEmpty()) {
                    displayName = "§e" + friend.getName();
                }

                // Append nickname if set
                if (friend.getNickname() != null && !friend.getNickname().isEmpty()) {
                    sb.append(displayName).append(" §7(").append(friend.getNickname()).append(")");
                } else {
                    sb.append(displayName);
                }

                // Server info (when online)
                if (friend.isOnline() && friend.getServer() != null && !friend.getServer().isEmpty()) {
                    sb.append(" §7- §e").append(StringUtility.toNormalCase(friend.getServer()));
                }

                TextComponent line = LegacyComponentSerializer.legacySection().deserialize(sb.toString());

                String friendsSinceText;
                if (friend.getFriendSince() > 0) {
                    long secondsSince = Math.max(0, (System.currentTimeMillis() - friend.getFriendSince()) / 1000);
                    friendsSinceText = "§7Friends for " + formatDuration(secondsSince);
                } else {
                    friendsSinceText = "§7Friends since: Unknown";
                }

                TextComponent hovered;
                if (friend.isOnline()) {
                    hovered = line.hoverEvent(Component.text(friendsSinceText));
                } else {
                    String lastSeenText;
                    if (friend.getLastSeen() > 0) {
                        long secondsAgo = Math.max(0, (System.currentTimeMillis() - friend.getLastSeen()) / 1000);
                        lastSeenText = "§7Last seen " + formatDuration(secondsAgo) + " ago";
                    } else {
                        lastSeenText = "§7Last seen: Unknown";
                    }
                    hovered = line.hoverEvent(Component.text(lastSeenText + "\n" + friendsSinceText));
                }
                player.sendMessage(hovered);
            }
        }

        if (event.getTotalPages() > 1) {
            TextComponent navMessage = LegacyComponentSerializer.legacySection().deserialize("§7Use §e/f list " + (event.isBestOnly() ? "best " : "") + "<page> §7to navigate.");
            player.sendMessage(navMessage);
        }

        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private void handleRequestsList(HypixelPlayer player, FriendRequestsListResponseEvent event) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage("§6Pending Friend Requests §7(Page " + event.getPage() + "/" + event.getTotalPages() + ")");

        if (event.getRequests().isEmpty()) {
            player.sendMessage("§7You have no pending friend requests.");
        } else {
            for (FriendRequestsListResponseEvent.FriendRequestEntry request : event.getRequests()) {
                TextComponent line = LegacyComponentSerializer.legacySection().deserialize("§e" + request.getSenderName() + " ");

                TextComponent acceptButton = LegacyComponentSerializer.legacySection().deserialize("§a[ACCEPT]");
                acceptButton = acceptButton.hoverEvent(Component.text("§eClick to accept"));
                acceptButton = acceptButton.clickEvent(ClickEvent.runCommand("/f accept " + request.getSenderName()));

                TextComponent denyButton = LegacyComponentSerializer.legacySection().deserialize(" §c[DENY]");
                denyButton = denyButton.hoverEvent(Component.text("§eClick to deny"));
                denyButton = denyButton.clickEvent(ClickEvent.runCommand("/f deny " + request.getSenderName()));

                player.sendMessage(line.append(acceptButton).append(denyButton));
            }
        }

        if (event.getTotalPages() > 1) {
            player.sendMessage("§7Use §e/f requests <page> §7to navigate.");
        }

        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private void sendMessage(HypixelPlayer player, String message) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage(message);
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private String formatDuration(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        if (days > 0) {
            return days + "d " + hours + "h";
        }
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        if (minutes > 0) {
            return minutes + "m";
        }
        return seconds + "s";
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
