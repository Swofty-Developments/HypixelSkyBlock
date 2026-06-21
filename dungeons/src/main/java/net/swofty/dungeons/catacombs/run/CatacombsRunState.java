package net.swofty.dungeons.catacombs.run;

import net.swofty.dungeons.catacombs.blessing.BlessingSet;
import net.swofty.dungeons.catacombs.blessing.BlessingType;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class CatacombsRunState {
    private final CatacombsRunConfig config;
    private final Instant startedAt;
    private final Map<Integer, DungeonRoomState> rooms = new HashMap<>();
    private final BlessingSet blessings = new BlessingSet();
    private DungeonRunPhase phase = DungeonRunPhase.START_ROOM;
    private int deaths;
    private int crypts;
    private int completedRooms;
    private int secretsFound;
    private int failedPuzzles;
    private boolean spiritPetFirstDeathReduction;
    private Instant finishedAt;

    private CatacombsRunState(CatacombsRunConfig config) {
        this.config = config;
        this.startedAt = Instant.now();
        for (int room = 0; room < config.totalRooms(); room++) {
            rooms.put(room, DungeonRoomState.UNDISCOVERED);
        }
    }

    public static CatacombsRunState start(CatacombsRunConfig config) {
        return new CatacombsRunState(config);
    }

    public CatacombsRunConfig config() {
        return config;
    }

    public DungeonRunPhase phase() {
        return phase;
    }

    public int deaths() {
        return deaths;
    }

    public int crypts() {
        return crypts;
    }

    public int completedRooms() {
        return completedRooms;
    }

    public int secretsFound() {
        return secretsFound;
    }

    public int failedPuzzles() {
        return failedPuzzles;
    }

    public Map<Integer, DungeonRoomState> rooms() {
        return Map.copyOf(rooms);
    }

    public BlessingSet blessings() {
        return blessings;
    }

    public Duration elapsed() {
        return Duration.between(startedAt, finishedAt == null ? Instant.now() : finishedAt);
    }

    public CatacombsRunState enterClearing() {
        phase = DungeonRunPhase.CLEARING;
        return this;
    }

    public CatacombsRunState enterBloodRoom() {
        phase = DungeonRunPhase.BLOOD_ROOM;
        return this;
    }

    public CatacombsRunState enterBoss() {
        phase = DungeonRunPhase.BOSS;
        return this;
    }

    public CatacombsRunState complete() {
        phase = DungeonRunPhase.COMPLETED;
        finishedAt = Instant.now();
        return this;
    }

    public CatacombsRunState fail() {
        phase = DungeonRunPhase.FAILED;
        finishedAt = Instant.now();
        return this;
    }

    public CatacombsRunState markRoom(int roomId, DungeonRoomState state) {
        DungeonRoomState previous = rooms.put(roomId, state);
        if (previous != DungeonRoomState.COMPLETED && state == DungeonRoomState.COMPLETED) {
            completedRooms++;
        }
        return this;
    }

    public CatacombsRunState recordDeath(boolean spiritPetReduced) {
        if (spiritPetReduced && !spiritPetFirstDeathReduction) {
            spiritPetFirstDeathReduction = true;
        }
        deaths++;
        return this;
    }

    public CatacombsRunState recordCrypt() {
        crypts++;
        return this;
    }

    public CatacombsRunState recordSecret() {
        secretsFound++;
        return this;
    }

    public CatacombsRunState failPuzzle(int roomId) {
        failedPuzzles++;
        markRoom(roomId, DungeonRoomState.FAILED);
        return this;
    }

    public CatacombsRunState addBlessing(BlessingType type, int level) {
        blessings.add(type, level);
        return this;
    }

    public DungeonScoreBreakdown score() {
        return new DungeonScoreBreakdown(skillScore(), explorationScore(), speedScore(), bonusScore());
    }

    public Map<String, Integer> scoreTrace() {
        Map<String, Integer> trace = new HashMap<>();
        trace.put("deaths", deaths);
        trace.put("failedPuzzles", failedPuzzles);
        trace.put("completedRooms", completedRooms);
        trace.put("totalRooms", config.totalRooms());
        trace.put("secretsFound", secretsFound);
        trace.put("totalSecrets", config.totalSecrets());
        trace.put("crypts", crypts);
        trace.put("elapsedSeconds", (int) elapsed().toSeconds());
        trace.put("speedScoreSeconds", config.floor().rules().speedScoreSeconds());
        trace.put("skill", skillScore());
        trace.put("exploration", explorationScore());
        trace.put("speed", speedScore());
        trace.put("bonus", bonusScore());
        trace.put("total", score().total());
        return Map.copyOf(trace);
    }

    public Map<DungeonRunPhase, Boolean> progressionTrace() {
        Map<DungeonRunPhase, Boolean> trace = new EnumMap<>(DungeonRunPhase.class);
        for (DungeonRunPhase value : DungeonRunPhase.values()) {
            trace.put(value, hasReached(value));
        }
        return Map.copyOf(trace);
    }

    private boolean hasReached(DungeonRunPhase value) {
        if (phase == DungeonRunPhase.FAILED) {
            return value != DungeonRunPhase.COMPLETED;
        }
        return value.ordinal() <= phase.ordinal();
    }

    private int skillScore() {
        int deathPenalty = deaths * 2;
        if (spiritPetFirstDeathReduction && deaths > 0) {
            deathPenalty--;
        }
        return clamp(100 - deathPenalty - failedPuzzles * 10, 20, 100);
    }

    private int explorationScore() {
        double roomCompletion = ratio(completedRooms, config.totalRooms());
        double secretCompletion = ratio(secretsFound, config.totalSecrets());
        return clamp((int) Math.floor(roomCompletion * 60 + secretCompletion * 40), 0, 100);
    }

    private int speedScore() {
        int target = config.floor().rules().speedScoreSeconds();
        long elapsedSeconds = elapsed().toSeconds();
        if (elapsedSeconds <= target) {
            return 100;
        }
        double percentOver = ((double) elapsedSeconds / target - 1) * 100;
        return clamp(100 - speedPenalty(percentOver), 0, 100);
    }

    private int bonusScore() {
        int bonus = Math.min(5, crypts);
        if (secretsFound >= config.totalSecrets()) {
            bonus += 5;
        }
        return clamp(bonus, 0, config.floor().rules().bonusScoreCap());
    }

    private int speedPenalty(double percentOver) {
        double remaining = percentOver;
        int penalty = 0;
        penalty += consumeSpeedBand(remaining, 20, 2);
        remaining -= Math.min(remaining, 20);
        penalty += consumeSpeedBand(remaining, 20, 4);
        remaining -= Math.min(remaining, 20);
        penalty += consumeSpeedBand(remaining, 10, 5);
        remaining -= Math.min(remaining, 10);
        penalty += consumeSpeedBand(remaining, 10, 6);
        remaining -= Math.min(remaining, 10);
        penalty += consumeSpeedBand(remaining, Double.MAX_VALUE, 7);
        return penalty;
    }

    private int consumeSpeedBand(double remainingPercent, double bandSize, double percentPerPoint) {
        if (remainingPercent <= 0) {
            return 0;
        }
        return (int) Math.floor(Math.min(remainingPercent, bandSize) / percentPerPoint);
    }

    private double ratio(int current, int total) {
        if (total <= 0) {
            return 1;
        }
        return Math.min(1, Math.max(0, (double) current / total));
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
