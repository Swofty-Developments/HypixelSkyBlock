package net.swofty.type.skywarslobby.soulwell;

import net.kyori.adventure.text.Component;
import net.swofty.type.generic.user.HypixelPlayer;

/**
 * Utility class for sending centered chat messages for Soul Well upgrades.
 */
public class SoulWellMessages {
    private static final int CENTER_PX = 154; // Minecraft chat center pixel

    /**
     * Send a purchase confirmation message to the player
     * Format:
     * [blank line]
     * [centered purple upgrade name]
     * [centered white description]
     * [blank line]
     * [centered yellow "Rewards"]
     * [centered effect description]
     */
    public static void sendPurchaseMessage(HypixelPlayer player, SoulWellUpgrade upgrade,
                                           SoulWellUpgrade.SoulWellUpgradeTier tier, int newLevel) {
        player.sendMessage(Component.empty());

        // Centered purple upgrade name with level
        String upgradeName = "§5§l" + upgrade.name().toUpperCase() + " " + toRoman(newLevel);
        player.sendMessage(Component.text(centerMessage(upgradeName)));

        // Centered white description
        String description = "§f" + upgrade.baseDescription();
        player.sendMessage(Component.text(centerMessage(description)));

        player.sendMessage(Component.empty());

        // Centered yellow "Rewards"
        player.sendMessage(Component.text(centerMessage("§e§lRewards")));

        // Centered effect description
        String effectLine = "§7" + tier.previousEffect() + " §l→ §a" + tier.newEffect() + " §7" + tier.effectDescription();
        player.sendMessage(Component.text(centerMessage(effectLine)));
    }

    /**
     * Center a message in Minecraft chat
     */
    public static String centerMessage(String message) {
        if (message == null || message.isEmpty()) return message;

        int messagePxSize = getMessagePixelWidth(message);
        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = 4; // Default space width
        int compensated = 0;

        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb.toString() + message;
    }

    /**
     * Calculate the pixel width of a message (accounting for color codes)
     */
    private static int getMessagePixelWidth(String message) {
        int width = 0;
        boolean isBold = false;
        boolean isColor = false;

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);

            if (c == '§' && i + 1 < message.length()) {
                char code = message.charAt(i + 1);
                if (code == 'l' || code == 'L') {
                    isBold = true;
                } else if (code == 'r' || code == 'R') {
                    isBold = false;
                } else if (isColorCode(code)) {
                    // Color codes don't reset bold in all versions
                }
                isColor = true;
                i++; // Skip the next character
                continue;
            }

            width += getCharWidth(c, isBold);
        }

        return width;
    }

    /**
     * Check if a character is a color code
     */
    private static boolean isColorCode(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /**
     * Get the pixel width of a character
     */
    private static int getCharWidth(char c, boolean bold) {
        int width;
        switch (c) {
            case ' ' -> width = 4;
            case '!' -> width = 2;
            case '"' -> width = 5;
            case '\'' -> width = 3;
            case '(' -> width = 5;
            case ')' -> width = 5;
            case '*' -> width = 5;
            case ',' -> width = 2;
            case '.' -> width = 2;
            case ':' -> width = 2;
            case ';' -> width = 2;
            case '<' -> width = 5;
            case '>' -> width = 5;
            case '@' -> width = 7;
            case 'I' -> width = 4;
            case '[' -> width = 4;
            case ']' -> width = 4;
            case '`' -> width = 3;
            case 'f' -> width = 5;
            case 'i' -> width = 2;
            case 'k' -> width = 5;
            case 'l' -> width = 3;
            case 't' -> width = 4;
            case '{' -> width = 5;
            case '|' -> width = 2;
            case '}' -> width = 5;
            case '~' -> width = 7;
            default -> width = 6;
        }
        if (bold) width += 1;
        return width;
    }

    /**
     * Convert a number to Roman numerals
     */
    public static String toRoman(int number) {
        if (number <= 0) return "";

        String[] romanNumerals = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                number -= values[i];
                result.append(romanNumerals[i]);
            }
        }
        return result.toString();
    }
}
