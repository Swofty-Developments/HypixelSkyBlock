package net.swofty.commons.bedwars;

import java.text.DecimalFormat;

public class BedwarsLevelUtil {

    private static final DecimalFormat FORMAT = new DecimalFormat("#,###.#");

    public static String suffix(double value) {
        if (value < 1000) {
            return FORMAT.format(value);
        } else {
            return FORMAT.format(value / 1000) + "k";
        }
    }

    public static int calculateMaxExperienceFromLevel(int level) {
        int multiplier = level % 100;
        if (multiplier > 5) {
            multiplier = 5;
        }

        return switch (multiplier) {
            case 0 -> 500;
            case 1 -> 1000;
            case 2 -> 2000;
            case 3 -> 3500;
            default -> 5000;
        };
    }

    public static int calculateLevel(long experience) {
        int level = 0;
        int totalExperience = 0;

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
        int totalExperience = 0;

        for (int i = 0; i < level; i++) {
            totalExperience += calculateMaxExperienceFromLevel(i);
        }

        return (int) (experience - totalExperience);
    }

    public static int calculateMaxExperienceFromExperience(long experience) {
        int level = calculateLevel(experience);
        return calculateMaxExperienceFromLevel(level);
    }
}
