package net.swofty.type.skyblockgeneric.chocolatefactory;

/**
 * @deprecated Production is now calculated on-demand based on time elapsed since last update.
 * This approach:
 * - Works for offline production (calculates when player logs in or opens GUI)
 * - Is more efficient (no constant ticking for all players)
 * - Matches how Hypixel handles chocolate production
 *
 * Production is calculated in:
 * - GUIChocolateFactory.setItems() - when GUI opens
 * - GUIChocolateFactory.refreshItems() - while GUI is open
 * - Any purchase/interaction that needs current chocolate amount
 *
 * The calculation uses lastUpdated timestamp to compute elapsed time and apply production rate.
 */
@Deprecated
public class ChocolateFactoryProductionLoop {

    /**
     * @deprecated No longer needed. Production is calculated on-demand.
     * This method is kept for backwards compatibility but does nothing.
     */
    @Deprecated
    public static void start() {
        // Production is now calculated on-demand when:
        // 1. Player opens Chocolate Factory GUI
        // 2. GUI refreshes while open
        // 3. Any chocolate-related action occurs
        //
        // This is handled by ChocolateFactoryHelper.updateProduction()
        // which uses time-based calculation from lastUpdated timestamp.
        //
        // Benefits:
        // - Works offline (production accumulates based on time)
        // - More efficient (no server tick overhead)
        // - Matches Hypixel's implementation
    }
}
