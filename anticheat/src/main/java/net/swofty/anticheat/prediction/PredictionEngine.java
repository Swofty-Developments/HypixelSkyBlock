package net.swofty.anticheat.prediction;

import lombok.Data;
import net.swofty.anticheat.api.AnticheatAPI;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.prediction.modifier.FrictionModifier;
import net.swofty.anticheat.prediction.modifier.VelocityModifier;

import java.util.*;

/**
 * Modular prediction engine that uses abstract modifiers
 * This allows for extensible and accurate movement prediction
 */
public class PredictionEngine {

    // Registered modifiers
    private final List<VelocityModifier> velocityModifiers = new ArrayList<>();
    private final List<FrictionModifier> frictionModifiers = new ArrayList<>();

    // Physics constants
    private static final double GRAVITY = 0.08;
    private static final double DRAG = 0.98;
    private static final double DEFAULT_FRICTION = 0.6f;
    private static final double JUMP_VELOCITY = 0.42;

    public PredictionEngine() {
        // Modifiers are registered externally for full flexibility
    }

    /**
     * Register a velocity modifier
     */
    public void registerVelocityModifier(VelocityModifier modifier) {
        velocityModifiers.add(modifier);
        velocityModifiers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
    }

    /**
     * Register a friction modifier
     */
    public void registerFrictionModifier(FrictionModifier modifier) {
        frictionModifiers.add(modifier);
        frictionModifiers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
    }

    /**
     * Predict all possible movements for next tick
     */
    public List<PredictedMovement> predictMovements(PlayerContext context) {
        List<PredictedMovement> predictions = new ArrayList<>();

        // Generate all possible inputs
        List<PlayerInput> possibleInputs = generatePossibleInputs(context);

        for (PlayerInput input : possibleInputs) {
            PredictedMovement prediction = simulateMovement(context, input);

            // Add uncertainty for ping/lag
            prediction.addUncertainty(calculatePingUncertainty(context));

            predictions.add(prediction);
        }

        return predictions;
    }

    /**
     * Simulate one tick of movement with given input
     */
    private PredictedMovement simulateMovement(PlayerContext context, PlayerInput input) {
        Vel currentVel = context.getVelocity();
        Pos currentPos = context.getPosition();

        // Calculate base movement from input
        Vel movementVel = calculateInputMovement(context, input);

        // Apply all velocity modifiers in priority order (including custom modifiers from API)
        List<VelocityModifier> allVelocityModifiers = new ArrayList<>(velocityModifiers);
        allVelocityModifiers.addAll(AnticheatAPI.getModifierRegistry().getCustomVelocityModifiers());
        allVelocityModifiers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));

        for (VelocityModifier modifier : allVelocityModifiers) {
            if (modifier.shouldApply(context)) {
                movementVel = modifier.apply(movementVel, context);
            }
        }

        // Add movement to current velocity
        Vel newVel = new Vel(
            currentVel.x() + movementVel.x(),
            currentVel.y() + movementVel.y(),
            currentVel.z() + movementVel.z()
        );

        // Apply jumping
        if (input.isJump() && context.isOnGround()) {
            double jumpVel = JUMP_VELOCITY;
            jumpVel += context.getJumpBoostLevel() * 0.1;
            newVel = newVel.withY(jumpVel);

            // Sprint jump boost
            if (context.isSprinting()) {
                double yawRad = Math.toRadians(currentPos.yaw());
                newVel = new Vel(
                    newVel.x() - Math.sin(yawRad) * 0.2,
                    newVel.y(),
                    newVel.z() + Math.cos(yawRad) * 0.2
                );
            }
        }

        // Apply gravity
        if (!context.isOnGround() && !context.isFlying()) {
            newVel = newVel.withY(newVel.y() - GRAVITY);
            newVel = newVel.withY(newVel.y() * DRAG);
        }

        // Apply friction
        float friction = getFriction(context);
        if (context.isOnGround()) {
            newVel = new Vel(
                newVel.x() * friction * 0.91,
                newVel.y(),
                newVel.z() * friction * 0.91
            );
        } else {
            newVel = new Vel(
                newVel.x() * 0.91,
                newVel.y(),
                newVel.z() * 0.91
            );
        }

        // Apply fluid friction
        if (context.isInWater()) {
            newVel = newVel.mul(0.8);
        }
        if (context.isInLava()) {
            newVel = newVel.mul(0.5);
        }
        if (context.isInCobweb()) {
            newVel = newVel.mul(0.25);
        }

        // Calculate new position
        Pos newPos = currentPos.add(newVel.x(), newVel.y(), newVel.z());

        return new PredictedMovement(newPos, newVel, input);
    }

    /**
     * Get friction from modifiers (including custom modifiers from API)
     */
    private float getFriction(PlayerContext context) {
        List<FrictionModifier> allFrictionModifiers = new ArrayList<>(frictionModifiers);
        allFrictionModifiers.addAll(AnticheatAPI.getModifierRegistry().getCustomFrictionModifiers());
        allFrictionModifiers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));

        for (FrictionModifier modifier : allFrictionModifiers) {
            if (modifier.shouldApply(context)) {
                return modifier.getFriction(context);
            }
        }
        return (float) DEFAULT_FRICTION;
    }

    /**
     * Calculate movement vector from player input
     */
    private Vel calculateInputMovement(PlayerContext context, PlayerInput input) {
        double forward = 0;
        double strafe = 0;

        if (input.isForward()) forward += 1.0;
        if (input.isBackward()) forward -= 1.0;
        if (input.isLeft()) strafe += 1.0;
        if (input.isRight()) strafe -= 1.0;

        // Normalize diagonal
        if (strafe != 0 || forward != 0) {
            double length = Math.sqrt(forward * forward + strafe * strafe);
            forward /= length;
            strafe /= length;
        }

        // Base movement speed
        double movementSpeed = 0.1;

        // Sneak modifier
        if (input.isSneak()) {
            movementSpeed *= 0.3;
        }

        // Acceleration (ground vs air)
        double acceleration = context.isOnGround() ? movementSpeed * 0.1 : 0.02;

        // Rotate by yaw
        double yawRad = Math.toRadians(context.getPosition().yaw());
        double sin = Math.sin(yawRad);
        double cos = Math.cos(yawRad);

        double motionX = (strafe * cos - forward * sin) * acceleration;
        double motionZ = (forward * cos + strafe * sin) * acceleration;

        return new Vel(motionX, 0, motionZ);
    }

    /**
     * Generate all possible inputs accounting for lag
     */
    private List<PlayerInput> generatePossibleInputs(PlayerContext context) {
        List<PlayerInput> inputs = new ArrayList<>();

        // If player has skipped ticks due to lag, we need to account for that
        int ticksToPredict = 1 + context.getSkippedTicks();

        // Generate base inputs (simplified - full version would be more extensive)
        boolean[] bools = {true, false};

        for (boolean forward : bools) {
            for (boolean left : bools) {
                for (boolean right : bools) {
                    if (left && right) continue;

                    boolean[] jumpOpts = context.isOnGround() ? bools : new boolean[]{false};
                    for (boolean jump : jumpOpts) {
                        inputs.add(new PlayerInput(forward, false, left, right, jump, false));
                    }
                }
            }
        }

        // Add backward
        inputs.add(new PlayerInput(false, true, false, false, false, false));

        return inputs;
    }

    /**
     * Calculate uncertainty radius based on ping
     */
    private double calculatePingUncertainty(PlayerContext context) {
        // Higher ping = more uncertainty
        // Use transaction ping if available (more accurate)
        long ping = context.getTransactionPing() > 0
            ? context.getTransactionPing()
            : context.getPing();

        // Every 50ms of ping adds ~0.01 blocks of uncertainty
        return (ping / 50.0) * 0.01;
    }

    @Data
    public static class PredictedMovement {
        private final Pos position;
        private final Vel velocity;
        private final PlayerInput input;
        private double certainty;
        private double uncertainty = 0.0;

        public void addUncertainty(double amount) {
            this.uncertainty += amount;
        }
    }

    @Data
    public static class PlayerInput {
        private final boolean forward;
        private final boolean backward;
        private final boolean left;
        private final boolean right;
        private final boolean jump;
        private final boolean sneak;
    }
}
