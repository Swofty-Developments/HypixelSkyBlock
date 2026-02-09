package net.swofty.type.skyblockgeneric.chocolatefactory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HoppityEggType {
    BREAKFAST("Chocolate Breakfast Egg", "6", breakfastTexture()),
    LUNCH("Chocolate Lunch Egg", "9", lunchTexture()),
    DINNER("Chocolate Dinner Egg", "a", dinnerTexture()),
    BRUNCH("Chocolate Brunch Egg", "6", breakfastTexture()),
    DEJEUNER("Chocolate Déjeuner Egg", "9", lunchTexture()),
    SUPPER("Chocolate Supper Egg", "a", dinnerTexture()),
    HITMAN("Hitman Egg", "c", breakfastTexture());

    private final String displayName;
    private final String colorCode;
    private final String textureHash;

    public String getFormattedName() {
        return "§" + colorCode + displayName;
    }

    private static String breakfastTexture() {
        return "a49333d85b8a315d0336eb2df37d8a714ca24c51b8c6074f1b5b927deb516c24";
    }

    private static String lunchTexture() {
        return "e5e36165819fd2850f98552edcd763ff986313119283c126ace0c4cc495e76a8";
    }

    private static String dinnerTexture() {
        return "7ae6d2d31d8167bcaf95293b68a4acd872d66e751db5a34f2cbc6766a0356d0a";
    }
}
