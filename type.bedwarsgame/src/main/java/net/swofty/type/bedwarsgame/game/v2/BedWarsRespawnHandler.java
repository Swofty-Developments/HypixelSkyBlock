package net.swofty.type.bedwarsgame.game.v2;

import lombok.RequiredArgsConstructor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.PlayerRespawnCompleteEvent;
import net.swofty.type.game.game.respawn.RespawnHandler;
import net.swofty.type.generic.event.HypixelEventHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class BedWarsRespawnHandler implements RespawnHandler<BedWarsPlayer> {
    private static final int DEFAULT_RESPAWN_DELAY = 5;

    private final BedWarsGame game;
    private final Map<UUID, Task> respawnTasks = new ConcurrentHashMap<>();

    @Override
    public boolean canRespawn(UUID playerId) {
        return game.getPlayerTeam(playerId)
            .map(BedWarsTeam::isBedAlive)
            .orElse(false);
    }

    @Override
    public int getRespawnDelay(UUID playerId) {
        return DEFAULT_RESPAWN_DELAY;
    }

    @Override
    public void startRespawn(BedWarsPlayer player) {
        if (!canRespawn(player.getUuid())) {
            game.onPlayerEliminated(player);
            return;
        }

        // Todo stuff

        // Start countdown
        AtomicInteger countdown = new AtomicInteger(getRespawnDelay(player.getUuid()));
        AtomicReference<Task> taskRef = new AtomicReference<>();

        Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (!player.isOnline()) {
                cancelRespawn(player.getUuid());
                return;
            }

            int remaining = countdown.getAndDecrement();

            if (remaining > 0) {
                showRespawnTitle(player, remaining);
            } else {
                completeRespawn(player);
                Task t = taskRef.get();
                if (t != null) t.cancel();
                respawnTasks.remove(player.getUuid());
            }
        }).repeat(TaskSchedule.seconds(1)).schedule();

        taskRef.set(task);
        respawnTasks.put(player.getUuid(), task);
    }

    @Override
    public void cancelRespawn(UUID playerId) {
        Task task = respawnTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public boolean isRespawning(UUID playerId) {
        return respawnTasks.containsKey(playerId);
    }

    private void showRespawnTitle(BedWarsPlayer player, int seconds) {

    }

    private void completeRespawn(BedWarsPlayer player) {
        player.clearTitle();

        game.setupPlayer(player);

        HypixelEventHandler.callCustomEvent(new PlayerRespawnCompleteEvent(
            game.getGameId(),
            player.getUuid(),
            player.getUsername()
        ));
    }
}
