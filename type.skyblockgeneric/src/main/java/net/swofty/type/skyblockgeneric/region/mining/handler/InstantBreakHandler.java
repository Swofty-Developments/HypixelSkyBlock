package net.swofty.type.skyblockgeneric.region.mining.handler;

import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.Collections;
import java.util.List;

/**
 * Handler for blocks that break instantly with any tool (or hand).
 * Used for: wheat, sugar cane, flowers, mushrooms, crops, etc.
 */
public class InstantBreakHandler implements SkyBlockMiningHandler {
    public static final InstantBreakHandler INSTANCE = new InstantBreakHandler();

    private InstantBreakHandler() {}

    @Override
    public List<Class<? extends SkyBlockItemComponent>> getValidToolComponents() {
        return Collections.emptyList(); // Any tool works
    }

    @Override
    public double getStrength() {
        return 0; // Instant break
    }

    @Override
    public int getMiningPowerRequirement() {
        return 0;
    }

    @Override
    public boolean breaksInstantly() {
        return true;
    }

    @Override
    public boolean canToolBreak(SkyBlockItem tool) {
        return true; // Any tool works
    }

    @Override
    public String getHandlerName() {
        return "Instant";
    }
}
