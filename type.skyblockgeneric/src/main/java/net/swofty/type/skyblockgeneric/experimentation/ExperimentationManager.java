package net.swofty.type.skyblockgeneric.experimentation;

import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointExperimentation;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExperimentationManager {
    
    private static final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();
    
    // Proper tier system based on Experimentations.md
    public static class GameTier {
        public final int colors;
        public final int xpPerNote;
        public final Map<Integer, Integer> clickMilestones;
        
        public GameTier(int colors, int xpPerNote, Map<Integer, Integer> clickMilestones) {
            this.colors = colors;
            this.xpPerNote = xpPerNote;
            this.clickMilestones = clickMilestones;
        }
    }
    
    public static final Map<String, GameTier> TIERS = new HashMap<>();
    static {
        TIERS.put("HIGH", new GameTier(3, 1500, Map.of(5, 1, 9, 2)));
        TIERS.put("GRAND", new GameTier(5, 2500, Map.of(5, 1, 9, 2)));
        TIERS.put("SUPREME", new GameTier(7, 3500, Map.of(5, 1, 9, 2)));
        TIERS.put("TRANSCENDENT", new GameTier(8, 4500, Map.of(5, 1, 9, 2)));
        TIERS.put("METAPHYSICAL", new GameTier(10, 6000, Map.of(5, 1, 9, 2, 12, 3)));
    }
    
    // SuperPairs methods
    public static boolean startSuperPairs(HypixelPlayer player, String tier) {
        UUID playerUUID = player.getUuid();
        
        GameSession session = new GameSession();
        session.setPlayerUUID(playerUUID);
        session.setGameType(GameSession.GameType.SUPER_PAIRS);
        session.setTier(tier);
        session.setStartTime(System.currentTimeMillis());

        GameSession.SuperPairsState state = new GameSession.SuperPairsState();
        
        // Generate a 4x4 board with 8 pairs of items
        List<String> items = Arrays.asList(
            "DIAMOND", "EMERALD", "GOLD_INGOT", "IRON_INGOT", 
            "COAL", "REDSTONE", "LAPIS_LAZULI", "QUARTZ"
        );
        
        List<String> boardLayout = new ArrayList<>();
        for (String item : items) {
            boardLayout.add(item);
            boardLayout.add(item); // Add the pair
        }
        
        // Shuffle the board
        Collections.shuffle(boardLayout);
        
        state.boardLayout = boardLayout;
        state.revealedTiles = false;
        state.firstFlipIndex = -1;
        state.clicksRemaining = getClicksForTier(tier);
        state.pairsFound = 0;
        
        session.setGameState(state);
        sessions.put(playerUUID, session);
        
        return true;
    }
    
    public static GameSession.SuperPairsState getSuperPairsState(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.SuperPairsState)) {
            return null;
        }
        return (GameSession.SuperPairsState) session.getGameState();
    }
    
    public static SuperPairsFlipResult flipTile(HypixelPlayer player, int tileIndex) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.SuperPairsState state)) {
            return new SuperPairsFlipResult(false, "no-active-session", false, null, 0, 0, false);
        }
        
        // Check if player has clicks remaining
        if (state.clicksRemaining <= 0) {
            return new SuperPairsFlipResult(false, "no-clicks-remaining", false, null, state.clicksRemaining, state.pairsFound, false);
        }
        
        // Decrease clicks
        state.clicksRemaining--;
        
        // Get the item at the flipped tile
        String itemType = state.boardLayout.get(tileIndex);
        
        // Check if this is the first flip
        if (state.firstFlipIndex == -1) {
            state.firstFlipIndex = tileIndex;
            return new SuperPairsFlipResult(true, null, false, itemType, state.clicksRemaining, state.pairsFound, false);
        }
        
        // This is the second flip, check for match
        String firstItemType = state.boardLayout.get(state.firstFlipIndex);
        boolean isMatch = firstItemType.equals(itemType);
        
        if (isMatch) {
            // Found a pair
            state.pairsFound++;
            state.claimedRewards.add(itemType);
            
            // Check if game is complete (all 8 pairs found)
            boolean gameComplete = state.pairsFound >= 8;
            
            // Reset for next pair
            state.firstFlipIndex = -1;
            
            return new SuperPairsFlipResult(true, null, true, itemType, state.clicksRemaining, state.pairsFound, gameComplete);
        } else {
            // No match, reset for next attempt
            state.firstFlipIndex = -1;
            
            return new SuperPairsFlipResult(true, null, false, itemType, state.clicksRemaining, state.pairsFound, false);
        }
    }
    
    public static SuperPairsFinishResult finishSuperPairs(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.SuperPairsState state)) {
            return new SuperPairsFinishResult(false, "no-active-session", 0, 0, 0);
        }
        
        int pairsFound = state.pairsFound;
        int xpAward = calculateXpAward(pairsFound, session.getTier());
        int bonusClicksEarned = calculateBonusClicks(pairsFound);
        
        // Persist bonus clicks into player's experimentation datapoint
        try {
            DatapointExperimentation experimentation = ((SkyBlockPlayer) player).getSkyblockDataHandler().get(SkyBlockDataHandler.Data.EXPERIMENTATION, DatapointExperimentation.class);
            if (experimentation != null) {
                DatapointExperimentation.PlayerExperimentation data = experimentation.getValue();
                data.superpairsBonusClicks += bonusClicksEarned;
                experimentation.setValue(data);
            }
        } catch (Exception ignored) {}
        
        // Clean up session
        sessions.remove(player.getUuid());
        
        return new SuperPairsFinishResult(true, null, pairsFound, xpAward, bonusClicksEarned);
    }
    
    // Ultrasequencer methods
    public static boolean startUltrasequencer(HypixelPlayer player, String tier) {
        UUID playerUUID = player.getUuid();
        
        GameSession session = new GameSession();
        session.setPlayerUUID(playerUUID);
        session.setGameType(GameSession.GameType.ULTRA_SEQUENCER);
        session.setTier(tier);
        session.setStartTime(System.currentTimeMillis());

        GameSession.UltraSequencerState state = new GameSession.UltraSequencerState();
        
        // Generate initial pattern with 3 numbers
        Random random = new Random();
        state.pattern = new HashMap<>();
        state.pattern.put(0, random.nextInt(9) + 1); // 1-9
        state.pattern.put(1, random.nextInt(9) + 1);
        state.pattern.put(2, random.nextInt(9) + 1);
        
        state.playerInputPosition = 1;
        state.currentSeriesLength = 3;
        
        session.setGameState(state);
        sessions.put(playerUUID, session);
        
        return true;
    }
    
    public static GameSession.UltraSequencerState getUltrasequencerState(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.UltraSequencerState)) {
            return null;
        }
        return (GameSession.UltraSequencerState) session.getGameState();
    }
    
    public static UltrasequencerInputResult inputUltrasequencer(HypixelPlayer player, int slotIndex) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.UltraSequencerState state)) {
            return new UltrasequencerInputResult(false, "no-active-session", false, false, 0);
        }
        
        // Check if the input is correct
        Integer expectedNumber = state.pattern.get(state.playerInputPosition - 1);
        boolean correct = expectedNumber != null && expectedNumber == slotIndex;
        
        if (correct) {
            state.playerInputPosition++;
            
            // Check if the current sequence is complete
            if (state.playerInputPosition > state.currentSeriesLength) {
                // Sequence complete, generate next round
                state.playerInputPosition = 1;
                state.currentSeriesLength++;
                
                // Add one more number to the pattern
                Random random = new Random();
                state.pattern.put(state.currentSeriesLength - 1, random.nextInt(9) + 1);
                
                return new UltrasequencerInputResult(true, null, true, true, state.currentSeriesLength);
            } else {
                return new UltrasequencerInputResult(true, null, true, false, state.currentSeriesLength);
            }
        } else {
            // Wrong input, game over
            return new UltrasequencerInputResult(true, null, false, false, state.currentSeriesLength);
        }
    }
    
    public static UltrasequencerFinishResult finishUltrasequencer(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.UltraSequencerState state)) {
            return new UltrasequencerFinishResult(false, "no-active-session", 0, 0, 0);
        }
        
        int bestSeriesLength = state.currentSeriesLength;
        int xpAward = calculateXpAward(bestSeriesLength, session.getTier());
        int bonusClicksEarned = calculateBonusClicks(bestSeriesLength);
        
        // Persist bonus clicks into player's experimentation datapoint
        try {
            DatapointExperimentation experimentation = ((SkyBlockPlayer) player).getSkyblockDataHandler().get(SkyBlockDataHandler.Data.EXPERIMENTATION, DatapointExperimentation.class);
            if (experimentation != null) {
                DatapointExperimentation.PlayerExperimentation data = experimentation.getValue();
                data.superpairsBonusClicks += bonusClicksEarned;
                experimentation.setValue(data);
            }
        } catch (Exception ignored) {}
        
        // Clean up session
        sessions.remove(player.getUuid());
        
        return new UltrasequencerFinishResult(true, null, bestSeriesLength, xpAward, bonusClicksEarned);
    }
    
    // Improved Chronomatron methods based on Experimentations.md
    public static boolean startChronomatron(HypixelPlayer player, String tier) {
        UUID playerUUID = player.getUuid();
        
        GameSession session = new GameSession();
        session.setPlayerUUID(playerUUID);
        session.setGameType(GameSession.GameType.CHRONOMATRON);
        session.setTier(tier);
        session.setStartTime(System.currentTimeMillis());

        GameSession.ChronomatronState state = new GameSession.ChronomatronState();
        
        // Initialize with proper tier settings
        GameTier gameTier = TIERS.get(tier.toUpperCase());
        if (gameTier == null) {
            gameTier = TIERS.get("HIGH"); // Default fallback
        }
        
        state.availableColors = gameTier.colors;
        state.gameState = GameSession.ChronomatronState.GameStateEnum.READY;
        state.correctSequence = new ArrayList<>();
        state.playerInputIndex = 0;
        state.score = 0;
        state.lastClickTime = 0;
        state.isSequencePlaying = false;
        
        session.setChronomatronBestChain(0);
        session.setGameState(state);
        sessions.put(playerUUID, session);
        
        // Do NOT start the first round here. The GUI triggers the first round when appropriate.
        
        return true;
    }
    
    public static GameSession.ChronomatronState getChronomatronState(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState)) {
            return null;
        }
        return (GameSession.ChronomatronState) session.getGameState();
    }
    
    /**
     * Starts a new round in Chronomatron
     */
    public static void startNewChronomatronRound(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState state)) {
            return;
        }
        
        // Set game state to WATCHING (input disabled)
        state.gameState = GameSession.ChronomatronState.GameStateEnum.WATCHING;
        state.isSequencePlaying = true;
        state.playerInputIndex = 0;
        
        // Ensure the first round starts with 3 colors, then add 1 per subsequent round
        Random random = new Random();
        if (state.correctSequence.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                state.correctSequence.add(random.nextInt(state.availableColors));
            }
        } else {
            state.correctSequence.add(random.nextInt(state.availableColors));
        }
        
        // The sequence will be played by the GUI, then set to PLAYING state
        // This is handled by the GUI after the sequence animation completes
    }
    
    /**
     * Called by GUI when sequence display is complete
     */
    public static void onChronomatronSequenceComplete(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState state)) {
            return;
        }
        
        // Enable player input
        state.gameState = GameSession.ChronomatronState.GameStateEnum.PLAYING;
        state.isSequencePlaying = false;
    }
    
    /**
     * Handles a single color input from the player
     */
    public static ChronomatronInputResult inputChronomatron(HypixelPlayer player, int color) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState state)) {
            return new ChronomatronInputResult(false, "no-active-session", false, false, 0);
        }
        
        // Input Guard: Check if we're in PLAYING state
        if (state.gameState != GameSession.ChronomatronState.GameStateEnum.PLAYING) {
            return new ChronomatronInputResult(false, "input-disabled", false, false, state.correctSequence.size());
        }
        
        // Debounce Clicks: 200ms cooldown
        long currentTime = System.currentTimeMillis();
        if (currentTime - state.lastClickTime < 200) {
            return new ChronomatronInputResult(false, "click-too-fast", false, false, state.correctSequence.size());
        }
        state.lastClickTime = currentTime;
        
        // Check for correctness
        if (state.playerInputIndex >= state.correctSequence.size()) {
            return new ChronomatronInputResult(false, "invalid-input-index", false, false, state.correctSequence.size());
        }
        
        int expectedColor = state.correctSequence.get(state.playerInputIndex);
        boolean correct = expectedColor == color;
        
        if (correct) {
            // Correct input
            state.playerInputIndex++;
            
            // Check if round is complete
            if (state.playerInputIndex >= state.correctSequence.size()) {
                // Round complete - player successfully repeated the whole sequence
                state.score = state.correctSequence.size();
                
                // Update best chain if needed
                if (state.score > session.getChronomatronBestChain()) {
                    session.setChronomatronBestChain(state.score);
                }
                
                // Set to READY for next round
                state.gameState = GameSession.ChronomatronState.GameStateEnum.READY;
                
                return new ChronomatronInputResult(true, null, true, true, state.correctSequence.size());
            } else {
                // Still inputting sequence
                return new ChronomatronInputResult(true, null, true, false, state.correctSequence.size());
            }
        } else {
            // Wrong input - game over
            state.gameState = GameSession.ChronomatronState.GameStateEnum.GAME_OVER;
            return new ChronomatronInputResult(true, null, false, false, state.correctSequence.size());
        }
    }
    
    public static ChronomatronFinishResult finishChronomatron(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState state)) {
            return new ChronomatronFinishResult(false, "no-active-session", 0, 0, 0);
        }
        
        int bestChain = session.getChronomatronBestChain();
        int xpAward = calculateChronomatronXpAward(bestChain, session.getTier());
        int bonusClicksEarned = calculateChronomatronBonusClicks(bestChain);
        
        // Persist bonus clicks into player's experimentation datapoint
        try {
            DatapointExperimentation experimentation = ((SkyBlockPlayer) player).getSkyblockDataHandler().get(SkyBlockDataHandler.Data.EXPERIMENTATION, DatapointExperimentation.class);
            if (experimentation != null) {
                DatapointExperimentation.PlayerExperimentation data = experimentation.getValue();
                data.superpairsBonusClicks += bonusClicksEarned;
                experimentation.setValue(data);
            }
        } catch (Exception ignored) {}
        
        // Clean up session
        sessions.remove(player.getUuid());
        
        return new ChronomatronFinishResult(true, null, bestChain, xpAward, bonusClicksEarned);
    }

    // Helper methods
    private static int getClicksForTier(String tier) {
        String t = tier.toLowerCase();
        if (t.contains("high")) return 20;
        if (t.contains("grand")) return 18;
        if (t.contains("supreme")) return 16;
        if (t.contains("transcendent")) return 14;
        if (t.contains("metaphysical")) return 12;
        return 20; // default
    }
    
    private static int calculateXpAward(int pairsFound, String tier) {
        int baseXp = pairsFound * 100;
        String t = tier.toLowerCase();
        
        if (t.contains("high")) return baseXp;
        if (t.contains("grand")) return (int)(baseXp * 1.5);
        if (t.contains("supreme")) return baseXp * 2;
        if (t.contains("transcendent")) return (int)(baseXp * 2.5);
        if (t.contains("metaphysical")) return baseXp * 3;
        
        return baseXp;
    }
    
    /**
     * Improved reward calculation based on Experimentations.md
     * XP is capped at score 15, clicks based on milestones
     */
    private static int calculateChronomatronXpAward(int chainLength, String tier) {
        // Get tier data
        GameTier gameTier = TIERS.get(tier.toUpperCase());
        if (gameTier == null) {
            gameTier = TIERS.get("HIGH"); // Default fallback
        }
        
        // XP is capped at score 15
        int xpScore = Math.min(chainLength, 15);
        return xpScore * gameTier.xpPerNote;
    }
    
    private static int calculateBonusClicks(int pairsFound) {
        if (pairsFound >= 8) return 3;
        if (pairsFound >= 6) return 2;
        if (pairsFound >= 4) return 1;
        return 0;
    }
    
    /**
     * Improved bonus clicks calculation based on milestones
     */
    private static int calculateChronomatronBonusClicks(int chainLength) {
        // Use the highest tier's milestones for calculation
        GameTier gameTier = TIERS.get("METAPHYSICAL"); // Use highest tier for max rewards
        
        int totalClicks = 0;
        // Iterate through milestones to find the highest one reached
        for (Map.Entry<Integer, Integer> milestone : gameTier.clickMilestones.entrySet()) {
            if (chainLength >= milestone.getKey()) {
                totalClicks = milestone.getValue();
            }
        }
        
        return totalClicks;
    }
    
    private static int getInitialSequenceLength(String tier) {
        return 3;
    }
    
    private static int getColorCountForTier(String tier) {
        GameTier gameTier = TIERS.get(tier.toUpperCase());
        if (gameTier == null) {
            return 3; // Default fallback
        }
        return gameTier.colors;
    }
    
    // Result classes
    public static class SuperPairsFlipResult {
        public final boolean success;
        public final String errorMessage;
        public final boolean isMatch;
        public final String itemType;
        public final int clicksRemaining;
        public final int pairsFound;
        public final boolean gameComplete;
        
        public SuperPairsFlipResult(boolean success, String errorMessage, boolean isMatch, String itemType, 
                                  int clicksRemaining, int pairsFound, boolean gameComplete) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.isMatch = isMatch;
            this.itemType = itemType;
            this.clicksRemaining = clicksRemaining;
            this.pairsFound = pairsFound;
            this.gameComplete = gameComplete;
        }
    }
    
    public static class SuperPairsFinishResult {
        public final boolean success;
        public final String errorMessage;
        public final int pairsFound;
        public final int xpAward;
        public final int bonusClicksEarned;
        
        public SuperPairsFinishResult(boolean success, String errorMessage, int pairsFound, int xpAward, int bonusClicksEarned) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.pairsFound = pairsFound;
            this.xpAward = xpAward;
            this.bonusClicksEarned = bonusClicksEarned;
        }
    }
    
    public static class UltrasequencerInputResult {
        public final boolean success;
        public final String errorMessage;
        public final boolean correct;
        public final boolean complete;
        public final int currentSeriesLength;
        
        public UltrasequencerInputResult(boolean success, String errorMessage, boolean correct, boolean complete, int currentSeriesLength) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.correct = correct;
            this.complete = complete;
            this.currentSeriesLength = currentSeriesLength;
        }
    }
    
    public static class UltrasequencerFinishResult {
        public final boolean success;
        public final String errorMessage;
        public final int bestSeriesLength;
        public final int xpAward;
        public final int bonusClicksEarned;
        
        public UltrasequencerFinishResult(boolean success, String errorMessage, int bestSeriesLength, int xpAward, int bonusClicksEarned) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.bestSeriesLength = bestSeriesLength;
            this.xpAward = xpAward;
            this.bonusClicksEarned = bonusClicksEarned;
        }
    }
    
    public static class ChronomatronInputResult {
        public final boolean success;
        public final String errorMessage;
        public final boolean correct;
        public final boolean complete;
        public final int sequenceLength;
        
        public ChronomatronInputResult(boolean success, String errorMessage, boolean correct, boolean complete, int sequenceLength) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.correct = correct;
            this.complete = complete;
            this.sequenceLength = sequenceLength;
        }
    }
    
    public static class ChronomatronFinishResult {
        public final boolean success;
        public final String errorMessage;
        public final int bestChain;
        public final int xpAward;
        public final int bonusClicksEarned;
        
        public ChronomatronFinishResult(boolean success, String errorMessage, int bestChain, int xpAward, int bonusClicksEarned) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.bestChain = bestChain;
            this.xpAward = xpAward;
            this.bonusClicksEarned = bonusClicksEarned;
        }
    }
}
