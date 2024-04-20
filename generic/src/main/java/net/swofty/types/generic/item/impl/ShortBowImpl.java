package net.swofty.types.generic.item.impl;

public interface ShortBowImpl extends BowImpl {
    default double getCooldown() {
        return 0.5;
    }
}
