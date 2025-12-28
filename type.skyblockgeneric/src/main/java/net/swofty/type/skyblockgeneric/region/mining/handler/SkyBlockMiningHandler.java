package net.swofty.type.skyblockgeneric.region.mining.handler;

import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.List;

/**
 * Interface for handling block mining mechanics.
 * Each handler defines which tools can break blocks, strength, power requirements, and speed stats.
 */
public interface SkyBlockMiningHandler {
    /**
     * Get the list of component classes that can break blocks handled by this handler.
     * An empty list means any tool (or hand) can break the block.
     */
    List<Class<? extends SkyBlockItemComponent>> getValidToolComponents();

    /**
     * Get the block strength (hardness) for mining time calculation.
     * A value of 0 or less means the block breaks instantly.
     */
    double getStrength();

    /**
     * Get the mining power requirement.
     * 0 means no power requirement.
     */
    int getMiningPowerRequirement();

    /**
     * Get the statistic used for mining speed calculation.
     * Default is MINING_SPEED.
     */
    default ItemStatistic getSpeedStatistic() {
        return ItemStatistic.MINING_SPEED;
    }

    /**
     * Check if this block breaks instantly (strength <= 0).
     */
    default boolean breaksInstantly() {
        return getStrength() <= 0;
    }

    /**
     * Check if the given tool can break blocks with this handler.
     * Returns true if:
     * - The block breaks instantly (any tool works)
     * - The tool has one of the valid components
     */
    default boolean canToolBreak(SkyBlockItem tool) {
        if (breaksInstantly()) {
            return true;
        }

        List<Class<? extends SkyBlockItemComponent>> validComponents = getValidToolComponents();
        if (validComponents.isEmpty()) {
            return true; // No restrictions
        }

        for (Class<? extends SkyBlockItemComponent> componentClass : validComponents) {
            if (tool.hasComponent(componentClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a descriptive name for this handler type.
     */
    String getHandlerName();

    /**
     * Whether this handler uses vanilla-like fixed break times instead of stat-based calculations.
     * If true, getFixedBreakTime() will be used instead of strength/speed calculations.
     */
    default boolean usesVanillaBreakTime() {
        return false;
    }

    /**
     * Get the fixed break time in ticks for vanilla-like breaking.
     * Only used if usesVanillaBreakTime() returns true.
     * @return break time in ticks (20 ticks = 1 second)
     */
    default int getFixedBreakTime() {
        return 20; // 1 second default
    }

    /**
     * Get the break time in ticks for a specific tool.
     * Override this for handlers that have different speeds for different tools.
     * Only used if usesVanillaBreakTime() returns true.
     * @param tool the tool being used (can be null for bare hands)
     * @return break time in ticks
     */
    default int getBreakTimeForTool(SkyBlockItem tool) {
        return getFixedBreakTime();
    }
}
