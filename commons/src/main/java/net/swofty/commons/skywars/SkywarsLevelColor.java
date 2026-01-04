package net.swofty.commons.skywars;

import lombok.Getter;

@Getter
public enum SkywarsLevelColor {
    NONE(0, "7", "⋆"),
    IRON(5, "f", "✦"),
    GOLD(10, "6", "✦"),
    DIAMOND(15, "b", "✦"),
    EMERALD(20, "a", "✦"),
    SAPPHIRE(25, "3", "✦"),
    RUBY(30, "c", "✦"),
    CRYSTAL(35, "d", "✦"),
    OPAL(40, "8", "✦"),
    AMETHYST(45, "5", "✦"),
    RAINBOW(50, "f", "✦");

    private final int minimumLevel;
    private final String colorCode;
    private final String symbol;

    SkywarsLevelColor(int minimumLevel, String colorCode, String symbol) {
        this.minimumLevel = minimumLevel;
        this.colorCode = colorCode;
        this.symbol = symbol;
    }

    public static SkywarsLevelColor fromLevel(int level) {
        SkywarsLevelColor result = NONE;
        for (SkywarsLevelColor color : values()) {
            if (level >= color.minimumLevel) {
                result = color;
            }
        }
        return result;
    }

    public String constructLevelBrackets(int level) {
        if (this == RAINBOW) {
            return constructRainbowBrackets(level);
        }
        return "§" + colorCode + "[" + level + symbol + "]";
    }

    public String constructLevelString(int level) {
        if (this == RAINBOW) {
            return constructRainbowString(level);
        }
        return "§" + colorCode + level + symbol;
    }

    private String constructRainbowBrackets(int level) {
        String levelStr = String.valueOf(level);
        String[] colors = {"c", "6", "e", "a", "b", "d"};
        StringBuilder result = new StringBuilder("§" + colors[0] + "[");

        for (int i = 0; i < levelStr.length(); i++) {
            result.append("§").append(colors[(i + 1) % colors.length]).append(levelStr.charAt(i));
        }

        result.append("§f").append(symbol).append("§" + colors[0] + "]");
        return result.toString();
    }

    private String constructRainbowString(int level) {
        String levelStr = String.valueOf(level);
        String[] colors = {"c", "6", "e", "a", "b", "d"};
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < levelStr.length(); i++) {
            result.append("§").append(colors[i % colors.length]).append(levelStr.charAt(i));
        }

        result.append("§f").append(symbol);
        return result.toString();
    }

    public static String getLevelDisplay(int level) {
        SkywarsLevelColor color = fromLevel(level);
        return color.constructLevelBrackets(level);
    }
}
