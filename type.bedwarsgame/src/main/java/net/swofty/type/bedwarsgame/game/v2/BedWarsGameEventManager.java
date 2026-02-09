package net.swofty.type.bedwarsgame.game.v2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.events.custom.BedWarsGameEventAdvanceEvent;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.event.HypixelEventHandler;

@RequiredArgsConstructor
public class BedWarsGameEventManager {
    private final BedWarsGame game;

    @Getter
    private GamePhase currentPhase = GamePhase.DIAMOND_I;
    @Getter
    private long secondsUntilNextPhase = 0;

    private Task ticker;

    public enum GamePhase {
        DIAMOND_I("Diamond I", 6 * 60, 30, 60),
        EMERALD_I("Emerald I", 2 * 6 * 60, 30, 60),
        DIAMOND_II("Diamond II", 3 * 6 * 60, 30, 60),
        EMERALD_II("Emerald II", 4 * 6 * 60, 30, 60),
        DIAMOND_III("Diamond III", 5 * 6 * 60, 30, 60),
        EMERALD_III("Emerald III", 6 * 6 * 60, 30, 60),
        BED_DESTRUCTION("Bed Destruction", 7 * 6 * 60, 30, 60),
        SUDDEN_DEATH("Sudden Death", 7 * 6 * 60 + 10 * 60, 30, 60),
        GAME_END("Game End", 3720, 30, 60);

        @Getter
        private final String displayName;
        @Getter
        private final int triggerTimeSeconds;
        @Getter
        private final int diamondSpawnSeconds;
        @Getter
        private final int emeraldSpawnSeconds;

        GamePhase(String displayName, int triggerTime, int diamondSpawn, int emeraldSpawn) {
            this.displayName = displayName;
            this.triggerTimeSeconds = triggerTime;
            this.diamondSpawnSeconds = diamondSpawn;
            this.emeraldSpawnSeconds = emeraldSpawn;
        }

        public GamePhase next() {
            int ordinal = this.ordinal();
            GamePhase[] values = values();
            return ordinal < values.length - 1 ? values[ordinal + 1] : this;
        }
    }

    public void start() {
        if (ticker != null) return;

        currentPhase = GamePhase.DIAMOND_I;
        secondsUntilNextPhase = currentPhase.getTriggerTimeSeconds();

        ticker = MinecraftServer.getSchedulerManager().buildTask(this::tick)
            .delay(TaskSchedule.seconds(1))
            .repeat(TaskSchedule.seconds(1))
            .schedule();
    }

    public void stop() {
        if (ticker != null) {
            ticker.cancel();
            ticker = null;
        }
    }

    private void tick() {
        if (game.getState() != GameState.IN_PROGRESS) {
            stop();
            return;
        }

        secondsUntilNextPhase--;

        if (secondsUntilNextPhase <= 0) {
            advancePhase();
        }
    }

    private void advancePhase() {
        GamePhase previous = currentPhase;
        currentPhase = currentPhase.next();

        if (currentPhase != previous) {
            secondsUntilNextPhase = currentPhase.next() != currentPhase
                ? currentPhase.getTriggerTimeSeconds()
                : Integer.MAX_VALUE;

            // Fire event
            HypixelEventHandler.callCustomEvent(new BedWarsGameEventAdvanceEvent(
                game.getGameId(),
                previous.getDisplayName(),
                currentPhase.getDisplayName(),
                secondsUntilNextPhase
            ));

            // Handle special phases
            if (currentPhase == GamePhase.BED_DESTRUCTION) {
                destroyAllBeds();
            }
        }
    }

    private void destroyAllBeds() {
        // Would destroy all remaining beds and fire events
        for (BedWarsTeam team : game.getTeams()) {
            if (team.isBedAlive()) {
                team.destroyBed();
            }
        }
    }

    public int getDiamondSpawnSeconds() {
        return currentPhase.getDiamondSpawnSeconds();
    }

    public int getEmeraldSpawnSeconds() {
        return currentPhase.getEmeraldSpawnSeconds();
    }

    @Deprecated(forRemoval = true)
    public GamePhase getNextEvent() {
        GamePhase next = currentPhase.next();
        return next != currentPhase ? next : null;
    }

    public GamePhase getCurrentEvent() {
        return currentPhase;
    }
}
