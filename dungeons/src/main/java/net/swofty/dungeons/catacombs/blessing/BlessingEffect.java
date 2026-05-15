package net.swofty.dungeons.catacombs.blessing;

public record BlessingEffect(BlessingStat stat, double percentPerLevel, double flatPerLevel) {
    public double percent(int level) {
        return percentPerLevel * level;
    }

    public double flat(int level) {
        return flatPerLevel * level;
    }
}
