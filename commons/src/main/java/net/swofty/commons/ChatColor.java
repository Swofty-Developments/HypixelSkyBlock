package net.swofty.commons;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
public enum ChatColor {
    BLACK('0', 0),
    DARK_BLUE('1', 1),
    DARK_GREEN('2', 2),
    DARK_AQUA('3', 3),
    DARK_RED('4', 4),
    DARK_PURPLE('5', 5),
    GOLD('6', 6),
    GRAY('7', 7),
    DARK_GRAY('8', 8),
    BLUE('9', 9),
    GREEN('a', 10),
    AQUA('b', 11),
    RED('c', 12),
    LIGHT_PURPLE('d', 13),
    YELLOW('e', 14),
    WHITE('f', 15),
    MAGIC('k', 16, true),
    BOLD('l', 17, true),
    STRIKETHROUGH('m', 18, true),
    UNDERLINE('n', 19, true),
    ITALIC('o', 20, true),
    RESET('r', 21);

    public static final char COLOR_CHAR = '§';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '§' + "[0-9A-FK-OR]");
    private static final Map<Character, ChatColor> BY_CODE = new HashMap<>();

    static {
        for (ChatColor color : values()) {
            BY_CODE.put(color.code, color);
        }
    }

    private final int intCode;
    private final char code;
    private final boolean isFormat;

    private final String toString;

    ChatColor(char code, int intCode) {
        this(code, intCode, false);
    }

    ChatColor(char code, int intCode, boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[]{'§', code});
    }

    public static ChatColor getLastColor(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        for (int i = text.length() - 2; i >= 0; i--) {
            if (text.charAt(i) == ChatColor.COLOR_CHAR) {
                ChatColor color = BY_CODE.get(text.charAt(i + 1));
                if (color != null) {
                    return color;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.toString;
    }
}
