package net.swofty.type.generic.event.actions;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.punishment.GetActivePunishmentProtocol;
import net.swofty.commons.punishment.*;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ActionPlayerMute implements HypixelEventClass {
    private static final ProxyService PUNISHMENT_SERVICE = new ProxyService(ServiceType.PUNISHMENT);
    private static final ConcurrentHashMap<UUID, ActivePunishment> ACTIVE_MUTES = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap.KeySetView<UUID, Boolean> REFRESHING = ConcurrentHashMap.newKeySet();

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false,
            phase = EventPhase.POST_SPAWN, isAsync = true)
    public void loadMute(PlayerSpawnEvent event) {
        refreshMute(event.getPlayer().getUuid());
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY, order = -100)
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        ActivePunishment punishment = ACTIVE_MUTES.get(player.getUuid());

        if (punishment != null && (punishment.expiresAt() <= 0 || punishment.expiresAt() > System.currentTimeMillis())) {
            event.setCancelled(true);
            player.sendMessage(PunishmentMessages.muteMessage(punishment));
        } else {
            ACTIVE_MUTES.remove(player.getUuid());
        }

        Thread.startVirtualThread(() -> refreshMute(player.getUuid()));
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void clearMute(PlayerDisconnectEvent event) {
        UUID uuid = event.getPlayer().getUuid();
        ACTIVE_MUTES.remove(uuid);
        REFRESHING.remove(uuid);
    }

    private static void refreshMute(UUID uuid) {
        if (!REFRESHING.add(uuid)) return;

        try {
            Object response = PUNISHMENT_SERVICE
                    .handleRequest(new GetActivePunishmentProtocol.GetActivePunishmentMessage(
                            uuid, PunishmentType.MUTE.name()))
                    .orTimeout(2, TimeUnit.SECONDS)
                    .join();

            if (response instanceof GetActivePunishmentProtocol.GetActivePunishmentResponse(
                    boolean found, String type, String banId, PunishmentReason reason,
                    long expiresAt, List<PunishmentTag> tags, boolean success, String error
            ) && found) {
                ACTIVE_MUTES.put(uuid, new ActivePunishment(type, banId, reason, expiresAt, tags));
            } else {
                ACTIVE_MUTES.remove(uuid);
            }
        } catch (Exception ignored) {
            // Preserve the last known state during a transient service outage.
        } finally {
            REFRESHING.remove(uuid);
        }
    }
}
