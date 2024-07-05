package net.swofty.anticheat.loader.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.*;
import net.minestom.server.network.packet.server.play.ServerDataPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.SwoftyEventHandler;
import net.swofty.anticheat.event.events.AnticheatPacketEvent;
import net.swofty.anticheat.event.packet.IsOnGroundPacket;
import net.swofty.anticheat.event.packet.PositionAndRotationPacket;
import net.swofty.anticheat.event.packet.PositionPacket;
import net.swofty.anticheat.event.packet.RotationPacket;
import net.swofty.anticheat.loader.managers.SwoftyPlayerManager;
import net.swofty.anticheat.loader.managers.SwoftySchedulerManager;
import net.swofty.anticheat.loader.Loader;
import net.swofty.anticheat.math.Pos;

import java.util.*;

public class MinestomLoader extends Loader {
    private Map<Runnable, Integer> activeTasks = new HashMap<>();

    public void registerListeners(GlobalEventHandler eventHandler) {
        EventNode<Event> eventNode = EventNode.all("packet-handler-client");

        eventNode.addListener(PlayerPacketEvent.class, rawEvent -> {
            ClientPacket packet = rawEvent.getPacket();
            AnticheatPacketEvent playerPacketEvent = null;

            System.out.println(packet.getClass().getSimpleName());

            switch (packet.getClass().getSimpleName()) {
                case ("ClientPlayerPositionAndRotationPacket") -> {
                    ClientPlayerPositionAndRotationPacket packetCasted = (ClientPlayerPositionAndRotationPacket) packet;

                    playerPacketEvent = new AnticheatPacketEvent(new PositionAndRotationPacket(
                            SwoftyPlayer.players.get(rawEvent.getPlayer().getUuid()),
                            new Pos(
                                    packetCasted.position().x(),
                                    packetCasted.position().y(),
                                    packetCasted.position().z(),
                                    packetCasted.position().yaw(),
                                    packetCasted.position().pitch()
                            ),
                            packetCasted.onGround()
                    ));
                }
                case ("ClientPlayerPacket") -> {
                    ClientPlayerPacket packetCasted = (ClientPlayerPacket) packet;

                    playerPacketEvent = new AnticheatPacketEvent(new IsOnGroundPacket(
                            SwoftyPlayer.players.get(rawEvent.getPlayer().getUuid()),
                            packetCasted.onGround()
                    ));
                }
                case ("ClientPlayerPositionPacket") -> {
                    ClientPlayerPositionPacket packetCasted = (ClientPlayerPositionPacket) packet;

                    playerPacketEvent = new AnticheatPacketEvent(new PositionPacket(
                            SwoftyPlayer.players.get(rawEvent.getPlayer().getUuid()),
                            packetCasted.position().x(),
                            packetCasted.position().y(),
                            packetCasted.position().z(),
                            packetCasted.onGround()
                    ));
                }
                case ("ClientPlayerRotationPacket") -> {
                    ClientPlayerRotationPacket packetCasted = (ClientPlayerRotationPacket) packet;

                    playerPacketEvent = new AnticheatPacketEvent(new RotationPacket(
                            SwoftyPlayer.players.get(rawEvent.getPlayer().getUuid()),
                            packetCasted.yaw(),
                            packetCasted.pitch(),
                            packetCasted.onGround()
                    ));
                }
            }

            if (playerPacketEvent != null) {
                SwoftyEventHandler.callEvent(playerPacketEvent);
            }
        });
        eventHandler.addChild(eventNode);
    }

    @Override
    public SwoftySchedulerManager getSchedulerManager() {
        return new SwoftySchedulerManager() {
            @Override
            public int scheduleDelayedTask(Runnable runnable, int delay) {
                int id = activeTasks.size();

                activeTasks.put(runnable, id);
                MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                    runnable.run();
                    activeTasks.remove(runnable);
                    return TaskSchedule.stop();
                }, TaskSchedule.tick(delay));

                return id;
            }

            @Override
            public int scheduleRepeatingTask(Runnable runnable, int delay, int period) {
                int id = activeTasks.size();

                activeTasks.put(runnable, id);
                MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                    if (!activeTasks.containsKey(runnable)) {
                        return TaskSchedule.stop();
                    }
                    runnable.run();
                    return TaskSchedule.tick(period);
                }, TaskSchedule.tick(delay));

                return id;
            }

            @Override
            public void cancelTask(int taskId) {
                List<Runnable> toRemove = new ArrayList<>();
                activeTasks.forEach((runnable, id) -> {
                    if (id == taskId) {
                        toRemove.add(runnable);
                    }
                });

                toRemove.forEach(activeTasks::remove);
            }

            @Override
            public void cancelAllTasks() {
                activeTasks.clear();
            }
        };
    }

    @Override
    public SwoftyPlayerManager getPlayerManager(UUID uuid) {
        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);

        return new SwoftyPlayerManager(uuid) {
            @Override
            public void setPositionForPlayer(Pos pos) {
                player.teleport(new net.minestom.server.coordinate.Pos(
                        pos.x(),
                        pos.y(),
                        pos.z(),
                        pos.yaw(),
                        pos.pitch()
                ));
            }

            @Override
            public void setVelocityForPlayer(Vec vel) {
                player.setVelocity(new net.minestom.server.coordinate.Vec(
                        vel.x(),
                        vel.y(),
                        vel.z()
                ));
            }

            @Override
            public void sendMessage(String message) {
                player.sendMessage(message);
            }
        };
    }

    @Override
    public List<UUID> getOnlinePlayers() {
        List<UUID> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> players.add(player.getUuid()));
        return players;
    }
}
