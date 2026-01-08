package net.swofty.type.generic.party;

import net.swofty.commons.ServiceType;
import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.PendingParty;
import net.swofty.commons.party.events.*;
import net.swofty.commons.protocol.objects.party.GetPartyProtocolObject;
import net.swofty.commons.protocol.objects.party.IsPlayerInPartyProtocolObject;
import net.swofty.commons.protocol.objects.party.SendPartyEventToServiceProtocolObject;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PartyManager {
    private static final ProxyService partyService = new ProxyService(ServiceType.PARTY);

    public static boolean isInParty(HypixelPlayer player) {
        if (!partyService.isOnline().join()) return false;
        IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage message = new IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage(player.getUuid());
        return partyService.<IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage,
                        IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse>handleRequest(message)
                .thenApply(IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse::isInParty)
                .join();
    }

    public static void invitePlayer(HypixelPlayer inviter, String targetName) {
        @Nullable UUID targetUUID = HypixelDataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(inviter, "§cCouldn't find a player with that name!");
            return;
        }

        ProxyPlayer target = new ProxyPlayer(targetUUID);
        if (!target.isOnline().join()) {
            sendError(inviter, "§cThat player is not online!");
            return;
        }

        if (targetUUID.equals(inviter.getUuid())) {
            sendError(inviter, "§cYou cannot invite yourself!");
            return;
        }

        if (isInParty(inviter)) {
            FullParty party = getPartyFromPlayer(inviter);
            if (party.getMembers().stream().anyMatch(member -> member.getUuid().equals(targetUUID))) {
                sendError(inviter, "§cThat player is already in your party!");
                return;
            }
        }

        PendingParty pendingParty = PendingParty.create(targetUUID, inviter.getUuid());
        PartyInviteEvent event = new PartyInviteEvent(pendingParty);
        sendEventToService(event);
    }

    public static void acceptInvite(HypixelPlayer player, String inviterName) {
        @Nullable UUID inviterUUID = HypixelDataHandler.getPotentialUUIDFromName(inviterName);
        if (inviterUUID == null) {
            sendError(player, "§cCouldn't find a player with that name!");
            return;
        }

        if (isInParty(player)) {
            sendError(player, "§cYou must leave your current party before accepting an invite!");
            return;
        }

        PartyAcceptInviteEvent event = new PartyAcceptInviteEvent(player.getUuid(), inviterUUID);
        sendEventToService(event);
    }

    public static void leaveParty(HypixelPlayer player) {
        PartyLeaveRequestEvent event = new PartyLeaveRequestEvent(player.getUuid());
        sendEventToService(event);
    }

    public static void disbandParty(HypixelPlayer player) {
        PartyDisbandRequestEvent event = new PartyDisbandRequestEvent(player.getUuid());
        sendEventToService(event);
    }

    public static void transferLeadership(HypixelPlayer leader, String targetName) {
        @Nullable UUID targetUUID = HypixelDataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(leader, "§cCouldn't find a player with that name!");
            return;
        }

        PartyTransferRequestEvent event = new PartyTransferRequestEvent(leader.getUuid(), targetUUID);
        sendEventToService(event);
    }

    public static void kickPlayer(HypixelPlayer kicker, String targetName) {
        @Nullable UUID targetUUID = HypixelDataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(kicker, "§cCouldn't find a player with that name!");
            return;
        }

        PartyKickRequestEvent event = new PartyKickRequestEvent(kicker.getUuid(), targetUUID);
        sendEventToService(event);
    }

    public static void promotePlayer(HypixelPlayer promoter, String targetName) {
        @Nullable UUID targetUUID = HypixelDataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(promoter, "§cCouldn't find a player with that name!");
            return;
        }

        PartyPromoteRequestEvent event = new PartyPromoteRequestEvent(promoter.getUuid(), targetUUID);
        sendEventToService(event);
    }

    public static void demotePlayer(HypixelPlayer demoter, String targetName) {
        @Nullable UUID targetUUID = HypixelDataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(demoter, "§cCouldn't find a player with that name!");
            return;
        }

        PartyDemoteRequestEvent event = new PartyDemoteRequestEvent(demoter.getUuid(), targetUUID);
        sendEventToService(event);
    }

    public static void warpParty(HypixelPlayer warper) {
        PartyWarpRequestEvent event = new PartyWarpRequestEvent(warper.getUuid());
        sendEventToService(event);
    }

    public static @Nullable FullParty getPartyFromPlayer(HypixelPlayer player) {
        if (!partyService.isOnline().join()) return null;

        GetPartyProtocolObject.GetPartyMessage message = new GetPartyProtocolObject.GetPartyMessage(player.getUuid());
        return (FullParty) partyService.<GetPartyProtocolObject.GetPartyMessage,
                        GetPartyProtocolObject.GetPartyResponse>handleRequest(message)
                .thenApply(GetPartyProtocolObject.GetPartyResponse::party)
                .join();
    }

    public static void sendChat(HypixelPlayer player, String message) {
        PartyChatMessageEvent event = new PartyChatMessageEvent(player.getUuid(), message);
        sendEventToService(event);
    }

    public static void switchPartyServer(HypixelPlayer player) {
        PartyPlayerSwitchedServerEvent event = new PartyPlayerSwitchedServerEvent(player.getUuid());
        sendEventToService(event);
    }

    public static void notifyPlayerDisconnect(UUID playerUUID) {
        if (!partyService.isOnline().join()) return;
        PartyPlayerDisconnectEvent event = new PartyPlayerDisconnectEvent(playerUUID);
        sendEventToService(event);
    }

    public static void notifyPlayerRejoin(UUID playerUUID) {
        if (!partyService.isOnline().join()) return;
        PartyPlayerRejoinEvent event = new PartyPlayerRejoinEvent(playerUUID);
        sendEventToService(event);
    }

    public static void hijackParty(HypixelPlayer hijacker, String targetName) {
        if (!hijacker.getRank().isEqualOrHigherThan(Rank.STAFF)) {
            hijacker.sendMessage("§cYou need STAFF to do this command");
            return;
        }

        @Nullable UUID targetUUID = HypixelDataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(hijacker, "§cCouldn't find a player with that name!");
            return;
        }

        ProxyPlayer target = new ProxyPlayer(targetUUID);
        if (!target.isOnline().join()) {
            sendError(hijacker, "§cThat player is not online!");
            return;
        }

        PartyHijackRequestEvent event = new PartyHijackRequestEvent(hijacker.getUuid(), targetUUID);
        sendEventToService(event);
    }

    private static void sendEventToService(PartyEvent event) {
        var message = new SendPartyEventToServiceProtocolObject.SendPartyEventToServiceMessage(event);
        partyService.handleRequest(message);
    }

    private static void sendError(HypixelPlayer player, String message) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage(message);
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private static void sendSuccess(HypixelPlayer player, String message) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage(message);
        player.sendMessage("§9§m-----------------------------------------------------");
    }
}