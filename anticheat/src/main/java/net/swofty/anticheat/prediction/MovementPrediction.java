package net.swofty.anticheat.prediction;

import lombok.Data;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced movement prediction engine
 * Predicts all possible movements based on Minecraft physics
 */
public class MovementPrediction {

    // Minecraft physics constants
    private static final double GRAVITY = 0.08;
    private static final double DRAG = 0.98;
    private static final double SLIPPERINESS = 0.6;

    // Movement constants
    private static final double WALK_SPEED = 0.1;
    private static final double SPRINT_MULTIPLIER = 1.3;
    private static final double SNEAK_MULTIPLIER = 0.3;
    private static final double JUMP_VELOCITY = 0.42;

    // Strafe multipliers
    private static final double FORWARD_STRAFE = 0.98;
    private static final double DIAGONAL_STRAFE = 0.98 * 0.98; // cos(45°) * sin(45°)
    private static final double SIDE_STRAFE = 0.98;

    @Data
    public static class PredictedMovement {
        private final Pos position;
        private final Vel velocity;
        private final MovementInput input;
        private final double probability; // How likely this movement is
    }

    @Data
    public static class MovementInput {
        private final boolean forward;
        private final boolean backward;
        private final boolean left;
        private final boolean right;
        private final boolean jump;
        private final boolean sneak;
        private final boolean sprint;
    }

    @Data
    public static class PlayerState {
        private final Pos position;
        private final Vel velocity;
        private final boolean onGround;
        private final boolean inWater;
        private final boolean inLava;
        private final boolean inWeb;
        private final float friction; // Block friction (ice, slime, etc.)
        private final int jumpBoostLevel;
        private final int speedLevel;
        private final int slownessLevel;
    }

    /**
     * Predicts all possible movements for next tick
     * This is the core of the prediction engine
     */
    public static List<PredictedMovement> predictPossibleMovements(PlayerState state) {
        List<PredictedMovement> predictions = new ArrayList<>();

        // Generate all possible input combinations
        List<MovementInput> possibleInputs = generatePossibleInputs(state);

        for (MovementInput input : possibleInputs) {
            PredictedMovement prediction = simulateMovement(state, input);
            predictions.add(prediction);
        }

        return predictions;
    }

    /**
     * Generate all possible player inputs
     * In Minecraft, player can press WASD, space, shift, and sprint
     */
    private static List<MovementInput> generatePossibleInputs(PlayerState state) {
        List<MovementInput> inputs = new ArrayList<>();

        // Core movement combinations
        boolean[] booleans = {true, false};

        // We don't generate ALL 2^7 = 128 combinations as many are impossible
        // (e.g., forward + backward simultaneously)

        // No movement
        inputs.add(new MovementInput(false, false, false, false, false, false, false));

        // Forward/backward (mutually exclusive)
        for (boolean forward : new boolean[]{true, false}) {
            for (boolean backward : new boolean[]{false}) { // Can't go forward and backward
                if (forward && backward) continue;

                for (boolean left : booleans) {
                    for (boolean right : booleans) {
                        if (left && right) continue; // Can't go left and right simultaneously

                        // Only jump if on ground or in water
                        boolean[] jumpOptions = state.isOnGround() || state.isInWater()
                            ? new boolean[]{true, false}
                            : new boolean[]{false};

                        for (boolean jump : jumpOptions) {
                            for (boolean sneak : booleans) {
                                for (boolean sprint : booleans) {
                                    // Can't sprint while sneaking
                                    if (sprint && sneak) continue;
                                    // Can't sprint backward
                                    if (sprint && !forward) continue;
                                    // Can't sprint without moving forward
                                    if (sprint && !forward) continue;

                                    inputs.add(new MovementInput(
                                        forward, backward, left, right,
                                        jump, sneak, sprint
                                    ));
                                }
                            }
                        }
                    }
                }
            }
        }

        // Backward only
        inputs.add(new MovementInput(false, true, false, false, false, false, false));
        inputs.add(new MovementInput(false, true, true, false, false, false, false));
        inputs.add(new MovementInput(false, true, false, true, false, false, false));

        return inputs;
    }

    /**
     * Simulate movement for one tick with given input
     * This implements Minecraft's movement physics
     */
    private static PredictedMovement simulateMovement(PlayerState state, MovementInput input) {
        Vel currentVel = state.getVelocity();
        Pos currentPos = state.getPosition();

        // Calculate movement direction vector
        double forward = 0;
        double strafe = 0;

        if (input.isForward()) forward += 1.0;
        if (input.isBackward()) forward -= 1.0;
        if (input.isLeft()) strafe += 1.0;
        if (input.isRight()) strafe -= 1.0;

        // Normalize diagonal movement
        if (strafe != 0 || forward != 0) {
            double movementLength = Math.sqrt(forward * forward + strafe * strafe);
            forward /= movementLength;
            strafe /= movementLength;
        }

        // Apply speed effects
        double movementSpeed = WALK_SPEED;
        movementSpeed += state.getSpeedLevel() * 0.02; // Speed potion
        movementSpeed -= state.getSlownessLevel() * 0.015; // Slowness potion

        // Apply movement modifiers
        if (input.isSprint()) {
            movementSpeed *= SPRINT_MULTIPLIER;
        }
        if (input.isSneak()) {
            movementSpeed *= SNEAK_MULTIPLIER;
        }

        // Calculate acceleration
        double acceleration = state.isOnGround() ? movementSpeed * 0.1 : 0.02; // Air vs ground acceleration

        if (state.isInWater() || state.isInLava()) {
            acceleration = 0.02; // Fluid acceleration
        }

        if (state.isInWeb()) {
            acceleration *= 0.25; // Web slows you down
        }

        // Convert movement to world coordinates (rotate by player's yaw)
        double yawRad = Math.toRadians(currentPos.yaw());
        double sin = Math.sin(yawRad);
        double cos = Math.cos(yawRad);

        double motionX = (strafe * cos - forward * sin) * acceleration;
        double motionZ = (forward * cos + strafe * sin) * acceleration;

        // Add to current velocity
        double newVelX = currentVel.x() + motionX;
        double newVelZ = currentVel.z() + motionZ;
        double newVelY = currentVel.y();

        // Apply jumping
        if (input.isJump() && state.isOnGround()) {
            newVelY = JUMP_VELOCITY;
            newVelY += state.getJumpBoostLevel() * 0.1; // Jump boost effect

            // Sprint jumping gives extra velocity
            if (input.isSprint()) {
                newVelX += -sin * 0.2;
                newVelZ += cos * 0.2;
            }
        }

        // Apply gravity
        if (!state.isOnGround()) {
            newVelY -= GRAVITY;
            newVelY *= DRAG; // Air resistance
        }

        // Apply friction
        if (state.isOnGround()) {
            double friction = SLIPPERINESS * 0.91 * state.getFriction();
            newVelX *= friction;
            newVelZ *= friction;
        } else {
            // Air friction
            newVelX *= 0.91;
            newVelZ *= 0.91;
        }

        // Apply fluid friction
        if (state.isInWater()) {
            newVelX *= 0.8;
            newVelY *= 0.8;
            newVelZ *= 0.8;
        }

        if (state.isInLava()) {
            newVelX *= 0.5;
            newVelY *= 0.5;
            newVelZ *= 0.5;
        }

        // Calculate new position
        Vel newVel = new Vel(newVelX, newVelY, newVelZ);
        Pos newPos = currentPos.add(newVelX, newVelY, newVelZ);

        // Calculate probability (how likely this input combination is)
        double probability = calculateInputProbability(input, state);

        return new PredictedMovement(newPos, newVel, input, probability);
    }

    /**
     * Calculate how likely a given input is
     * Used for weighting predictions
     */
    private static double calculateInputProbability(MovementInput input, PlayerState state) {
        // Most common: forward movement
        if (input.isForward() && !input.isLeft() && !input.isRight()) {
            return 0.4; // 40% chance
        }

        // Common: strafing while moving forward
        if (input.isForward() && (input.isLeft() || input.isRight())) {
            return 0.25; // 25% chance
        }

        // Less common: side-only strafing
        if (!input.isForward() && !input.isBackward() && (input.isLeft() || input.isRight())) {
            return 0.15; // 15% chance
        }

        // Uncommon: backward movement
        if (input.isBackward()) {
            return 0.08; // 8% chance
        }

        // Rare: no movement
        if (!input.isForward() && !input.isBackward() && !input.isLeft() && !input.isRight()) {
            return 0.05; // 5% chance
        }

        return 0.01; // Very rare inputs
    }

    /**
     * Find the closest predicted movement to actual movement
     * Returns the offset (how far off the actual movement was)
     */
    public static PredictionResult compareToActual(List<PredictedMovement> predictions,
                                                   Pos actualPos, Vel actualVel) {
        double minOffset = Double.MAX_VALUE;
        PredictedMovement closestPrediction = null;

        for (PredictedMovement prediction : predictions) {
            // Calculate 3D distance offset
            double posOffset = calculateOffset(prediction.getPosition(), actualPos);
            double velOffset = calculateOffset(
                prediction.getVelocity().asPosition(),
                actualVel.asPosition()
            );

            // Combined offset (weighted more toward position)
            double totalOffset = posOffset * 0.7 + velOffset * 0.3;

            if (totalOffset < minOffset) {
                minOffset = totalOffset;
                closestPrediction = prediction;
            }
        }

        return new PredictionResult(closestPrediction, minOffset);
    }

    private static double calculateOffset(Pos pos1, Pos pos2) {
        double dx = pos1.x() - pos2.x();
        double dy = pos1.y() - pos2.y();
        double dz = pos1.z() - pos2.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Data
    public static class PredictionResult {
        private final PredictedMovement closestPrediction;
        private final double offset; // How far off the actual movement was

        /**
         * Returns true if movement is impossible (too far from any prediction)
         */
        public boolean isImpossible(double threshold) {
            return offset > threshold;
        }

        /**
         * Get certainty that this is cheating (0.0 to 1.0)
         * Based on how far off from predictions
         */
        public double getCertainty() {
            // Even 0.01 block offset is suspicious due to prediction precision
            if (offset < 0.001) return 0.0; // Within margin of error
            if (offset < 0.01) return 0.3;   // Slightly suspicious
            if (offset < 0.05) return 0.6;   // Moderately suspicious
            if (offset < 0.1) return 0.8;    // Very suspicious
            return 0.95;                      // Extremely suspicious
        }
    }
}
