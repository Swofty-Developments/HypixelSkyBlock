package net.swofty.type.lobby.events;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LobbyAFKEvents implements HypixelEventClass {

    private static final long AFK_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5);
    private static final Map<UUID, Long> lastActivityAt = new ConcurrentHashMap<>();
    private static final AtomicBoolean schedulerStarted = new AtomicBoolean(false);

    public LobbyAFKEvents() {
        startSchedulerIfNeeded();
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onPlayerSpawn(PlayerSpawnEvent event) {
        markPlayerActive(event.getPlayer().getUuid());
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        UUID uuid = event.getPlayer().getUuid();
        lastActivityAt.remove(uuid);
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUuid();
        markPlayerActive(uuid);
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onPlayerPacket(PlayerPacketEvent event) {
        ClientPacket packet = event.getPacket();
        if (packet instanceof ClientPlayerRotationPacket
            || packet instanceof ClientPlayerPositionAndRotationPacket) {
            markPlayerActive(event.getPlayer().getUuid());
        }
    }

    private static void startSchedulerIfNeeded() {
        if (!schedulerStarted.compareAndSet(false, true)) {
            return;
        }

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            ServerType currentType = HypixelConst.getTypeLoader().getType();

            long now = System.currentTimeMillis();
            for (Player p : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
                if (!(p instanceof HypixelPlayer player)) {
                    continue;
                }

                UUID uuid = player.getUuid();
                long lastActivity = lastActivityAt.getOrDefault(uuid, now);
                if (now - lastActivity < AFK_TIMEOUT_MS) {
                    continue;
                }

                player.asProxyPlayer().transferToLimboFromAfk(currentType);
                lastActivityAt.remove(uuid);
            }
        }).repeat(TaskSchedule.seconds(10)).schedule();
    }

    private static void markPlayerActive(UUID uuid) {
        lastActivityAt.put(uuid, System.currentTimeMillis());
    }

}