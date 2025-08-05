package net.swofty.types.generic.party;

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
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PartyManager {
    private static final ProxyService partyService = new ProxyService(ServiceType.PARTY);

    public static boolean isInParty(SkyBlockPlayer player) {
        if (!partyService.isOnline().join()) return false;
        IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage message = new IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage(player.getUuid());
        return partyService.<IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage,
                        IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse>handleRequest(message)
                .thenApply(IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse::isInParty)
                .join();
    }

    public static void invitePlayer(SkyBlockPlayer inviter, String targetName) {
        @Nullable UUID targetUUID = DataHandler.getPotentialUUIDFromName(targetName);
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

    public static void acceptInvite(SkyBlockPlayer player, String inviterName) {
        @Nullable UUID inviterUUID = DataHandler.getPotentialUUIDFromName(inviterName);
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

    public static void leaveParty(SkyBlockPlayer player) {
        PartyLeaveRequestEvent event = new PartyLeaveRequestEvent(player.getUuid());
        sendEventToService(event);
    }

    public static void disbandParty(SkyBlockPlayer player) {
        PartyDisbandRequestEvent event = new PartyDisbandRequestEvent(player.getUuid());
        sendEventToService(event);
    }

    public static void transferLeadership(SkyBlockPlayer leader, String targetName) {
        @Nullable UUID targetUUID = DataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(leader, "§cCouldn't find a player with that name!");
            return;
        }

        PartyTransferRequestEvent event = new PartyTransferRequestEvent(leader.getUuid(), targetUUID);
        sendEventToService(event);
    }

    public static void kickPlayer(SkyBlockPlayer kicker, String targetName) {
        @Nullable UUID targetUUID = DataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(kicker, "§cCouldn't find a player with that name!");
            return;
        }

        PartyKickRequestEvent event = new PartyKickRequestEvent(kicker.getUuid(), targetUUID);
        sendEventToService(event);
    }

    public static void promotePlayer(SkyBlockPlayer promoter, String targetName) {
        @Nullable UUID targetUUID = DataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(promoter, "§cCouldn't find a player with that name!");
            return;
        }

        PartyPromoteRequestEvent event = new PartyPromoteRequestEvent(promoter.getUuid(), targetUUID);
        sendEventToService(event);
    }

    public static void demotePlayer(SkyBlockPlayer demoter, String targetName) {
        @Nullable UUID targetUUID = DataHandler.getPotentialUUIDFromName(targetName);
        if (targetUUID == null) {
            sendError(demoter, "§cCouldn't find a player with that name!");
            return;
        }

        PartyDemoteRequestEvent event = new PartyDemoteRequestEvent(demoter.getUuid(), targetUUID);
        sendEventToService(event);
    }

    public static void warpParty(SkyBlockPlayer warper) {
        PartyWarpRequestEvent event = new PartyWarpRequestEvent(warper.getUuid());
        sendEventToService(event);
    }

    public static @Nullable FullParty getPartyFromPlayer(SkyBlockPlayer player) {
        if (!partyService.isOnline().join()) return null;

        GetPartyProtocolObject.GetPartyMessage message = new GetPartyProtocolObject.GetPartyMessage(player.getUuid());
        return (FullParty) partyService.<GetPartyProtocolObject.GetPartyMessage,
                        GetPartyProtocolObject.GetPartyResponse>handleRequest(message)
                .thenApply(GetPartyProtocolObject.GetPartyResponse::party)
                .join();
    }

    public static void sendChat(SkyBlockPlayer player, String message) {
        PartyChatMessageEvent event = new PartyChatMessageEvent(player.getUuid(), message);
        sendEventToService(event);
    }

    public static void switchPartyServer(SkyBlockPlayer player) {
        PartyPlayerSwitchedServerEvent event = new PartyPlayerSwitchedServerEvent(player.getUuid());
        sendEventToService(event);
    }

    public static void hijackParty(SkyBlockPlayer hijacker, String targetName) {
        if (!hijacker.getRank().isEqualOrHigherThan(Rank.ADMIN)) {
            hijacker.sendMessage("§cYou need ADMIN or above to do this command");
            return;
        }

        @Nullable UUID targetUUID = DataHandler.getPotentialUUIDFromName(targetName);
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

    private static void sendError(SkyBlockPlayer player, String message) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage(message);
        player.sendMessage("§9§m-----------------------------------------------------");
    }

    private static void sendSuccess(SkyBlockPlayer player, String message) {
        player.sendMessage("§9§m-----------------------------------------------------");
        player.sendMessage(message);
        player.sendMessage("§9§m-----------------------------------------------------");
    }
}