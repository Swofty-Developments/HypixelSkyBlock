package net.swofty.type.generic.experience;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LevelReward {
    private final int level;
    private final int coins;
    private final int dust;
    private final int mysteryDust;
    private final String boosterType;
    private final int boosterDuration;
    private final List<String> specialRewards;

    public boolean hasCoins() {
        return coins > 0;
    }

    public boolean hasDust() {
        return dust > 0 || mysteryDust > 0;
    }

    public boolean hasBooster() {
        return boosterType != null && !boosterType.isEmpty();
    }

    public boolean hasSpecialRewards() {
        return specialRewards != null && !specialRewards.isEmpty();
    }

    public boolean hasAnyReward() {
        return hasCoins() || hasDust() || hasBooster() || hasSpecialRewards();
    }

    public String getCoinsDisplay() {
        if (coins >= 1000) {
            return (coins / 1000) + "k";
        }
        return String.valueOf(coins);
    }

    public String getBoosterDurationDisplay() {
        if (boosterDuration >= 60) {
            int hours = boosterDuration / 60;
            return hours + " Hour" + (hours > 1 ? "s" : "");
        }
        return boosterDuration + " Minutes";
    }
}
