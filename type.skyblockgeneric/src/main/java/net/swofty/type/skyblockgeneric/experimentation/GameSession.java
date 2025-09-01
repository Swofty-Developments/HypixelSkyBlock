package net.swofty.type.skyblockgeneric.experimentation;

import java.util.*;

// This class represents a player's active game session for experimentation
public class GameSession {
    private UUID playerUUID;
    private GameType gameType;
    private String tier;
    private GameState gameState;
    private long startTime;
    private int bonusClicksEarned;
    private int chronomatronBestChain;
    private final Map<String, Object> meta = new HashMap<>();

    public UUID getPlayerUUID() { return playerUUID; }
    public void setPlayerUUID(UUID playerUUID) { this.playerUUID = playerUUID; }
    public GameType getGameType() { return gameType; }
    public void setGameType(GameType gameType) { this.gameType = gameType; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState gameState) { this.gameState = gameState; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public int getBonusClicksEarned() { return bonusClicksEarned; }
    public void setBonusClicksEarned(int bonusClicksEarned) { this.bonusClicksEarned = bonusClicksEarned; }
    public Map<String, Object> getMeta() { return meta; }
    public int getChronomatronBestChain() { return chronomatronBestChain; }
    public void setChronomatronBestChain(int value) { this.chronomatronBestChain = value; }

    public enum GameType {
        CHRONOMATRON,
        ULTRA_SEQUENCER,
        SUPER_PAIRS
    }

    // Abstract class for game-specific states
    public static abstract class GameState {}

    public static class ChronomatronState extends GameState {
        // Core game state management
        public enum GameStateEnum {
            READY,      // Waiting to start a new round
            WATCHING,   // Displaying sequence to player (input disabled)
            PLAYING,    // Waiting for player input (input enabled)
            GAME_OVER   // Game ended (input disabled)
        }
        
        public GameStateEnum gameState = GameStateEnum.READY;
        public List<Integer> correctSequence = new ArrayList<>();
        public int playerInputIndex = 0;  // How far into sequence player has correctly guessed
        public int score = 0;  // Length of last successfully completed sequence
        public long lastClickTime = 0;  // For debounce system (200ms cooldown)
        public int availableColors = 3;  // Number of colors for current tier
        public boolean isSequencePlaying = false;  // Prevents input during sequence display
    }

    public static class UltraSequencerState extends GameState {
        public Map<Integer, Integer> pattern; // Slot -> Number
        public int playerInputPosition = 1;
        public int currentSeriesLength = 0;
    }

    public static class SuperPairsState extends GameState {
        public List<String> boardLayout; // List of item identifiers
        public boolean revealedTiles;
        public int firstFlipIndex = -1;
        public int clicksRemaining;
        public List<String> claimedRewards = new ArrayList<>();
        public int pairsFound = 0;
    }
}
