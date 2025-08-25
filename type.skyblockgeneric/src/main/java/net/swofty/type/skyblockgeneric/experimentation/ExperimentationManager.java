package net.swofty.type.skyblockgeneric.experimentation;

import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointExperimentation;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExperimentationManager {
    
    private static final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();
    
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
    
    // Chronomatron methods (keeping existing functionality)
    public static boolean startChronomatron(HypixelPlayer player, String tier) {
        UUID playerUUID = player.getUuid();
        
        GameSession session = new GameSession();
        session.setPlayerUUID(playerUUID);
        session.setGameType(GameSession.GameType.CHRONOMATRON);
        session.setTier(tier);
        session.setStartTime(System.currentTimeMillis());

        GameSession.ChronomatronState state = new GameSession.ChronomatronState();
        // Generate initial sequence based on tier
        Random r = new Random();
        int initialLength = getInitialSequenceLength(tier);
        int colorCount = getColorCountForTier(tier);
        for (int i = 0; i < initialLength; i++) state.correctSequence.add(r.nextInt(colorCount));
        session.setChronomatronBestChain(0);
        session.setGameState(state);

        sessions.put(playerUUID, session);
        
        return true;
    }
    
    public static GameSession.ChronomatronState getChronomatronState(HypixelPlayer player) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState)) {
            return null;
        }
        return (GameSession.ChronomatronState) session.getGameState();
    }
    
    public static ChronomatronInputResult inputChronomatron(HypixelPlayer player, List<Integer> inputs) {
        GameSession session = sessions.get(player.getUuid());
        if (session == null || !(session.getGameState() instanceof GameSession.ChronomatronState state)) {
            return new ChronomatronInputResult(false, "no-active-session", false, 0);
        }
        
        // Check if the input matches the sequence
        if (inputs.size() != state.correctSequence.size()) {
            return new ChronomatronInputResult(false, "wrong-sequence-length", false, state.correctSequence.size());
        }
        
        boolean correct = true;
        for (int i = 0; i < inputs.size(); i++) {
            if (!inputs.get(i).equals(state.correctSequence.get(i))) {
                correct = false;
                break;
            }
        }
        
        if (correct) {
            // Correct input, generate next round
            Random random = new Random();
            int colorCount = getColorCountForTier(session.getTier());
            state.correctSequence.add(random.nextInt(colorCount));
            state.playerInputPosition = 0;
            
            // Update best chain if needed
            if (state.correctSequence.size() > session.getChronomatronBestChain()) {
                session.setChronomatronBestChain(state.correctSequence.size());
            }
            
            return new ChronomatronInputResult(true, null, true, state.correctSequence.size());
        } else {
            // Wrong input, game over
            return new ChronomatronInputResult(true, null, false, state.correctSequence.size());
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
    
    private static int calculateChronomatronXpAward(int chainLength, String tier) {
        int baseXp = chainLength * 1500;
        String t = tier.toLowerCase();
        
        if (t.contains("high")) return baseXp;
        if (t.contains("grand")) return (int)(baseXp * 1.5);
        if (t.contains("supreme")) return baseXp * 2;
        if (t.contains("transcendent")) return (int)(baseXp * 2.5);
        if (t.contains("metaphysical")) return baseXp * 3;
        
        return baseXp;
    }
    
    private static int calculateBonusClicks(int pairsFound) {
        if (pairsFound >= 8) return 3;
        if (pairsFound >= 6) return 2;
        if (pairsFound >= 4) return 1;
        return 0;
    }
    
    private static int calculateChronomatronBonusClicks(int chainLength) {
        if (chainLength >= 9) return 2;
        if (chainLength >= 5) return 1;
        return 0;
    }
    
    private static int getInitialSequenceLength(String tier) {
        return 3;
    }
    
        private static int getColorCountForTier(String tier) {
        String t = tier.toLowerCase();
        if (t.contains("metaphysical")) return 10;  
        if (t.contains("transcendent")) return 8;  
        if (t.contains("supreme")) return 7;
        if (t.contains("grand")) return 5;
        if (t.contains("high")) return 3;
        return 3; 
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
        public final int sequenceLength;
        
        public ChronomatronInputResult(boolean success, String errorMessage, boolean correct, int sequenceLength) {
            this.success = success;
            this.errorMessage = errorMessage;
            this.correct = correct;
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
