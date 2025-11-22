package net.swofty.anticheat.prediction;

import lombok.Builder;
import lombok.Data;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;
import net.swofty.anticheat.world.Block;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Complete context about a player's state for predictions
 * This includes position, velocity, effects, blocks, ping, etc.
 */
@Data
@Builder
public class PlayerContext {
    // Position and movement
    private final Pos position;
    private final Vel velocity;
    private final boolean onGround;
    private final boolean wasOnGround; // Previous tick

    // Environment
    private final Block blockAt; // Block at player position
    private final Block blockBelow; // Block below player
    private final Block blockAbove; // Block above player
    private final boolean inWater;
    private final boolean inLava;
    private final boolean inBubbleColumn;
    private final boolean inCobweb;
    private final boolean onSoulSand;
    private final boolean onIce;
    private final boolean onSlime;
    private final boolean onHoney;

    // Effects
    private final int speedLevel;
    private final int slownessLevel;
    private final int jumpBoostLevel;
    private final int levitationLevel;
    private final int slowFallingLevel;
    private final boolean hasDolphinsGrace;

    // Equipment
    private final int depthStriderLevel;
    private final int soulSpeedLevel;
    private final boolean hasFrostWalker;

    // Player state
    private final boolean sprinting;
    private final boolean sneaking;
    private final boolean swimming;
    private final boolean gliding; // Elytra
    private final boolean flying; // Creative/spectator
    private final boolean usingItem; // Eating, blocking, etc.

    // Latency compensation
    private final long ping; // Current ping in ms
    private final long transactionPing; // Transaction-based ping
    private final int skippedTicks; // Ticks missed due to lag

    // Knockback tracking
    private final Vel expectedKnockback; // If player was recently hit
    private final int ticksSinceKnockback;

    // Custom data for extensibility
    @Builder.Default
    private final Map<String, Object> customData = new ConcurrentHashMap<>();

    /**
     * Get custom data by key
     */
    @SuppressWarnings("unchecked")
    public <T> T getCustomData(String key) {
        return (T) customData.get(key);
    }

    /**
     * Set custom data
     */
    public void setCustomData(String key, Object value) {
        customData.put(key, value);
    }
}
