package net.swofty.commons.skywars;

import java.text.DecimalFormat;

public class SkywarsLevelUtil {

    private static final DecimalFormat FORMAT = new DecimalFormat("#,###.#");

    public static String suffix(double value) {
        if (value < 1000) {
            return FORMAT.format(value);
        } else {
            return FORMAT.format(value / 1000) + "k";
        }
    }

    /**
     * XP requirements per level:
     * Level 1-5: 100 XP each
     * Level 6-10: 200 XP each
     * Level 11-15: 300 XP each
     * Level 16+: 500 XP each
     */
    public static int calculateMaxExperienceFromLevel(int level) {
        if (level < 5) return 100;
        if (level < 10) return 200;
        if (level < 15) return 300;
        return 500;
    }

    public static int calculateLevel(long experience) {
        int level = 0;
        long totalExperience = 0;

        while (totalExperience <= experience) {
            int maxExperience = calculateMaxExperienceFromLevel(level);
            totalExperience += maxExperience;
            if (totalExperience <= experience) {
                level++;
            }
        }

        return level;
    }

    public static int calculateExperienceSinceLastLevel(long experience) {
        int level = calculateLevel(experience);
        long totalExperience = 0;

        for (int i = 0; i < level; i++) {
            totalExperience += calculateMaxExperienceFromLevel(i);
        }

        return (int) (experience - totalExperience);
    }

    public static int calculateMaxExperienceFromExperience(long experience) {
        int level = calculateLevel(experience);
        return calculateMaxExperienceFromLevel(level);
    }

    public static long calculateTotalExperienceForLevel(int targetLevel) {
        long total = 0;
        for (int i = 0; i < targetLevel; i++) {
            total += calculateMaxExperienceFromLevel(i);
        }
        return total;
    }
}
