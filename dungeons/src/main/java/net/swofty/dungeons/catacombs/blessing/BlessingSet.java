package net.swofty.dungeons.catacombs.blessing;

import java.util.EnumMap;
import java.util.Map;

public final class BlessingSet {
    private final Map<BlessingType, Integer> levels = new EnumMap<>(BlessingType.class);

    public void add(BlessingType type, int level) {
        levels.merge(type, level, Integer::sum);
    }

    public int level(BlessingType type) {
        return levels.getOrDefault(type, 0);
    }

    public Map<BlessingType, Integer> levels() {
        return Map.copyOf(levels);
    }

    public Map<BlessingStat, AppliedBlessingStat> appliedStats(double effectivenessMultiplier) {
        Map<BlessingStat, AppliedBlessingStat> applied = new EnumMap<>(BlessingStat.class);
        levels.forEach((type, level) -> type.effects().forEach(effect -> {
            AppliedBlessingStat current = applied.getOrDefault(effect.stat(), new AppliedBlessingStat(0, 0));
            applied.put(effect.stat(), new AppliedBlessingStat(
                    current.percent() + effect.percent(level) * effectivenessMultiplier,
                    current.flat() + effect.flat(level) * effectivenessMultiplier));
        }));
        return Map.copyOf(applied);
    }

    public record AppliedBlessingStat(double percent, double flat) {}
}
