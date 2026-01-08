package net.swofty.service.party;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.PendingParty;
import net.swofty.commons.party.events.*;
import net.swofty.commons.party.events.response.*;
import net.swofty.commons.party.events.PartyPlayerDisconnectEvent;
import net.swofty.commons.party.events.PartyPlayerRejoinEvent;
import net.swofty.commons.party.events.response.PartyMemberDisconnectedResponseEvent;
import net.swofty.commons.party.events.response.PartyMemberRejoinedResponseEvent;
import net.swofty.commons.party.events.response.PartyMemberDisconnectTimeoutResponseEvent;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.service.generic.redis.ServiceToServerManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PartyCache {
    private static final Map<UUID, FullParty> activeParties = new ConcurrentHashMap<>();
    private static final Map<UUID, UUID> playerToParty = new ConcurrentHashMap<>();
    private static final Map<UUID, PendingParty> pendingInvites = new ConcurrentHashMap<>();
    private static final List<UUID> partyWarpCooldown = new ArrayList<>();
    private static final Map<UUID, UUID> disconnectTimers = new ConcurrentHashMap<>(); // playerUUID -> timerUUID (for cancellation tracking)
    private static final long DISCONNECT_TIMEOUT_MS = 300000; // 5 minutes

    public static boolean isInParty(UUID playerUUID) {
        return playerToParty.containsKey(playerUUID);
    }

    public static void handleChatMessage(PartyChatMessageEvent event) {
        UUID playerUUID = event.getPlayer();
        String message = event.getMessage();

        FullParty party = getPlayerParty(playerUUID);
        if (party == null) {
            sendErrorToPlayer(playerUUID, "§cYou are not in a party!");
            return;
        }

        PartyChatMessageResponseEvent responseEvent = new PartyChatMessageResponseEvent(party, playerUUID, message);
        sendEvent(responseEvent);
    }

    public static void handleInviteEvent(PartyInviteEvent event) {
        PendingParty pending = (PendingParty) event.getParty();
        pendingInvites.put(pending.getInvitee(), pending);

        scheduleInviteExpiration(pending.getLeader(), pending.getInvitee(), 60000);

        PartyInviteResponseEvent responseEvent = new PartyInviteResponseEvent(pending);
        sendEvent(responseEvent);
    }

    public static void handlePlayerSwitchedServer(PartyPlayerSwitchedServerEvent event) {
        UUID playerUUID = event.getMover();

        FullParty party = getPlayerParty(playerUUID);
        if (party == null) {
            return;
        }

        PartyPlayerSwitchedServerResponseEvent responseEvent = new PartyPlayerSwitchedServerResponseEvent(party, playerUUID);
        sendEvent(responseEvent);
    }

    public static void handleAcceptInvite(PartyAcceptInviteEvent event) {
        UUID playerUUID = event.getAccepter();
        UUID inviterUUID = event.getInviter();

        PendingParty pending = pendingInvites.get(playerUUID);
        if (pending == null || !pending.getLeader().equals(inviterUUID)) {
            sendErrorToPlayer(playerUUID, "§cYou haven't been invited to a party, or the invitation has expired!");
            return;
        }

        FullParty party = getPlayerParty(inviterUUID);

        if (party == null) {
            party = FullParty.create(inviterUUID, playerUUID);
            activeParties.put(party.getUuid(), party);
            playerToParty.put(inviterUUID, party.getUuid());
            playerToParty.put(playerUUID, party.getUuid());

            PartyMemberJoinResponseEvent responseEvent = new PartyMemberJoinResponseEvent(party, inviterUUID, playerUUID);
            sendEvent(responseEvent);
        } else {
            FullParty.Member newMember = new FullParty.Member(playerUUID, FullParty.Role.MEMBER, true);
            party.getMembers().add(newMember);
            playerToParty.put(playerUUID, party.getUuid());

            PartyMemberJoinResponseEvent responseEvent = new PartyMemberJoinResponseEvent(party, inviterUUID, playerUUID);
            sendEvent(responseEvent);
        }

        pendingInvites.remove(playerUUID);
    }

    public static void handleLeaveRequest(PartyLeaveRequestEvent event) {
        UUID playerUUID = event.getLeaver();
        FullParty party = getPlayerParty(playerUUID);
        if (party == null) {
            sendErrorToPlayer(playerUUID, "§cYou are not in a party.");
            return;
        }

        // Cancel any pending disconnect timer for this player
        cancelDisconnectTimer(playerUUID);

        FullParty.Member member = getMember(party, playerUUID);
        if (member.getRole() == FullParty.Role.LEADER) {
            disbandParty(party, playerUUID);
        } else {
            party.getMembers().remove(member);
            playerToParty.remove(playerUUID);

            PartyMemberLeaveResponseEvent responseEvent = new PartyMemberLeaveResponseEvent(party, playerUUID);
            sendEvent(responseEvent);
        }
    }

    public static void handleDisbandRequest(PartyDisbandRequestEvent event) {
        UUID playerUUID = event.getDisbander();
        FullParty party = getPlayerParty(playerUUID);
        if (party == null) {
            sendErrorToPlayer(playerUUID, "§cYou are not in a party.");
            return;
        }

        FullParty.Member member = getMember(party, playerUUID);
        if (member.getRole() != FullParty.Role.LEADER) {
            sendErrorToPlayer(playerUUID, "§cYou are not the party leader!");
            return;
        }

        disbandParty(party, playerUUID);
    }

    public static void handleTransferRequest(PartyTransferRequestEvent event) {
        UUID currentLeaderUUID = event.getCurrentLeader();
        UUID newLeaderUUID = event.getNewLeader();

        FullParty party = getPlayerParty(currentLeaderUUID);
        if (party == null) {
            sendErrorToPlayer(currentLeaderUUID, "§cYou are not in a party!");
            return;
        }

        FullParty.Member currentLeader = getMember(party, currentLeaderUUID);
        FullParty.Member newLeader = getMember(party, newLeaderUUID);

        if (currentLeader.getRole() != FullParty.Role.LEADER) {
            sendErrorToPlayer(currentLeaderUUID, "§cYou are not the party leader!");
            return;
        }

        if (newLeader == null) {
            sendErrorToPlayer(currentLeaderUUID, "§cThat player is not in your party!");
            return;
        }

        currentLeader.setRole(FullParty.Role.MEMBER);
        newLeader.setRole(FullParty.Role.LEADER);

        PartyLeaderTransferResponseEvent responseEvent = new PartyLeaderTransferResponseEvent(party, currentLeaderUUID, newLeaderUUID);
        sendEvent(responseEvent);
    }

    public static void handleKickRequest(PartyKickRequestEvent event) {
        UUID kickerUUID = event.getKicker();
        UUID targetUUID = event.getTarget();

        FullParty party = getPlayerParty(kickerUUID);
        if (party == null) {
            sendErrorToPlayer(kickerUUID, "§cYou are not in a party!");
            return;
        }

        FullParty.Member kicker = getMember(party, kickerUUID);
        FullParty.Member target = getMember(party, targetUUID);

        if (kicker.getRole() == FullParty.Role.MEMBER) {
            sendErrorToPlayer(kickerUUID, "§cOnly party leaders and moderators can kick players!");
            return;
        }

        if (target == null) {
            sendErrorToPlayer(kickerUUID, "§cThat player is not in your party!");
            return;
        }

        if (target.getRole() == FullParty.Role.LEADER) {
            sendErrorToPlayer(kickerUUID, "§cYou cannot kick the party leader!");
            return;
        }

        // Cancel any pending disconnect timer for the kicked player
        cancelDisconnectTimer(targetUUID);

        party.getMembers().remove(target);
        playerToParty.remove(targetUUID);

        PartyMemberKickResponseEvent responseEvent = new PartyMemberKickResponseEvent(party, kickerUUID, targetUUID);
        sendEvent(responseEvent);
    }

    public static void handlePromoteRequest(PartyPromoteRequestEvent event) {
        UUID promoterUUID = event.getPromoter();
        UUID targetUUID = event.getTarget();

        FullParty party = getPlayerParty(promoterUUID);
        if (party == null) return;

        FullParty.Member promoter = getMember(party, promoterUUID);
        FullParty.Member target = getMember(party, targetUUID);

        if (promoter.getRole() != FullParty.Role.LEADER) {
            sendErrorToPlayer(promoterUUID, "§cOnly the party leader can promote players!");
            return;
        }

        if (target == null || target.getRole() == FullParty.Role.MODERATOR) return;

        target.setRole(FullParty.Role.MODERATOR);

        PartyPromotionResponseEvent responseEvent = new PartyPromotionResponseEvent(party, promoterUUID, targetUUID, FullParty.Role.MODERATOR);
        sendEvent(responseEvent);
    }

    public static void handleDemoteRequest(PartyDemoteRequestEvent event) {
        UUID demoterUUID = event.getDemoter();
        UUID targetUUID = event.getTarget();

        FullParty party = getPlayerParty(demoterUUID);
        if (party == null) return;

        FullParty.Member demoter = getMember(party, demoterUUID);
        FullParty.Member target = getMember(party, targetUUID);

        if (demoter.getRole() != FullParty.Role.LEADER) return;
        if (target == null || target.getRole() == FullParty.Role.MEMBER) return;

        target.setRole(FullParty.Role.MEMBER);

        PartyPromotionResponseEvent responseEvent = new PartyPromotionResponseEvent(party, demoterUUID, targetUUID, FullParty.Role.MEMBER);
        sendEvent(responseEvent);
    }

    public static void handleWarpRequest(PartyWarpRequestEvent event) {
        UUID warperUUID = event.getWarper();

        FullParty party = getPlayerParty(warperUUID);
        if (party == null) {
            sendErrorToPlayer(warperUUID, "§cYou are not in a party!");
            return;
        }

        FullParty.Member warper = getMember(party, warperUUID);
        if (warper.getRole() != FullParty.Role.LEADER) {
            sendErrorToPlayer(warperUUID, "§cYou are not the party leader!");
            return;
        }

        if (party.getMembers().size() == 1) {
            sendErrorToPlayer(warperUUID, "§cYou need someone else in the party to warp!");
            return;
        }

        if (partyWarpCooldown.contains(warperUUID)) {
            sendErrorToPlayer(warperUUID, "§cYou can only warp once every 5 seconds!");
            return;
        }
        partyWarpCooldown.add(warperUUID);
        CompletableFuture.delayedExecutor(5000, TimeUnit.MILLISECONDS)
                .execute(() -> partyWarpCooldown.remove(warperUUID));

        PartyWarpResponseEvent responseEvent = new PartyWarpResponseEvent(party, warperUUID);
        JSONObject message = new JSONObject();
        message.put("eventType", responseEvent.getClass().getSimpleName());
        message.put("eventData", responseEvent.getSerializer().serialize(responseEvent));
        message.put("participants", responseEvent.getParticipants());

        List<UUID> intendedToWarp = party.getParticipants().stream().filter(uuid -> !uuid.equals(warperUUID)).toList();
        Map<UUID, JSONObject> responses = ServiceToServerManager.sendToAllServers(FromServiceChannels.PROPAGATE_PARTY_EVENT, message, 3000).join();
        List<UUID> actualWarped = new ArrayList<>();
        Map<UUID, String> failureReasons = new HashMap<>();

        for (JSONObject response : responses.values()) {
            boolean success = response.getBoolean("success");

            // Check if the warp was blocked (e.g., game already started)
            if (!success) {
                if (response.optBoolean("blocked", false)) {
                    String blockReason = response.optString("blockReason", "Unable to warp");
                    // Apply block reason to all intended warp targets
                    for (UUID uuid : intendedToWarp) {
                        failureReasons.put(uuid, blockReason);
                    }
                }
                continue;
            }

            List<UUID> playersHandledUuids = response.getJSONArray("playersHandledUUIDs")
                    .toList().stream().map(object -> {
                        try {
                            return UUID.fromString(object.toString());
                        } catch (Exception e) {
                            return null;
                        }
                    }).toList();

            for (UUID uuid : playersHandledUuids) {
                if (intendedToWarp.contains(uuid)) {
                    actualWarped.add(uuid);
                }
            }

            // Collect rejection reasons from server response
            if (response.has("rejectedPlayers")) {
                JSONObject rejectedPlayers = response.getJSONObject("rejectedPlayers");
                for (String key : rejectedPlayers.keySet()) {
                    try {
                        UUID rejectedUuid = UUID.fromString(key);
                        String reason = rejectedPlayers.getString(key);
                        failureReasons.put(rejectedUuid, reason);
                    } catch (Exception ignored) {}
                }
            }
        }

        List<UUID> didNotWarp = new ArrayList<>();
        intendedToWarp.forEach(uuid -> {
            if (!actualWarped.contains(uuid)) {
                didNotWarp.add(uuid);
            }
        });

        PartyWarpOverviewResponseEvent warpOverviewResponse = new PartyWarpOverviewResponseEvent(party, warperUUID, actualWarped, didNotWarp, failureReasons);
        sendEvent(warpOverviewResponse);
    }

    public static FullParty getPartyFromPlayer(UUID playerUUID) {
        return getPlayerParty(playerUUID);
    }

    public static void handleHijackRequest(PartyHijackRequestEvent event) {
        UUID hijackerUUID = event.getHijacker();
        UUID targetUUID = event.getTarget();

        FullParty targetParty = getPlayerParty(targetUUID);

        if (targetParty == null) {
            sendErrorToPlayer(hijackerUUID, "§cThat player is not in a party!");
            return;
        }

        FullParty currentParty = getPlayerParty(hijackerUUID);
        if (currentParty != null) {
            handleLeaveRequest(new PartyLeaveRequestEvent(hijackerUUID));
        }

        FullParty.Member currentLeader = targetParty.getMembers().stream()
                .filter(m -> m.getRole() == FullParty.Role.LEADER)
                .findFirst().orElse(null);

        if (currentLeader != null) {
            currentLeader.setRole(FullParty.Role.MEMBER);
        }

        FullParty.Member hijacker = new FullParty.Member(hijackerUUID, FullParty.Role.LEADER, true);
        targetParty.getMembers().add(hijacker);
        playerToParty.put(hijackerUUID, targetParty.getUuid());
    }

    public static void handlePlayerDisconnect(PartyPlayerDisconnectEvent event) {
        UUID playerUUID = event.getDisconnectedPlayer();

        FullParty party = getPlayerParty(playerUUID);
        if (party == null) {
            return; // Player not in a party, nothing to do
        }

        // Check if player already has an active disconnect timer (prevent duplicates)
        if (disconnectTimers.containsKey(playerUUID)) {
            return;
        }

        // Create a unique timer ID for this disconnect event
        UUID timerId = UUID.randomUUID();
        disconnectTimers.put(playerUUID, timerId);

        // Schedule the timeout
        CompletableFuture.delayedExecutor(DISCONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    // Race condition check 1: Timer was cancelled (player rejoined or was removed)
                    UUID currentTimerId = disconnectTimers.get(playerUUID);
                    if (currentTimerId == null || !currentTimerId.equals(timerId)) {
                        return;
                    }

                    // Race condition check 2: Party still exists
                    FullParty currentParty = getPlayerParty(playerUUID);
                    if (currentParty == null) {
                        disconnectTimers.remove(playerUUID);
                        return;
                    }

                    // Race condition check 3: Player still in party
                    FullParty.Member member = getMember(currentParty, playerUUID);
                    if (member == null) {
                        disconnectTimers.remove(playerUUID);
                        return;
                    }

                    // Remove the timer
                    disconnectTimers.remove(playerUUID);

                    // Handle timeout based on role
                    boolean isLeader = member.getRole() == FullParty.Role.LEADER;

                    if (isLeader) {
                        // Leader timed out - disband the party
                        disbandPartyDueToTimeout(currentParty, playerUUID);
                    } else {
                        // Regular member timed out - kick them
                        currentParty.getMembers().remove(member);
                        playerToParty.remove(playerUUID);

                        PartyMemberDisconnectTimeoutResponseEvent responseEvent =
                                new PartyMemberDisconnectTimeoutResponseEvent(currentParty, playerUUID, false);
                        sendEvent(responseEvent);
                    }
                });

        // Notify all party members about the disconnect
        PartyMemberDisconnectedResponseEvent responseEvent =
                new PartyMemberDisconnectedResponseEvent(party, playerUUID, DISCONNECT_TIMEOUT_MS / 1000);
        sendEvent(responseEvent);
    }

    public static void handlePlayerRejoin(PartyPlayerRejoinEvent event) {
        UUID playerUUID = event.getRejoinedPlayer();

        // Check if player has a pending disconnect timer
        UUID timerId = disconnectTimers.remove(playerUUID);
        if (timerId == null) {
            return; // No pending timer, nothing to do
        }

        // Verify player is still in a party
        FullParty party = getPlayerParty(playerUUID);
        if (party == null) {
            return; // Party was disbanded while they were disconnected
        }

        // Notify party members about the rejoin
        PartyMemberRejoinedResponseEvent responseEvent =
                new PartyMemberRejoinedResponseEvent(party, playerUUID);
        sendEvent(responseEvent);
    }

    private static void disbandPartyDueToTimeout(FullParty party, UUID timedOutLeader) {
        // Cancel all disconnect timers for party members
        for (FullParty.Member member : party.getMembers()) {
            disconnectTimers.remove(member.getUuid());
            playerToParty.remove(member.getUuid());
        }
        activeParties.remove(party.getUuid());

        // Send timeout response event (indicating leader timed out)
        PartyMemberDisconnectTimeoutResponseEvent responseEvent =
                new PartyMemberDisconnectTimeoutResponseEvent(party, timedOutLeader, true);
        sendEvent(responseEvent);
    }

    private static void cancelDisconnectTimer(UUID playerUUID) {
        disconnectTimers.remove(playerUUID);
    }

    private static void disbandParty(FullParty party, UUID disbander) {
        // Cancel all disconnect timers for party members
        for (FullParty.Member member : party.getMembers()) {
            cancelDisconnectTimer(member.getUuid());
            playerToParty.remove(member.getUuid());
        }
        activeParties.remove(party.getUuid());

        PartyDisbandResponseEvent responseEvent = new PartyDisbandResponseEvent(party, disbander, "disbanded");
        sendEvent(responseEvent);
    }

    private static void sendEvent(PartyEvent event) {
        JSONObject message = new JSONObject();
        message.put("eventType", event.getClass().getSimpleName());
        message.put("eventData", event.getSerializer().serialize(event));
        message.put("participants", event.getParticipants());

        ServiceToServerManager.sendToAllServers(FromServiceChannels.PROPAGATE_PARTY_EVENT, message);
    }

    private static void sendErrorToPlayer(UUID playerUUID, String message) {
        sendMessageToPlayer(playerUUID, "§9§m-----------------------------------------------------\n" + message + "\n§9§m-----------------------------------------------------");
    }

    private static void sendMessageToPlayer(UUID playerUUID, String message) {
        JSONObject messageData = new JSONObject();
        messageData.put("playerUUID", playerUUID.toString());
        messageData.put("message", message);

        ServiceToServerManager.sendToAllServers(FromServiceChannels.SEND_MESSAGE, messageData);
    }

    private static FullParty getPlayerParty(UUID playerUUID) {
        UUID partyUUID = playerToParty.get(playerUUID);
        return partyUUID != null ? activeParties.get(partyUUID) : null;
    }

    private static FullParty.Member getMember(FullParty party, UUID playerUUID) {
        return party.getMembers().stream()
                .filter(member -> member.getUuid().equals(playerUUID))
                .findFirst().orElse(null);
    }

    private static void scheduleInviteExpiration(UUID inviter, UUID invitee, long delayMs) {
        CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    pendingInvites.remove(invitee);

                    // Check if the player joined the party
                    FullParty party = getPlayerParty(invitee);
                    if (party != null) {
                        FullParty.Member inviteeMember = getMember(party, invitee);
                        if (inviteeMember == null) {
                            sendEvent(new PartyInviteExpiredResponseEvent(party, inviter, invitee));
                        }
                    }
                });
    }
}