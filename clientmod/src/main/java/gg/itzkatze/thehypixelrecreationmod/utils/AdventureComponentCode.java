package gg.itzkatze.thehypixelrecreationmod.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.List;

public final class AdventureComponentCode {
    private AdventureComponentCode() {
    }

    public static String serialize(Component component) {
        StringBuilder expression = new StringBuilder(serializeContents(component));
        expression = new StringBuilder(applyStyle(expression.toString(), component.getStyle()));
        for (Component sibling : component.getSiblings()) {
            expression.append(".append(").append(serialize(sibling)).append(")");
        }
        return expression.toString();
    }

    private static String serializeContents(Component component) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            List<String> arguments = new ArrayList<>();
            for (Object argument : translatable.getArgs()) {
                arguments.add(argument instanceof Component child
                        ? serialize(child)
                        : "Component.text(\"" + StringUtility.escapeJavaString(String.valueOf(argument)) + "\")");
            }
            String suffix = arguments.isEmpty() ? "" : ", " + String.join(", ", arguments);
            return "Component.translatable(\"" + StringUtility.escapeJavaString(translatable.getKey()) + "\"" + suffix + ")";
        }
        if (component.getContents() instanceof PlainTextContents plainText) {
            return "Component.text(\"" + StringUtility.escapeJavaString(plainText.text()) + "\")";
        }
        return "Component.text(\"" + StringUtility.escapeJavaString(component.getString()) + "\")";
    }

    private static String applyStyle(String expression, Style style) {
        if (style.getColor() != null) {
            String namedColor = namedColor(style.getColor().getValue());
            expression += namedColor == null
                    ? ".color(TextColor.color(0x%06X))".formatted(style.getColor().getValue())
                    : ".color(NamedTextColor." + namedColor + ")";
        }
        List<String> decorations = new ArrayList<>();
        if (style.isBold()) decorations.add("TextDecoration.BOLD");
        if (style.isItalic()) decorations.add("TextDecoration.ITALIC");
        if (style.isUnderlined()) decorations.add("TextDecoration.UNDERLINED");
        if (style.isStrikethrough()) decorations.add("TextDecoration.STRIKETHROUGH");
        if (style.isObfuscated()) decorations.add("TextDecoration.OBFUSCATED");
        if (!decorations.isEmpty()) expression += ".decorate(" + String.join(", ", decorations) + ")";
        return expression;
    }

    private static String namedColor(int rgb) {
        return switch (rgb) {
            case 0x000000 -> "BLACK";
            case 0x0000AA -> "DARK_BLUE";
            case 0x00AA00 -> "DARK_GREEN";
            case 0x00AAAA -> "DARK_AQUA";
            case 0xAA0000 -> "DARK_RED";
            case 0xAA00AA -> "DARK_PURPLE";
            case 0xFFAA00 -> "GOLD";
            case 0xAAAAAA -> "GRAY";
            case 0x555555 -> "DARK_GRAY";
            case 0x5555FF -> "BLUE";
            case 0x55FF55 -> "GREEN";
            case 0x55FFFF -> "AQUA";
            case 0xFF5555 -> "RED";
            case 0xFF55FF -> "LIGHT_PURPLE";
            case 0xFFFF55 -> "YELLOW";
            case 0xFFFFFF -> "WHITE";
            default -> null;
        };
    }
}
