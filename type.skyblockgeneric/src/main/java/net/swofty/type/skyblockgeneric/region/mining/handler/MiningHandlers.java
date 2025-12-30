package net.swofty.type.skyblockgeneric.region.mining.handler;

/**
 * Factory methods for creating mining handlers.
 * Provides a clean API for MineableBlock enum.
 */
public final class MiningHandlers {
    private MiningHandlers() {} // Utility class

    /**
     * Create a pickaxe mining handler.
     * @param strength Block hardness for mining time calculation
     * @param powerRequirement Minimum breaking power required
     */
    public static SkyBlockMiningHandler pickaxe(double strength, int powerRequirement) {
        return new PickaxeMiningHandler(strength, powerRequirement);
    }

    public static SkyBlockMiningHandler hand(double strength, int powerRequirement) {
        return new PickaxeMiningHandler(strength, powerRequirement);
    }

    /**
     * Create an axe mining handler.
     * Uses FORAGING_SPEED stat for axes, fixed time for bare hands.
     * @param strength Block hardness for foraging speed calculation
     * @param handBreakTimeTicks time in ticks to break with bare hands (20 ticks = 1 second)
     */
    public static SkyBlockMiningHandler axe(double strength, int handBreakTimeTicks) {
        return new AxeMiningHandler(strength, handBreakTimeTicks);
    }

    /**
     * Create a hoe mining handler.
     * @param strength Block hardness for mining time calculation
     */
    public static SkyBlockMiningHandler hoe(double strength) {
        return new HoeMiningHandler(strength);
    }

    /**
     * Get the instant break handler singleton.
     * Used for blocks that break instantly with any tool.
     */
    public static SkyBlockMiningHandler instant() {
        return InstantBreakHandler.INSTANCE;
    }
}
