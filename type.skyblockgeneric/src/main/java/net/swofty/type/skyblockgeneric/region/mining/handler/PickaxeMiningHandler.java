package net.swofty.type.skyblockgeneric.region.mining.handler;

import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.components.DrillComponent;
import net.swofty.type.skyblockgeneric.item.components.PickaxeComponent;

import java.util.List;

/**
 * Mining handler for blocks that require pickaxes or drills.
 * Used for: stone, ores, dwarven metals, etc.
 */
public class PickaxeMiningHandler implements SkyBlockMiningHandler {
    private final double strength;
    private final int miningPowerRequirement;

    public PickaxeMiningHandler(double strength, int miningPowerRequirement) {
        this.strength = strength;
        this.miningPowerRequirement = miningPowerRequirement;
    }

    @Override
    public List<Class<? extends SkyBlockItemComponent>> getValidToolComponents() {
        return List.of(PickaxeComponent.class, DrillComponent.class);
    }

    @Override
    public double getStrength() {
        return strength;
    }

    @Override
    public int getMiningPowerRequirement() {
        return miningPowerRequirement;
    }

    @Override
    public ItemStatistic getSpeedStatistic() {
        return ItemStatistic.MINING_SPEED;
    }

    @Override
    public String getHandlerName() {
        return "Pickaxe";
    }
}
