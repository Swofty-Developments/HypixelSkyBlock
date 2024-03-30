package net.swofty.types.generic.levels;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum SkyBlockLevelRequirement {
    LEVEL_0(0),
    LEVEL_1(100),
    LEVEL_2(200),
    LEVEL_3(300),
    LEVEL_4(400),
    LEVEL_5(500),
    ;

    private final int experience;

    SkyBlockLevelRequirement(int experience) {
        this.experience = experience;
    }

    public String getColor() {
        return "§7";
    }

    public @Nullable SkyBlockLevelRequirement getNextLevel() {
        return ordinal() + 1 < values().length ? values()[ordinal() + 1] : null;
    }

    @Override
    public String toString() {
        return String.valueOf(ordinal());
    }

    public static SkyBlockLevelRequirement getFromXP(double xp) {
        int cumulative = 0;
        for (SkyBlockLevelRequirement requirement : values()) {
            cumulative += requirement.experience;
            if (cumulative >= xp) {
                return requirement;
            }
        }
        return null;
    }
}