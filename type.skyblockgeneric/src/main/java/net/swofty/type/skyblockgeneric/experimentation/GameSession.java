package net.swofty.type.skyblockgeneric.experimentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class GameSession {
    private final UUID playerId;
    private final ExperimentType type;
    private final ExperimentTier tier;
    private final long startTime;
    private GameState state;
    private int bestScore;

    public GameSession(UUID playerId, ExperimentType type, ExperimentTier tier) {
        this.playerId = playerId;
        this.type = type;
        this.tier = tier;
        this.startTime = System.currentTimeMillis();
        this.state = switch (type) {
            case SUPERPAIRS -> new SuperPairsState(tier.baseClicks());
            case CHRONOMATRON -> new ChronomatronState();
            case ULTRASEQUENCER -> new UltraSequencerState();
        };
    }

    public UUID playerId() {
        return playerId;
    }

    public ExperimentType type() {
        return type;
    }

    public ExperimentTier tier() {
        return tier;
    }

    public long startTime() {
        return startTime;
    }

    public GameState state() {
        return state;
    }

    public void state(GameState state) {
        this.state = state;
    }

    public int bestScore() {
        return bestScore;
    }

    public void bestScore(int score) {
        bestScore = Math.max(bestScore, score);
    }

    public abstract static class GameState {
    }

    public enum GamePhase {
        READY,
        WATCHING,
        PLAYING,
        COMPLETE
    }

    public static final class ChronomatronState extends GameState {
        private final List<Integer> sequence = new ArrayList<>();
        private GamePhase phase = GamePhase.READY;
        private int inputIndex;
        private long lastInput;
        private long deadline;

        public List<Integer> sequence() {
            return sequence;
        }

        public GamePhase phase() {
            return phase;
        }

        public void phase(GamePhase phase) {
            this.phase = phase;
        }

        public int inputIndex() {
            return inputIndex;
        }

        public void inputIndex(int inputIndex) {
            this.inputIndex = inputIndex;
        }

        public long lastInput() {
            return lastInput;
        }

        public void lastInput(long lastInput) {
            this.lastInput = lastInput;
        }

        public long deadline() {
            return deadline;
        }

        public void deadline(long deadline) {
            this.deadline = deadline;
        }
    }

    public static final class UltraSequencerState extends GameState {
        private final List<Integer> sequence = new ArrayList<>();
        private GamePhase phase = GamePhase.READY;
        private int inputIndex;
        private long deadline;

        public List<Integer> sequence() {
            return sequence;
        }

        public GamePhase phase() {
            return phase;
        }

        public void phase(GamePhase phase) {
            this.phase = phase;
        }

        public int inputIndex() {
            return inputIndex;
        }

        public void inputIndex(int inputIndex) {
            this.inputIndex = inputIndex;
        }

        public long deadline() {
            return deadline;
        }

        public void deadline(long deadline) {
            this.deadline = deadline;
        }
    }

    public static final class SuperPairsState extends GameState {
        private final List<SuperPairItem> board = new ArrayList<>();
        private final Set<Integer> matchedTiles = new java.util.HashSet<>();
        private final int totalClicks;
        private int clicksRemaining;
        private int firstFlip = -1;
        private int mismatchFirst = -1;
        private int mismatchSecond = -1;
        private long mismatchUntil;

        public SuperPairsState(int totalClicks) {
            this.totalClicks = totalClicks;
            this.clicksRemaining = totalClicks;
        }

        public List<SuperPairItem> board() {
            return board;
        }

        public Set<Integer> matchedTiles() {
            return matchedTiles;
        }

        public int totalClicks() {
            return totalClicks;
        }

        public int clicksRemaining() {
            return clicksRemaining;
        }

        public void clicksRemaining(int clicksRemaining) {
            this.clicksRemaining = clicksRemaining;
        }

        public int firstFlip() {
            return firstFlip;
        }

        public void firstFlip(int firstFlip) {
            this.firstFlip = firstFlip;
        }

        public int mismatchFirst() {
            return mismatchFirst;
        }

        public void mismatchFirst(int mismatchFirst) {
            this.mismatchFirst = mismatchFirst;
        }

        public int mismatchSecond() {
            return mismatchSecond;
        }

        public void mismatchSecond(int mismatchSecond) {
            this.mismatchSecond = mismatchSecond;
        }

        public long mismatchUntil() {
            return mismatchUntil;
        }

        public void mismatchUntil(long mismatchUntil) {
            this.mismatchUntil = mismatchUntil;
        }

        public int pairsFound() {
            return matchedTiles.size() / 2;
        }
    }
}
