package net.swofty.type.skyblockgeneric.chocolatefactory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HoppityEggType {
    BREAKFAST("Chocolate Breakfast Egg", "6", "a49333d85b8a315d0336eb2df37d8a714ca24c51b8c6074f1b5b927deb516c24"),
    LUNCH("Chocolate Lunch Egg", "9", "e5e36165819fd2850f98552edcd763ff986313119283c126ace0c4cc495e76a8"),
    DINNER("Chocolate Dinner Egg", "a", "7ae6d2d31d8167bcaf95293b68a4acd872d66e751db5a34f2cbc6766a0356d0a"),
    BRUNCH("Chocolate Brunch Egg", "6", "a49333d85b8a315d0336eb2df37d8a714ca24c51b8c6074f1b5b927deb516c24"),
    DEJEUNER("Chocolate Déjeuner Egg", "9", "e5e36165819fd2850f98552edcd763ff986313119283c126ace0c4cc495e76a8"),
    SUPPER("Chocolate Supper Egg", "a", "7ae6d2d31d8167bcaf95293b68a4acd872d66e751db5a34f2cbc6766a0356d0a"),
    HITMAN("Hitman Egg", "c", "a49333d85b8a315d0336eb2df37d8a714ca24c51b8c6074f1b5b927deb516c24");

    private final String displayName;
    private final String colorCode;
    private final String textureHash;

    public String getFormattedName() {
        return "§" + colorCode + displayName;
    }
}
