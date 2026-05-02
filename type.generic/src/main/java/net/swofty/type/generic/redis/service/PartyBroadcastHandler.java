package net.swofty.type.generic.redis.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyBroadcast;
import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.party.PartyBroadcastPushProtocol;
import net.swofty.commons.protocol.objects.party.PartyBroadcastPushProtocol.Request;
import net.swofty.commons.protocol.objects.party.PartyBroadcastPushProtocol.Response;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PartyBroadcastHandler implements TypedServiceHandler<Request, Response> {

    private static final String SEPARATOR = "§9§m-----------------------------------------------------";
    private static final PartyBroadcastPushProtocol PROTOCOL = new PartyBroadcastPushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request message) {
        try {
            PartyBroadcast broadcast = message.broadcast();
            List<UUID> handled = new ArrayList<>();
            Map<UUID, String> rejected = new HashMap<>();

            for (UUID participant : broadcast.participants()) {
                HypixelPlayer player = findPlayer(participant);
                if (player == null) continue;
                try {
                    DispatchResult result = render(player, broadcast);
                    if (result.handled) handled.add(participant);
                    if (result.rejection != null) rejected.put(participant, result.rejection);
                } catch (Exception e) {
                    Logger.error(e, "Failed to render party broadcast {} for {}",
                            broadcast.getClass().getSimpleName(), participant);
                }
            }
            return Response.success(handled, rejected);
        } catch (Exception e) {
            Logger.error(e, "Failed to handle party broadcast");
            return Response.failure(e.getMessage());
        }
    }

    private static HypixelPlayer findPlayer(UUID uuid) {
        return HypixelGenericLoader.getLoadedPlayers().stream()
                .filter(p -> p.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    private static DispatchResult render(HypixelPlayer player, PartyBroadcast broadcast) {
        return switch (broadcast) {
            case PartyBroadcast.Invited b -> renderInvited(player, b);
            case PartyBroadcast.InviteExpired b -> renderInviteExpired(player, b);
            case PartyBroadcast.MemberJoined b -> renderJoined(player, b);
            case PartyBroadcast.MemberLeft b -> renderLeft(player, b);
            case PartyBroadcast.MemberKicked b -> renderKicked(player, b);
            case PartyBroadcast.LeaderTransferred b -> renderTransferred(player, b);
            case PartyBroadcast.RoleChanged b -> renderRoleChanged(player, b);
            case PartyBroadcast.Disbanded b -> renderDisbanded(player, b);
            case PartyBroadcast.Chat b -> renderChat(player, b);
            case PartyBroadcast.Warp b -> renderWarp(player, b);
            case PartyBroadcast.WarpOverview b -> renderWarpOverview(player, b);
            case PartyBroadcast.MemberSwitchedServer b -> renderSwitchedServer(player, b);
            case PartyBroadcast.MemberDisconnected b -> renderDisconnected(player, b);
            case PartyBroadcast.MemberRejoined b -> renderRejoined(player, b);
            case PartyBroadcast.MemberDisconnectTimedOut b -> renderDisconnectTimedOut(player, b);
        };
    }

    private static DispatchResult renderInvited(HypixelPlayer player, PartyBroadcast.Invited b) {
        UUID inviter = b.party().leader();
        UUID invitee = b.party().invitee();
        if (invitee.equals(player.getUuid())) {
            String inviterName = HypixelPlayer.getRawName(inviter);
            player.sendMessage(SEPARATOR);
            player.sendMessage(HypixelPlayer.getDisplayName(inviter) + " §ehas invited you to join their party!");
            TextComponent component = LegacyComponentSerializer.legacySection()
                    .deserialize("§eYou have §c60 §eseconds to accept. §6Click here to join!");
            component = component.hoverEvent(Component.text("§eClick here to join!"));
            component = component.clickEvent(ClickEvent.runCommand("/p accept " + inviterName));
            player.sendMessage(component);
            player.sendMessage(SEPARATOR);
        } else {
            sendBoxed(player, HypixelPlayer.getDisplayName(inviter)
                    + " §einvited " + HypixelPlayer.getDisplayName(invitee)
                    + " §eto join the party! They have §c60 §eseconds to accept.");
        }
        return DispatchResult.ok();
    }

    private static DispatchResult renderInviteExpired(HypixelPlayer player, PartyBroadcast.InviteExpired b) {
        if (b.invitee().equals(player.getUuid())) {
            sendBoxed(player, "§eThe party invite from " + HypixelPlayer.getDisplayName(b.inviter()) + " §ehas expired!");
        } else if (b.inviter().equals(player.getUuid())) {
            sendBoxed(player, "§eThe party invite to " + HypixelPlayer.getDisplayName(b.invitee()) + " §ehas expired.");
        }
        return DispatchResult.ok();
    }

    private static DispatchResult renderJoined(HypixelPlayer player, PartyBroadcast.MemberJoined b) {
        UUID leaderUUID = b.party().getLeader().getUuid();
        if (!b.joiner().equals(player.getUuid())) {
            if (leaderUUID.equals(b.inviter())) {
                sendBoxed(player, HypixelPlayer.getDisplayName(b.joiner()) + " §ejoined the party.");
            } else {
                sendBoxed(player, HypixelPlayer.getDisplayName(b.joiner())
                        + " §ejoined the party using an invite from " + HypixelPlayer.getDisplayName(b.inviter()) + "!");
            }
        } else if (leaderUUID.equals(b.inviter())) {
            sendBoxed(player, "§eYou have joined " + HypixelPlayer.getDisplayName(leaderUUID) + "'s §eparty!");
        } else {
            sendBoxed(player, "§eYou have joined " + HypixelPlayer.getDisplayName(leaderUUID)
                    + "'s §eparty using an invite from " + HypixelPlayer.getDisplayName(b.inviter()) + "!");
        }
        return DispatchResult.ok();
    }

    private static DispatchResult renderLeft(HypixelPlayer player, PartyBroadcast.MemberLeft b) {
        if (b.leaver().equals(player.getUuid())) {
            sendBoxed(player, "§eYou left the party.");
        } else {
            sendBoxed(player, HypixelPlayer.getDisplayName(b.leaver()) + " §ehas left the party.");
        }
        return DispatchResult.ok();
    }

    private static DispatchResult renderKicked(HypixelPlayer player, PartyBroadcast.MemberKicked b) {
        if (b.kicked().equals(player.getUuid())) {
            sendBoxed(player, "§cYou have been kicked from the party!");
        } else {
            sendBoxed(player, HypixelPlayer.getDisplayName(b.kicker())
                    + " §ehas kicked " + HypixelPlayer.getDisplayName(b.kicked()) + " §efrom the party!");
        }
        return DispatchResult.ok();
    }

    private static DispatchResult renderTransferred(HypixelPlayer player, PartyBroadcast.LeaderTransferred b) {
        if (b.newLeader().equals(player.getUuid())) {
            sendBoxed(player, "§eYou are now the party leader!");
        } else {
            sendBoxed(player, "§eThe party was transferred to " + HypixelPlayer.getDisplayName(b.newLeader()));
        }
        return DispatchResult.ok();
    }

    private static DispatchResult renderRoleChanged(HypixelPlayer player, PartyBroadcast.RoleChanged b) {
        boolean isDemotion = b.newRole() == FullParty.Role.MEMBER;
        String roleName = b.newRole().name().toLowerCase();
        if (b.promoted().equals(player.getUuid())) {
            sendBoxed(player, isDemotion ? "§cYou have been demoted to member!" : "§aYou have been promoted to " + roleName + "!");
        } else {
            String verb = isDemotion ? "demoted" : "promoted";
            sendBoxed(player, HypixelPlayer.getDisplayName(b.promoter()) + " §e" + verb + " "
                    + HypixelPlayer.getDisplayName(b.promoted()) + " §eto " + roleName + "!");
        }
        return DispatchResult.ok();
    }

    private static DispatchResult renderDisbanded(HypixelPlayer player, PartyBroadcast.Disbanded b) {
        sendBoxed(player, HypixelPlayer.getDisplayName(b.disbander()) + " §ehas disbanded the party!");
        return DispatchResult.ok();
    }

    private static DispatchResult renderChat(HypixelPlayer player, PartyBroadcast.Chat b) {
        player.sendMessage("§9Party §8> " + HypixelPlayer.getDisplayName(b.sender()) + "§f: " + b.message());
        return DispatchResult.ok();
    }

    private static DispatchResult renderWarp(HypixelPlayer player, PartyBroadcast.Warp b) {
        if (b.warper().equals(player.getUuid())) {
            player.sendMessage("§7Warping party...");
            return DispatchResult.ok();
        }

        UUID warper = b.warper();
        String warperName = HypixelPlayer.getDisplayName(warper);
        FullParty.Member warperMember = b.party().getFromUuid(warper);

        sendBoxed(player, "§eParty " + warperMember.getRole() + ", " + warperName + "§e, summoned you to their server.");

        ProxyPlayer warperProxy = new ProxyPlayer(warper);
        if (!warperProxy.isOnline().join()) {
            player.sendMessage("§cCouldn't find a proxy for " + warperName + "!");
            return DispatchResult.rejected("Warper offline");
        }

        UnderstandableProxyServer warperServer = warperProxy.getServer().join();
        if (warperServer.uuid().equals(HypixelConst.getServerUUID())) {
            return DispatchResult.ok();
        }

        new ProxyPlayer(player.getUuid()).transferToWithIndication(warperServer.uuid())
                .orTimeout(2, TimeUnit.SECONDS)
                .exceptionally(throwable -> {
                    if (player.isOnline()) {
                        throw new RuntimeException(throwable);
                    }
                    return null;
                }).join();
        return DispatchResult.ok();
    }

    private static DispatchResult renderWarpOverview(HypixelPlayer player, PartyBroadcast.WarpOverview b) {
        if (!b.warper().equals(player.getUuid())) return DispatchResult.ok();
        int total = b.warped().size() + b.failed().size();
        boolean plural = total > 1;
        String label = HypixelConst.getTypeLoader().getType().isSkyBlock() ? "§eSkyBlock Party Warp" : "§eParty Warp";
        player.sendMessage(SEPARATOR);
        player.sendMessage(label + " §7(" + total + (plural ? " players" : " player") + ")");
        for (UUID uuid : b.warped()) {
            player.sendMessage("§a§l✔ " + HypixelPlayer.getDisplayName(uuid) + " §awarped to your server");
        }
        for (UUID uuid : b.failed()) {
            String reason = b.failureReasons() != null
                    ? b.failureReasons().getOrDefault(uuid, "Unable to warp")
                    : "Unable to warp";
            player.sendMessage("§c§l✖ " + HypixelPlayer.getDisplayName(uuid) + " §c- " + reason);
        }
        player.sendMessage(SEPARATOR);
        return DispatchResult.ok();
    }

    private static DispatchResult renderSwitchedServer(HypixelPlayer player, PartyBroadcast.MemberSwitchedServer b) {
        if (b.mover().equals(player.getUuid())) return DispatchResult.ok();

        ProxyPlayer mover = new ProxyPlayer(b.mover());
        if (!mover.isOnline().join()) return DispatchResult.ok();

        String moverName = HypixelPlayer.getDisplayName(b.mover());
        UnderstandableProxyServer moverServer = mover.getServer().join();
        ServerType moverServerType = moverServer.type();
        String displayName = "§e" + (moverServerType.isSkyBlock() ? "SkyBlock Travel" : "Hypixel Travel");

        TextComponent component = LegacyComponentSerializer.legacySection()
                .deserialize("§9§l» " + moverName + " §eis traveling to §a" + moverServerType.formatName() + " §e§lFOLLOW");
        Component hover = LegacyComponentSerializer.legacySection().deserialize(displayName).appendNewline()
                .append(LegacyComponentSerializer.legacySection().deserialize("§9Party Member")).appendNewline()
                .append(LegacyComponentSerializer.legacySection().deserialize(" ")).appendNewline()
                .append(LegacyComponentSerializer.legacySection().deserialize("§eClick to follow!"));

        component = component.hoverEvent(hover);
        component = component.clickEvent(ClickEvent.runCommand("/p movetoserver " + moverServer.uuid()));
        player.sendMessage(component);
        return DispatchResult.ok();
    }

    private static DispatchResult renderDisconnected(HypixelPlayer player, PartyBroadcast.MemberDisconnected b) {
        if (b.disconnectedPlayer().equals(player.getUuid())) return DispatchResult.ok();
        int minutes = (int) (b.timeoutSeconds() / 60);
        sendBoxed(player, HypixelPlayer.getDisplayName(b.disconnectedPlayer())
                + " §ehas disconnected. They have §c" + minutes + " minutes §eto rejoin before being removed.");
        return DispatchResult.ok();
    }

    private static DispatchResult renderRejoined(HypixelPlayer player, PartyBroadcast.MemberRejoined b) {
        if (b.rejoinedPlayer().equals(player.getUuid())) return DispatchResult.ok();
        sendBoxed(player, HypixelPlayer.getDisplayName(b.rejoinedPlayer()) + " §ehas reconnected to the party.");
        return DispatchResult.ok();
    }

    private static DispatchResult renderDisconnectTimedOut(HypixelPlayer player, PartyBroadcast.MemberDisconnectTimedOut b) {
        String name = HypixelPlayer.getDisplayName(b.timedOutPlayer());
        if (b.wasLeader()) {
            sendBoxed(player, "§cThe party leader " + name + " §ctimed out. The party has been disbanded.");
        } else if (!b.timedOutPlayer().equals(player.getUuid())) {
            sendBoxed(player, name + " §ehas been removed from the party due to disconnect timeout.");
        }
        return DispatchResult.ok();
    }

    private static void sendBoxed(HypixelPlayer player, String message) {
        player.sendMessage(SEPARATOR);
        player.sendMessage(message);
        player.sendMessage(SEPARATOR);
    }

    private record DispatchResult(boolean handled, String rejection) {
        static DispatchResult ok() { return new DispatchResult(true, null); }
        static DispatchResult rejected(String reason) { return new DispatchResult(false, reason); }
    }
}
