package net.swofty.commons.bedwars;

import lombok.Getter;

import java.util.List;

@Getter
public enum BedwarsLevelColor {
    NONE(0, "§7", "✫"),
    IRON(100, "§f", "✫"),
    GOLD(200, "§6", "✫"),
    DIAMOND(300, "§b", "✫"),
    EMERALD(400, "§a", "✫"),
    SAPPHIRE(500, "§b", "✫"),
    RUBY(600, "§c", "✫"),
    CRYSTAL(700, "§d", "✫"),
    OPAL(800, "§8", "✫"),
    AMETHYST(900, "§5", "✫"),
    RAINBOW(1000, "§e", "✫");

    private final int levelThreshold;
    private final String color;
    private final String icon;

    BedwarsLevelColor(int levelThreshold, String color, String icon) {
        this.levelThreshold = levelThreshold;
        this.color = color;
        this.icon = icon;
    }

    public static BedwarsLevelColor getByLevel(int level) {
        for (BedwarsLevelColor levelColor : List.of(values()).reversed()) {
            if (level >= levelColor.getLevelThreshold()) {
                return levelColor;
            }
        }
        return NONE;
    }

    public static String constructLevelBrackets(int level) {
        BedwarsLevelColor levelColor = getByLevel(level);
        return String.format("%s[%s%s]", levelColor.getColor(), level, levelColor.getIcon());
    }

    public static String constructLevelString(int level) {
        BedwarsLevelColor levelColor = getByLevel(level);
        return String.format("%s%s%s", levelColor.getColor(), level, levelColor.getIcon());
    }
}
