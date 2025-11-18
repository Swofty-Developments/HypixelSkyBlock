package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.PlayerTickInformation;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.PlayerContext;
import net.swofty.anticheat.prediction.PredictionEngine;
import net.swofty.anticheat.prediction.PredictionEngine.PredictedMovement;
import net.swofty.anticheat.prediction.compensation.LagCompensator;
import net.swofty.anticheat.prediction.modifier.impl.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Modular prediction-based movement check
 * Uses abstract modifiers for extensibility and proper lag compensation
 */
public class PredictionFlag extends Flag {

    // Shared prediction engine (registered modifiers are reused)
    private static final PredictionEngine engine = new PredictionEngine();
    private static final LagCompensator lagCompensator = new LagCompensator();

    // Track transaction IDs per player
    private static final Map<UUID, Integer> currentTransactions = new HashMap<>();

    static {
        // Register all velocity modifiers
        engine.registerVelocityModifier(new SprintModifier());
        engine.registerVelocityModifier(new SpeedEffectModifier());
        engine.registerVelocityModifier(new SlownessEffectModifier());
        engine.registerVelocityModifier(new SoulSpeedModifier());
        engine.registerVelocityModifier(new DepthStriderModifier());
        engine.registerVelocityModifier(new DolphinsGraceModifier());

        // Register all friction modifiers
        engine.registerFrictionModifier(new IceFrictionModifier());
        engine.registerFrictionModifier(new SlimeFrictionModifier());

        // More modifiers can be added dynamically as needed
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();
        PlayerTickInformation currentTick = event.getCurrentTick();
        PlayerTickInformation previousTick = event.getPreviousTick();

        if (previousTick == null) return;

        UUID playerId = player.getUuid();
        long ping = player.getPing();

        // Check if we should skip due to lag
        if (lagCompensator.shouldSkipDueToLag(playerId, ping)) {
            return; // Too laggy, can't predict accurately
        }

        // Build player context with all available information
        PlayerContext context = buildPlayerContext(player, previousTick);

        // Record snapshot for lag compensation
        int transactionId = currentTransactions.getOrDefault(playerId, 0) + 1;
        currentTransactions.put(playerId, transactionId);
        lagCompensator.recordSnapshot(
            playerId,
            transactionId,
            previousTick.getPos(),
            previousTick.getVel(),
            System.currentTimeMillis()
        );

        // Predict all possible movements
        List<PredictedMovement> predictions = engine.predictMovements(context);

        // Find closest prediction to actual movement
        Pos actualPos = currentTick.getPos();
        Vel actualVel = currentTick.getVel();

        PredictionResult result = findClosestPrediction(predictions, actualPos, actualVel);

        // Apply lag compensation
        double compensationOffset = lagCompensator.getCompensationOffset(ping, context.getVelocity());
        double effectiveOffset = Math.max(0, result.offset - compensationOffset);

        // Calculate certainty based on effective offset
        double certainty = calculateCertainty(effectiveOffset);

        // Flag if suspicious
        if (certainty > 0.4) { // Threshold for flagging
            player.flag(net.swofty.anticheat.flag.FlagType.PREDICTION, certainty);
        }
    }

    /**
     * Build comprehensive player context
     */
    private PlayerContext buildPlayerContext(SwoftyPlayer player, PlayerTickInformation tick) {
        // TODO: In a full implementation, these would be populated from actual game state
        // For now, using defaults with the framework in place

        return PlayerContext.builder()
            .position(tick.getPos())
            .velocity(tick.getVel())
            .onGround(tick.isOnGround())
            .wasOnGround(tick.getPrevious() != null && tick.getPrevious().isOnGround())

            // Environment (TODO: detect from world)
            .blockAt(null)
            .blockBelow(null)
            .blockAbove(null)
            .inWater(false)
            .inLava(false)
            .inBubbleColumn(false)
            .inCobweb(false)
            .onSoulSand(false)
            .onIce(false)
            .onSlime(false)
            .onHoney(false)

            // Effects (TODO: get from player)
            .speedLevel(0)
            .slownessLevel(0)
            .jumpBoostLevel(0)
            .levitationLevel(0)
            .slowFallingLevel(0)
            .hasDolphinsGrace(false)

            // Equipment (TODO: get from player)
            .depthStriderLevel(0)
            .soulSpeedLevel(0)
            .hasFrostWalker(false)

            // Player state (TODO: get from player)
            .sprinting(false)
            .sneaking(false)
            .swimming(false)
            .gliding(false)
            .flying(false)
            .usingItem(false)

            // Latency compensation
            .ping(player.getPing())
            .transactionPing(0) // TODO: calculate from transaction system
            .skippedTicks(0)

            // Knockback (TODO: track knockback events)
            .expectedKnockback(new Vel(0, 0, 0))
            .ticksSinceKnockback(999)

            .build();
    }

    /**
     * Find closest prediction to actual movement
     */
    private PredictionResult findClosestPrediction(List<PredictedMovement> predictions,
                                                    Pos actualPos, Vel actualVel) {
        double minOffset = Double.MAX_VALUE;
        PredictedMovement closest = null;

        for (PredictedMovement prediction : predictions) {
            double posOffset = calculateDistance(prediction.getPosition(), actualPos);
            double velOffset = calculateDistance(
                prediction.getVelocity().asPosition(),
                actualVel.asPosition()
            );

            // Weight position more heavily than velocity
            double totalOffset = posOffset * 0.8 + velOffset * 0.2;

            // Add prediction uncertainty
            totalOffset -= prediction.getUncertainty();

            if (totalOffset < minOffset) {
                minOffset = totalOffset;
                closest = prediction;
            }
        }

        return new PredictionResult(closest, Math.max(0, minOffset));
    }

    private double calculateDistance(Pos pos1, Pos pos2) {
        double dx = pos1.x() - pos2.x();
        double dy = pos1.y() - pos2.y();
        double dz = pos1.z() - pos2.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Calculate certainty based on offset from predictions
     */
    private double calculateCertainty(double offset) {
        // With proper modifiers and lag compensation, we can be very precise
        if (offset < 0.0001) return 0.0; // Perfect match
        if (offset < 0.001) return 0.1;   // Rounding error
        if (offset < 0.01) return 0.4;    // Slightly suspicious
        if (offset < 0.05) return 0.7;    // Moderately suspicious
        if (offset < 0.1) return 0.85;    // Very suspicious
        return 0.95;                       // Extremely suspicious / blatant
    }

    private static class PredictionResult {
        final PredictedMovement prediction;
        final double offset;

        PredictionResult(PredictedMovement prediction, double offset) {
            this.prediction = prediction;
            this.offset = offset;
        }
    }
}
