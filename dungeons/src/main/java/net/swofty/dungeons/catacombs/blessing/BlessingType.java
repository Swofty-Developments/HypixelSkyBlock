package net.swofty.dungeons.catacombs.blessing;

import java.util.List;

public enum BlessingType {
    LIFE("Blessing of Life", List.of(
            new BlessingEffect(BlessingStat.HEALTH, 3, 0),
            new BlessingEffect(BlessingStat.HEALTH_REGEN, 3, 0))),
    POWER("Blessing of Power", List.of(
            new BlessingEffect(BlessingStat.STRENGTH, 2, 4),
            new BlessingEffect(BlessingStat.CRIT_DAMAGE, 2, 4))),
    STONE("Blessing of Stone", List.of(
            new BlessingEffect(BlessingStat.DEFENSE, 2, 4),
            new BlessingEffect(BlessingStat.DAMAGE, 0, 6))),
    WISDOM("Blessing of Wisdom", List.of(
            new BlessingEffect(BlessingStat.INTELLIGENCE, 2, 4),
            new BlessingEffect(BlessingStat.SPEED, 0, 0))),
    TIME("Blessing of Time", List.of(
            new BlessingEffect(BlessingStat.HEALTH, 2, 4),
            new BlessingEffect(BlessingStat.INTELLIGENCE, 2, 4),
            new BlessingEffect(BlessingStat.STRENGTH, 2, 4),
            new BlessingEffect(BlessingStat.DEFENSE, 2, 4)));

    private final String displayName;
    private final List<BlessingEffect> effects;

    BlessingType(String displayName, List<BlessingEffect> effects) {
        this.displayName = displayName;
        this.effects = effects;
    }

    public String displayName() {
        return displayName;
    }

    public List<BlessingEffect> effects() {
        return effects;
    }
}
