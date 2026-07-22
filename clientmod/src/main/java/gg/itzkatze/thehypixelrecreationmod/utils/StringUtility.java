package gg.itzkatze.thehypixelrecreationmod.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.Optional;
import java.util.regex.Pattern;

public final class StringUtility {
    private static final Pattern LEGACY_FORMATTING_PATTERN = Pattern.compile("(?i)[§&][0-9a-fk-orx]");

    private StringUtility() {
    }

    /**
     * Converts a Component (Text) into a legacy-formatted string with '§' codes
     * Works for vanilla ChatFormatting colors and formatting.
     */
    public static String toLegacyString(Component component) {
        StringBuilder out = new StringBuilder();

        component.visit((style, text) -> {
            var color = style.getColor();

            if (color != null) {
                for (ChatFormatting formatting : ChatFormatting.values()) {
                    if (color.equals(TextColor.fromLegacyFormat(formatting))) {
                        out.append(formatting);
                        break;
                    }
                }
            }

            if (style.isBold()) out.append(ChatFormatting.BOLD);
            if (style.isItalic()) out.append(ChatFormatting.ITALIC);
            if (style.isUnderlined()) out.append(ChatFormatting.UNDERLINE);
            if (style.isStrikethrough()) out.append(ChatFormatting.STRIKETHROUGH);
            if (style.isObfuscated()) out.append(ChatFormatting.OBFUSCATED);

            out.append(text);
            return Optional.empty();
        }, Style.EMPTY);

        return out.toString();
    }

    public static String stripColor(String s) {
        if (s == null) return "";

        return LEGACY_FORMATTING_PATTERN.matcher(s).replaceAll("");
    }

    public static String escapeJson(String value) {
        if (value == null) return "";
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    public static String escapeJavaString(String value) {
        return escapeJson(value).replace("\t", "\\t");
    }
}
