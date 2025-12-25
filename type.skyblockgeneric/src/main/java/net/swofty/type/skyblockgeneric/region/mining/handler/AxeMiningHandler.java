package net.swofty.type.skyblockgeneric.region.mining.handler;

import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.components.AxeComponent;

import java.util.List;

/**
 * Mining handler for blocks that can be broken by axes or hands.
 * Uses the axe's axe_strength value from AxeComponent to calculate break time.
 * Axes break faster based on their axe_strength, hands use fixed time.
 * Used for: logs, wood blocks, etc.
 */
public class AxeMiningHandler implements SkyBlockMiningHandler {
    private final double blockStrength;
    private final int handBreakTimeTicks;

    /**
     * Create an axe mining handler.
     * @param blockStrength Block hardness - divided by axe's strength to get break time
     * @param handBreakTimeTicks Fixed time in ticks to break with bare hands (20 ticks = 1 second)
     */
    public AxeMiningHandler(double blockStrength, int handBreakTimeTicks) {
        this.blockStrength = blockStrength;
        this.handBreakTimeTicks = handBreakTimeTicks;
    }

    @Override
    public List<Class<? extends SkyBlockItemComponent>> getValidToolComponents() {
        return List.of(AxeComponent.class);
    }

    @Override
    public double getStrength() {
        return blockStrength;
    }

    @Override
    public int getMiningPowerRequirement() {
        return 0; // Logs don't have power requirements
    }

    @Override
    public ItemStatistic getSpeedStatistic() {
        return ItemStatistic.MINING_SPEED; // Not used, we calculate directly
    }

    @Override
    public boolean usesVanillaBreakTime() {
        return true;
    }

    @Override
    public int getFixedBreakTime() {
        return handBreakTimeTicks;
    }

    /**
     * Get the break time based on the tool being used.
     * For axes: calculates from block strength / axe strength
     * For hands: uses fixed handBreakTimeTicks
     * @param tool the tool being used (can be empty/null for bare hands)
     * @return break time in ticks
     */
    @Override
    public int getBreakTimeForTool(SkyBlockItem tool) {
        if (tool != null && tool.hasComponent(AxeComponent.class)) {
            AxeComponent axeComponent = tool.getComponent(AxeComponent.class);
            int axeStrength = axeComponent.getAxeStrength();
            // Calculate break time: blockStrength / axeStrength, minimum 1 tick
            return Math.max(1, (int) Math.ceil(blockStrength / axeStrength));
        }
        return handBreakTimeTicks; // Fixed time for bare hands
    }

    @Override
    public boolean canToolBreak(SkyBlockItem tool) {
        return true; // Both axes and hands can break logs
    }

    @Override
    public String getHandlerName() {
        return "Axe";
    }
}
