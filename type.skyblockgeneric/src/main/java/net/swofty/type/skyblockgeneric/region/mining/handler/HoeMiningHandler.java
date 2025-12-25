package net.swofty.type.skyblockgeneric.region.mining.handler;

import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.components.HoeComponent;

import java.util.List;

/**
 * Mining handler for blocks that benefit from hoes.
 * Placeholder for future farming speed bonuses.
 * Used for: pumpkins, melons, and potentially other farming blocks.
 */
public class HoeMiningHandler implements SkyBlockMiningHandler {
    private final double strength;

    public HoeMiningHandler(double strength) {
        this.strength = strength;
    }

    @Override
    public List<Class<? extends SkyBlockItemComponent>> getValidToolComponents() {
        return List.of(HoeComponent.class);
    }

    @Override
    public double getStrength() {
        return strength;
    }

    @Override
    public int getMiningPowerRequirement() {
        return 0;
    }

    @Override
    public ItemStatistic getSpeedStatistic() {
        // TODO: Add FARMING_SPEED to ItemStatistic if needed
        return ItemStatistic.MINING_SPEED;
    }

    @Override
    public String getHandlerName() {
        return "Hoe";
    }
}
