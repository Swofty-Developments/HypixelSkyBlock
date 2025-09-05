package net.swofty.type.bedwarslobby.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.events.response.*;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisBedWarsLobbyPropagatePartyEvent implements ServiceToClient {

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.PROPAGATE_PARTY_EVENT;
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

            PartyEvent event = parseEvent(eventType, eventData);
            if (event == null) {
                Logger.error("Failed to parse event of type: " + eventType);
                return createFailureResponse("Failed to parse event of type: " + eventType);
            }

            List<UUID> playersHandled = handleEventForPlayers(event, participants);
            // Logger.info("Handled party event: " + event.getClass().getSimpleName() + " for " + participants.size() + " players");
            return createSuccessResponse(playersHandled.size(), playersHandled);
        } catch (Exception e) {
            Logger.error("Failed to handle party event: " + e.getMessage());
            return createFailureResponse("Exception occurred: " + e.getMessage());
        }
    }

    private PartyEvent parseEvent(String eventType, String eventData) {
        try {
            PartyEvent templateEvent = PartyEvent.findFromType(eventType);
            return (PartyEvent) templateEvent.getSerializer().deserialize(eventData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<UUID> handleEventForPlayers(PartyEvent event, List<UUID> participants) {
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
                    Logger.error("Failed to handle party event for player " + participantUUID + ": " + e.getMessage());
                }
            }
        }

        return playersHandled;
    }

    private void handleEventForPlayer(HypixelPlayer player, PartyEvent event) {
        switch (event) {
            case PartyInviteResponseEvent inviteEvent -> handleInviteEvent(player, inviteEvent);
            case PartyMemberJoinResponseEvent startedEvent -> handleStartedEvent(player, startedEvent);
            case PartyDisbandResponseEvent disbandEvent -> handleDisbandEvent(player, disbandEvent);
            case PartyLeaderTransferResponseEvent transferEvent -> handleTransferEvent(player, transferEvent);
            case PartyMemberKickResponseEvent kickEvent -> handleKickEvent(player, kickEvent);
            case PartyMemberLeaveResponseEvent leaveEvent -> handleLeaveEvent(player, leaveEvent);
            case PartyPromotionResponseEvent promotionEvent -> handlePromotionEvent(player, promotionEvent);
            case PartyWarpResponseEvent warpEvent -> handleWarpEvent(player, warpEvent);
            case PartyInviteExpiredResponseEvent expiredEvent -> handleInviteExpiredEvent(player, expiredEvent);
            case PartyWarpOverviewResponseEvent overviewEvent -> handleWarpOverviewEvent(player, overviewEvent);
            case PartyChatMessageResponseEvent chatEvent -> handleChatMessageEvent(player, chatEvent);
            case PartyPlayerSwitchedServerResponseEvent switchEvent -> handlePlayerSwitchedServerEvent(player, switchEvent);
            default -> Logger.warn("Unhandled party event type: " + event.getClass().getSimpleName());
        }
    }

    private void handleChatMessageEvent(HypixelPlayer player, PartyChatMessageResponseEvent event) {
        UUID messenger = event.getPlayer();
        String message = event.getMessage();

        String messengerName = HypixelPlayer.getDisplayName(messenger);
        player.sendMessage("§9Party §8> " + messengerName + "§f: " + message);
    }

    private void handlePlayerSwitchedServerEvent(HypixelPlayer player, PartyPlayerSwitchedServerResponseEvent event) {
        if (!event.getMover().equals(player.getUuid())) {
            String moverName = HypixelPlayer.getDisplayName(event.getMover());
            ProxyPlayer mover = new ProxyPlayer(event.getMover());

            if (!mover.isOnline().join()) {
                return;
            }

            UnderstandableProxyServer moverServer = mover.getServer().join();
            ServerType moverServerType = moverServer.type();
            String displayName = "§e" + (moverServerType.isSkyBlock() ? "SkyBlock Travel" : "Hypixel Travel");

            TextComponent component = LegacyComponentSerializer.legacySection().deserialize("§9§l» " + moverName + " §eis traveling to §a" + moverServerType.formatName() + " §e§lFOLLOW");
            Component hoverComponent =
                    LegacyComponentSerializer.legacySection().deserialize(displayName).appendNewline().append(
                            LegacyComponentSerializer.legacySection().deserialize("§9Party Member")).appendNewline().append(
                            LegacyComponentSerializer.legacySection().deserialize(" ")).appendNewline().append(
                            LegacyComponentSerializer.legacySection().deserialize("§eClick to follow!")
                    );

            component = component.hoverEvent(hoverComponent);
            component = component.clickEvent(ClickEvent.clickEvent(
                    ClickEvent.Action.RUN_COMMAND, "/p movetoserver " + moverServer.uuid()
            ));
            player.sendMessage(component);
        }
    }

    private void handleWarpOverviewEvent(HypixelPlayer player, PartyWarpOverviewResponseEvent event) {
        if (event.getWarper().equals(player.getUuid())) {
            player.sendMessage("§9§m-----------------------------------------------------");
            int amountToWarp = event.getSuccessfullyWarped().size() + event.getFailedToWarp().size();
            boolean isPlural = amountToWarp > 1;
            player.sendMessage("§eParty Warp §7(" + amountToWarp + (isPlural ? " players" : " player") + ")");
            for (UUID uuid : event.getSuccessfullyWarped()) {
                player.sendMessage("§a§l✔ " + HypixelPlayer.getDisplayName(uuid) + " §awarped to your server");
            }
            for (UUID uuid : event.getFailedToWarp()) {
                player.sendMessage("§c§l✖ " + HypixelPlayer.getDisplayName(uuid) + " §cwas unable to be warped");
            }
            player.sendMessage("§9§m-----------------------------------------------------");
        }
    }

    private void handleInviteExpiredEvent(HypixelPlayer player, PartyInviteExpiredResponseEvent event) {
        if (event.getInvitee().equals(player.getUuid())) {
            player.sendMessage("§9§m-----------------------------------------------------");
            player.sendMessage("§eThe party invite from " + HypixelPlayer.getDisplayName(event.getInviter()) + " §ehas expired!");
            player.sendMessage("§9§m-----------------------------------------------------");
        } else if (event.getInviter().equals(player.getUuid())) {
            player.sendMessage("§9§m-----------------------------------------------------");
            player.sendMessage("§eThe party invite to " + HypixelPlayer.getDisplayName(event.getInvitee()) + " §ehas expired.");
            player.sendMessage("§9§m-----------------------------------------------------");
        }
    }

    private void handleInviteEvent(HypixelPlayer player, PartyInviteResponseEvent event) {
        if (event.getInvitee().equals(player.getUuid())) {
            UUID inviter = event.getInviter();
            String inviterName = HypixelPlayer.getRawName(inviter);

            player.sendMessage("§9§m-----------------------------------------------------");
            player.sendMessage(HypixelPlayer.getDisplayName(event.getInviter()) + " §ehas invited you to join their party!");
            TextComponent component = LegacyComponentSerializer.legacySection().deserialize("§eYou have §c60 §eseconds to accept. §6Click here to join!");
            component = component.hoverEvent(Component.text("§eClick here to join!"));
            component = component.clickEvent(ClickEvent.runCommand("/p accept " + inviterName));
            player.sendMessage(component);
            player.sendMessage("§9§m-----------------------------------------------------");
        } else {
            player.sendMessage("§9§m-----------------------------------------------------");
            player.sendMessage(HypixelPlayer.getDisplayName(event.getInviter()) + " §einvited " + HypixelPlayer.getDisplayName(event.getInvitee()) + " §eto join the party! They have §c60 §eseconds to accept.");
            player.sendMessage("§9§m-----------------------------------------------------");
        }
    }

    private void handleStartedEvent(HypixelPlayer player, PartyMemberJoinResponseEvent event) {
        UUID leaderUUID = event.getParty().getLeader().getUuid();
        if (!event.getJoiner().equals(player.getUuid())) {
            if (leaderUUID.equals(event.getInviter())) {
                sendMessage(player, HypixelPlayer.getDisplayName(event.getJoiner()) + " §ejoined the party.");
            } else {
                sendMessage(player, HypixelPlayer.getDisplayName(event.getJoiner()) + " §ejoined the party using an invite from " + HypixelPlayer.getDisplayName(event.getInviter()) + "!");
            }
        } else {
            if (leaderUUID.equals(event.getInviter())) {
                sendMessage(player, "§eYou have joined " + HypixelPlayer.getDisplayName(leaderUUID) + "'s §eparty!");
            } else {
                sendMessage(player, "§eYou have joined " + HypixelPlayer.getDisplayName(leaderUUID) + "'s §eparty using an invite from " + HypixelPlayer.getDisplayName(event.getInviter()) + "!");
            }
        }
    }

    private void handleDisbandEvent(HypixelPlayer player, PartyDisbandResponseEvent event) {
        sendMessage(player, HypixelPlayer.getDisplayName(event.getDisbander()) + " §ehas disbanded the party!");
    }

    private void handleTransferEvent(HypixelPlayer player, PartyLeaderTransferResponseEvent event) {
        if (event.getNewLeader().equals(player.getUuid())) {
            sendMessage(player, "§eYou are now the party leader!");
        } else {
            UUID newLeader = event.getNewLeader();
            String newLeaderName = HypixelPlayer.getDisplayName(newLeader);

            sendMessage(player, "§eThe party was transferred to " + newLeaderName);
        }
    }

    private void handleKickEvent(HypixelPlayer player, PartyMemberKickResponseEvent event) {
        if (event.getKicked().equals(player.getUuid())) {
            sendMessage(player, "§cYou have been kicked from the party!");
        } else {
            UUID kicked = event.getKicked();
            String kickedName = HypixelPlayer.getDisplayName(kicked);
            UUID kicker = event.getKicker();
            String kickerName = HypixelPlayer.getDisplayName(kicker);

            sendMessage(player, kickerName + " §ehas kicked " + kickedName + " §efrom the party!");
        }
    }

    private void handleLeaveEvent(HypixelPlayer player, PartyMemberLeaveResponseEvent event) {
        if (!event.getLeaver().equals(player.getUuid())) {
            sendMessage(player, HypixelPlayer.getDisplayName(event.getLeaver()) + " §ehas left the party.");
        } else {
            sendMessage(player, "§eYou left the party.");
        }
    }

    private void handlePromotionEvent(HypixelPlayer player, PartyPromotionResponseEvent event) {
        if (event.getPromoted().equals(player.getUuid())) {
            if (event.getNewRole() == FullParty.Role.MEMBER) {
                sendMessage(player, "§cYou have been demoted to member!");
            } else {
                sendMessage(player, "§aYou have been promoted to " + event.getNewRole().name().toLowerCase() + "!");
            }
        } else {
            String action = event.getNewRole() == FullParty.Role.MEMBER ? "demoted" : "promoted";
            String role = event.getNewRole().name().toLowerCase();

            UUID promoted = event.getPromoted();
            String promotedName = HypixelPlayer.getDisplayName(promoted);
            UUID promoter = event.getPromoter();
            String promoterName = HypixelPlayer.getDisplayName(promoter);

            sendMessage(player, promoterName + " §e" + action + " " + promotedName + " §eto " + role + "!");
        }
    }

    private void handleWarpEvent(HypixelPlayer player, PartyWarpResponseEvent event) {
        if (!event.getWarper().equals(player.getUuid())) {
            UUID warper = event.getWarper();
            String warperName = HypixelPlayer.getDisplayName(warper);
            FullParty party = (FullParty) event.getParty();
            FullParty.Member warperMember = party.getFromUuid(warper);

            sendMessage(player, "§eParty " + warperMember.getRole() + ", " + warperName + "§e, summoned you to their server.");

            ProxyPlayer warperProxy = new ProxyPlayer(warper);
            UnderstandableProxyServer warperServer = warperProxy.getServer().join();

            if (!warperProxy.isOnline().join()) {
                player.sendMessage("§cCouldn't find a proxy for " + warperName + "!");
                throw new RuntimeException("Couldn't find a proxy for " + warperName);
            }

            if (warperServer.uuid() == HypixelConst.getServerUUID()) {
                return;
            }

            ProxyPlayer playerProxy = new ProxyPlayer(player.getUuid());
            playerProxy.transferToWithIndication(warperServer.uuid())
                    .orTimeout(2, TimeUnit.SECONDS)
                    .exceptionally(throwable -> {
                        if (player.isOnline()) {
                            throw new RuntimeException(throwable);
                        }
                        return null; // Return value for the CompletableFuture
                    }).join();
        } else {
            player.sendMessage("§7Warping party...");
        }
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