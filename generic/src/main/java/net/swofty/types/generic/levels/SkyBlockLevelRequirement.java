package net.swofty.types.generic.levels;

public enum SkyBlockLevelRequirement {
    LEVEL_0(0),
    LEVEL_1(100),
    ;

    private final int experience;

    SkyBlockLevelRequirement(int experience) {
        this.experience = experience;
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