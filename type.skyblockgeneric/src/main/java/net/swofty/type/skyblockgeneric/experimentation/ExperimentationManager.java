package net.swofty.type.skyblockgeneric.experimentation;

import net.swofty.commons.StringUtility;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointExperimentation;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public final class ExperimentationManager {
    private static final Map<UUID, GameSession> SESSIONS = new ConcurrentHashMap<>();

    private ExperimentationManager() {
    }

    public static boolean canStart(SkyBlockPlayer player, ExperimentTier tier) {
        return !SESSIONS.containsKey(player.getUuid()) && tier.isUnlocked(player);
    }

    public static String requirementMessage(ExperimentTier tier) {
        return "§cYou need Enchanting " + StringUtility.getAsRomanNumeral(tier.requiredEnchantingLevel())
                + " to play this experiment.";
    }

    public static boolean start(SkyBlockPlayer player, ExperimentType type, ExperimentTier tier) {
        if (!tier.isUnlocked(player)) return false;

        GameSession session = new GameSession(player.getUuid(), type, tier);
        if (SESSIONS.putIfAbsent(player.getUuid(), session) != null) return false;

        try {
            if (type == ExperimentType.SUPERPAIRS) {
                GameSession.SuperPairsState state = (GameSession.SuperPairsState) session.state();
                int bonusClicks = getBonusClicks(player);
                state.clicksRemaining(state.totalClicks() + bonusClicks);
                resetBonusClicks(player);
                List<SuperPairItem> items = new ArrayList<>();
                for (SuperPairItem item : SuperPairItem.values()) {
                    items.add(item);
                    items.add(item);
                }
                Collections.shuffle(items);
                state.board().addAll(items);
            }
            return true;
        } catch (RuntimeException exception) {
            SESSIONS.remove(player.getUuid(), session);
            throw exception;
        }
    }

    public static GameSession.ChronomatronState getChronomatronState(SkyBlockPlayer player) {
        GameSession session = SESSIONS.get(player.getUuid());
        return session != null && session.type() == ExperimentType.CHRONOMATRON
                ? (GameSession.ChronomatronState) session.state() : null;
    }

    public static GameSession.UltraSequencerState getUltraSequencerState(SkyBlockPlayer player) {
        GameSession session = SESSIONS.get(player.getUuid());
        return session != null && session.type() == ExperimentType.ULTRASEQUENCER
                ? (GameSession.UltraSequencerState) session.state() : null;
    }

    public static GameSession.SuperPairsState getSuperPairsState(SkyBlockPlayer player) {
        GameSession session = SESSIONS.get(player.getUuid());
        if (session == null || session.type() != ExperimentType.SUPERPAIRS) return null;

        GameSession.SuperPairsState state = (GameSession.SuperPairsState) session.state();
        expireMismatch(state);
        return state;
    }

    public static boolean startChronomatronRound(SkyBlockPlayer player) {
        GameSession.ChronomatronState state = getChronomatronState(player);
        if (state == null || state.phase() != GameSession.GamePhase.READY) return false;

        int length = state.sequence().isEmpty() ? 3 : state.sequence().size() + 1;
        for (int i = state.sequence().size(); i < length; i++) {
            state.sequence().add(ThreadLocalRandom.current().nextInt(playerTier(player).colorCount()));
        }
        state.inputIndex(0);
        state.deadline(0);
        state.phase(GameSession.GamePhase.WATCHING);
        return true;
    }

    public static void chronomatronSequenceShown(SkyBlockPlayer player) {
        GameSession session = SESSIONS.get(player.getUuid());
        GameSession.ChronomatronState state = getChronomatronState(player);
        if (state != null && state.phase() == GameSession.GamePhase.WATCHING) {
            state.phase(GameSession.GamePhase.PLAYING);
            state.deadline(System.currentTimeMillis() + secondsForTier(session.tier()) * 1_000L);
        }
    }

    public static ChronomatronInputResult inputChronomatron(SkyBlockPlayer player, int color) {
        GameSession session = SESSIONS.get(player.getUuid());
        if (session == null || session.type() != ExperimentType.CHRONOMATRON) {
            return new ChronomatronInputResult(false, "No active Chronomatron session.", false, false);
        }

        GameSession.ChronomatronState state = (GameSession.ChronomatronState) session.state();
        if (state.phase() != GameSession.GamePhase.PLAYING) {
            return new ChronomatronInputResult(false, "The sequence is still being shown.", false, false);
        }
        if (color < 0 || color >= session.tier().colorCount()) {
            return new ChronomatronInputResult(false, "That color is not part of this experiment.", false, false);
        }

        long now = System.currentTimeMillis();
        if (state.deadline() > 0 && now >= state.deadline()) {
            state.phase(GameSession.GamePhase.COMPLETE);
            return new ChronomatronInputResult(true, null, false, false);
        }
        if (now - state.lastInput() < 150) {
            return new ChronomatronInputResult(false, "Please slow down.", false, false);
        }
        state.lastInput(now);

        boolean correct = state.sequence().get(state.inputIndex()) == color;
        if (!correct) {
            state.phase(GameSession.GamePhase.COMPLETE);
            return new ChronomatronInputResult(true, null, false, false);
        }

        state.inputIndex(state.inputIndex() + 1);
        if (state.inputIndex() < state.sequence().size()) {
            return new ChronomatronInputResult(true, null, true, false);
        }

        session.bestScore(state.sequence().size());
        state.phase(GameSession.GamePhase.READY);
        return new ChronomatronInputResult(true, null, true, true);
    }

    public static ChronomatronFinishResult finishChronomatron(SkyBlockPlayer player) {
        GameSession session = SESSIONS.remove(player.getUuid());
        if (session == null || session.type() != ExperimentType.CHRONOMATRON) {
            return new ChronomatronFinishResult(false, "No active Chronomatron session.", 0, 0, 0);
        }

        int score = session.bestScore();
        int xp = Math.min(score, 15) * session.tier().xpPerStep();
        int bonus = bonusClicksForScore(score);
        award(player, xp, bonus);
        return new ChronomatronFinishResult(true, null, score, xp, bonus);
    }

    public static boolean startUltraSequencerRound(SkyBlockPlayer player) {
        GameSession.UltraSequencerState state = getUltraSequencerState(player);
        if (state == null || state.phase() != GameSession.GamePhase.READY) return false;

        int length = state.sequence().isEmpty() ? 3 : state.sequence().size() + 1;
        while (state.sequence().size() < length) {
            state.sequence().add(ThreadLocalRandom.current().nextInt(1, 10));
        }
        state.inputIndex(0);
        state.deadline(0);
        state.phase(GameSession.GamePhase.WATCHING);
        return true;
    }

    public static void ultraSequencerShown(SkyBlockPlayer player) {
        GameSession session = SESSIONS.get(player.getUuid());
        GameSession.UltraSequencerState state = getUltraSequencerState(player);
        if (state != null && state.phase() == GameSession.GamePhase.WATCHING) {
            state.phase(GameSession.GamePhase.PLAYING);
            state.deadline(System.currentTimeMillis() + secondsForTier(session.tier()) * 1_000L);
        }
    }

    public static UltraSequencerInputResult inputUltraSequencer(SkyBlockPlayer player, int number) {
        GameSession session = SESSIONS.get(player.getUuid());
        if (session == null || session.type() != ExperimentType.ULTRASEQUENCER) {
            return new UltraSequencerInputResult(false, "No active Ultrasequencer session.", false, false);
        }

        GameSession.UltraSequencerState state = (GameSession.UltraSequencerState) session.state();
        if (state.phase() != GameSession.GamePhase.PLAYING) {
            return new UltraSequencerInputResult(false, "The sequence is still being shown.", false, false);
        }
        if (number < 1 || number > 9) {
            return new UltraSequencerInputResult(false, "That number is not on the board.", false, false);
        }
        if (state.deadline() > 0 && System.currentTimeMillis() >= state.deadline()) {
            state.phase(GameSession.GamePhase.COMPLETE);
            return new UltraSequencerInputResult(true, null, false, false);
        }

        boolean correct = state.sequence().get(state.inputIndex()) == number;
        if (!correct) {
            state.phase(GameSession.GamePhase.COMPLETE);
            return new UltraSequencerInputResult(true, null, false, false);
        }

        state.inputIndex(state.inputIndex() + 1);
        if (state.inputIndex() < state.sequence().size()) {
            return new UltraSequencerInputResult(true, null, true, false);
        }

        session.bestScore(state.sequence().size());
        state.phase(GameSession.GamePhase.READY);
        return new UltraSequencerInputResult(true, null, true, true);
    }

    public static UltraSequencerFinishResult finishUltraSequencer(SkyBlockPlayer player) {
        GameSession session = SESSIONS.remove(player.getUuid());
        if (session == null || session.type() != ExperimentType.ULTRASEQUENCER) {
            return new UltraSequencerFinishResult(false, "No active Ultrasequencer session.", 0, 0, 0);
        }

        int score = session.bestScore();
        int xp = Math.min(score, 20) * session.tier().xpPerStep();
        int bonus = bonusClicksForScore(score);
        award(player, xp, bonus);
        return new UltraSequencerFinishResult(true, null, score, xp, bonus);
    }

    public static SuperPairsFlipResult flipSuperPair(SkyBlockPlayer player, int tile) {
        GameSession session = SESSIONS.get(player.getUuid());
        if (session == null || session.type() != ExperimentType.SUPERPAIRS) {
            return new SuperPairsFlipResult(false, "No active Superpairs session.", false, false, -1, -1);
        }

        GameSession.SuperPairsState state = (GameSession.SuperPairsState) session.state();
        expireMismatch(state);
        if (state.clicksRemaining() <= 0) {
            return new SuperPairsFlipResult(false, "You have no clicks remaining.", false, false, -1, -1);
        }
        if (state.mismatchUntil() > System.currentTimeMillis()) {
            return new SuperPairsFlipResult(false, "Wait for the tiles to turn over.", false, false, -1, -1);
        }
        if (tile < 0 || tile >= state.board().size() || state.matchedTiles().contains(tile) || state.firstFlip() == tile) {
            return new SuperPairsFlipResult(false, "That tile cannot be flipped.", false, false, -1, -1);
        }

        state.clicksRemaining(state.clicksRemaining() - 1);
        if (state.firstFlip() < 0) {
            state.firstFlip(tile);
            return new SuperPairsFlipResult(true, null, false, false, tile, -1);
        }

        int first = state.firstFlip();
        state.firstFlip(-1);
        boolean match = state.board().get(first) == state.board().get(tile);
        if (match) {
            state.matchedTiles().add(first);
            state.matchedTiles().add(tile);
            return new SuperPairsFlipResult(true, null, true, state.pairsFound() == SuperPairItem.values().length, first, tile);
        }

        state.mismatchFirst(first);
        state.mismatchSecond(tile);
        state.mismatchUntil(System.currentTimeMillis() + 750);
        return new SuperPairsFlipResult(true, null, false, false, first, tile);
    }

    public static SuperPairsFinishResult finishSuperPairs(SkyBlockPlayer player) {
        GameSession session = SESSIONS.remove(player.getUuid());
        if (session == null || session.type() != ExperimentType.SUPERPAIRS) {
            return new SuperPairsFinishResult(false, "No active Superpairs session.", 0, 0, 0);
        }

        GameSession.SuperPairsState state = (GameSession.SuperPairsState) session.state();
        int pairs = state.pairsFound();
        int xp = pairs * session.tier().superPairsXpPerPair();
        int bonus = pairs >= 8 ? 3 : pairs >= 6 ? 2 : pairs >= 4 ? 1 : 0;
        award(player, xp, bonus);
        return new SuperPairsFinishResult(true, null, pairs, xp, bonus);
    }

    public static void cancel(SkyBlockPlayer player, ExperimentType type) {
        SESSIONS.computeIfPresent(player.getUuid(), (uuid, session) -> session.type() == type ? null : session);
    }

    private static ExperimentTier playerTier(SkyBlockPlayer player) {
        GameSession session = SESSIONS.get(player.getUuid());
        return session == null ? ExperimentTier.HIGH : session.tier();
    }

    private static void expireMismatch(GameSession.SuperPairsState state) {
        if (state.mismatchUntil() != 0 && state.mismatchUntil() <= System.currentTimeMillis()) {
            state.mismatchFirst(-1);
            state.mismatchSecond(-1);
            state.mismatchUntil(0);
        }
    }

    private static int bonusClicksForScore(int score) {
        return score >= 9 ? 3 : score >= 7 ? 2 : score >= 5 ? 1 : 0;
    }

    private static int secondsForTier(ExperimentTier tier) {
        return switch (tier) {
            case HIGH -> 20;
            case GRAND -> 18;
            case SUPREME -> 16;
            case TRANSCENDENT -> 14;
            case METAPHYSICAL -> 12;
        };
    }

    private static int getBonusClicks(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler()
                .get(SkyBlockDataHandler.Data.EXPERIMENTATION, DatapointExperimentation.class)
                .getValue().superpairsBonusClicks();
    }

    private static void resetBonusClicks(SkyBlockPlayer player) {
        player.getSkyblockDataHandler()
                .get(SkyBlockDataHandler.Data.EXPERIMENTATION, DatapointExperimentation.class)
                .setValue(new DatapointExperimentation.PlayerExperimentation(0));
    }

    private static void award(SkyBlockPlayer player, int xp, int bonusClicks) {
        if (xp > 0) player.getSkills().increase(player, SkillCategories.ENCHANTING, (double) xp);
        if (bonusClicks <= 0) return;

        DatapointExperimentation datapoint = player.getSkyblockDataHandler()
                .get(SkyBlockDataHandler.Data.EXPERIMENTATION, DatapointExperimentation.class);
        int current = datapoint.getValue().superpairsBonusClicks();
        datapoint.setValue(new DatapointExperimentation.PlayerExperimentation(current + bonusClicks));
    }

    public record ChronomatronInputResult(boolean success, String errorMessage, boolean correct, boolean complete) {
    }

    public record ChronomatronFinishResult(boolean success, String errorMessage, int bestChain, int xpAward,
                                           int bonusClicksEarned) {
    }

    public record UltraSequencerInputResult(boolean success, String errorMessage, boolean correct, boolean complete) {
    }

    public record UltraSequencerFinishResult(boolean success, String errorMessage, int bestSeriesLength, int xpAward,
                                             int bonusClicksEarned) {
    }

    public record SuperPairsFlipResult(boolean success, String errorMessage, boolean match, boolean complete,
                                       int firstTile, int secondTile) {
    }

    public record SuperPairsFinishResult(boolean success, String errorMessage, int pairsFound, int xpAward,
                                         int bonusClicksEarned) {
    }
}
