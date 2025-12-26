package net.swofty.type.generic.achievement;

import lombok.Getter;

@Getter
public enum SeasonType {
    HOLIDAY("Holiday", "§c", "Christmas/Winter event"),
    EASTER("Easter", "§b", "Easter/Spring event"),
    SUMMER("Summer", "§e", "Summer event"),
    HALLOWEEN("Halloween", "§6", "Halloween event");

    private final String displayName;
    private final String colorCode;
    private final String description;

    SeasonType(String displayName, String colorCode, String description) {
        this.displayName = displayName;
        this.colorCode = colorCode;
        this.description = description;
    }

    public static SeasonType fromString(String name) {
        for (SeasonType season : values()) {
            if (season.name().equalsIgnoreCase(name)) {
                return season;
            }
        }
        return null;
    }

    public String getFormattedName() {
        return colorCode + displayName + " Achievement";
    }
}
