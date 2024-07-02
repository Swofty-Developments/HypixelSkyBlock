package net.swofty.anticheat.loader.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.listener.PlayerPositionListener;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import net.minestom.server.network.packet.server.play.PlayerPositionAndLookPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.loader.SwoftyPlayerManager;
import net.swofty.anticheat.loader.SwoftySchedulerManager;
import net.swofty.anticheat.loader.Loader;
import net.swofty.anticheat.math.Pos;

import java.util.*;

public class MinestomLoader implements Loader {
    private Map<Runnable, Integer> activeTasks = new HashMap<>();

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
            public Pos getPositionFromPlayer() {
                return new Pos(
                        player.getPosition().x(),
                        player.getPosition().y(),
                        player.getPosition().z()
                );
            }

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
            public Vec getVelocityFromPlayer() {
                return new Vec(
                        player.getVelocity().x(),
                        player.getVelocity().y(),
                        player.getVelocity().z()
                );
            }

            @Override
            public void setVelocityForPlayer(Vec vel) {
                player.setVelocity(new net.minestom.server.coordinate.Vec(
                        vel.x(),
                        vel.y(),
                        vel.z()
                ));
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
