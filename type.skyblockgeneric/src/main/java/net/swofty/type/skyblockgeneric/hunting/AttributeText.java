package net.swofty.type.skyblockgeneric.hunting;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.swofty.type.generic.i18n.HypixelTranslator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AttributeText {
    private static final Pattern LINK = Pattern.compile("\\[\\[([^]|]+)(?:\\|([^]]+))?]]");
    private static final Pattern TEMPLATE = Pattern.compile("\\{\\{([^{}]+)}}");

    private AttributeText() {
    }

    public static String plain(String input) {
        if (input == null || input.isBlank()) return "";
        String result = input.replace("'''", "").replace("''", "");
        Matcher links = LINK.matcher(result);
        StringBuilder linked = new StringBuilder();
        while (links.find()) links.appendReplacement(linked, Matcher.quoteReplacement(
                links.group(2) == null ? links.group(1) : links.group(2)));
        links.appendTail(linked);
        result = linked.toString();
        for (int pass = 0; pass < 8 && result.contains("{{"); pass++) {
            Matcher templates = TEMPLATE.matcher(result);
            StringBuilder replaced = new StringBuilder();
            while (templates.find()) {
                String[] parts = templates.group(1).split("\\|");
                String replacement = parts.length >= 3 ? parts[parts.length - 1]
                        : parts.length == 2 ? parts[1] : "";
                templates.appendReplacement(replaced, Matcher.quoteReplacement(replacement));
            }
            templates.appendTail(replaced);
            result = replaced.toString();
        }
        return result.replaceAll("<[^>]+>", "").replaceAll("\\s+", " ").trim();
    }

    public static List<String> wrap(String input, String color, int width) {
        if (input == null || input.isBlank()) return List.of();
        String rendered = LegacyComponentSerializer.legacySection().serialize(MiniMessage.builder()
                .tags(TagResolver.builder().resolver(TagResolver.standard())
                        .resolver(HypixelTranslator.SKYBLOCK_STAT_TAG_RESOLVER).build())
                .build().deserialize(input));
        String visible = plain(input);
        if (visible.isBlank() && rendered.isBlank()) return List.of();
        List<String> result = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        int visibleLength = 0;
        for (String word : rendered.split(" ")) {
            int wordLength = LegacyComponentSerializer.legacySection().deserialize(word).content().length();
            if (!line.isEmpty() && visibleLength + wordLength + 1 > width) {
                result.add(color + line);
                line.setLength(0);
                visibleLength = 0;
            }
            if (!line.isEmpty()) line.append(' ');
            line.append(word);
            visibleLength += wordLength + (visibleLength == 0 ? 0 : 1);
        }
        if (!line.isEmpty()) result.add(color + line);
        return result;
    }

    public static List<String> huntingLore(String input) {
        List<String> result = new ArrayList<>();
        if (input == null) return result;
        for (String line : input.split("\\n")) {
            String cleaned = plain(line.replaceFirst("^\\*\\s*", ""));
            if (!cleaned.isBlank()) result.addAll(wrap(cleaned, "§7", 34));
        }
        return result;
    }

    /**
     * Renders the description at one level. Descriptions remain MiniMessage strings in attributes.yml.
     */
    public static String atLevel(AttributeDefinition definition, int level) {
        String result = definition.effect();
        for (AttributeDefinition.AttributeEffect effect : definition.effects()) {
            String original = format(effect.minimum());
            String replacement = format(effect.atLevel(level));
            result = result.replaceFirst("(?<![0-9.])" + Pattern.quote(original) + "(?![0-9.])",
                    Matcher.quoteReplacement(replacement));
        }
        return result;
    }

    /**
     * Adds Hypixel's grey old value, arrow and green next value to every scaling value.
     */
    public static String upgrade(AttributeDefinition definition, int oldLevel, int newLevel) {
        String result = definition.effect();
        for (AttributeDefinition.AttributeEffect effect : definition.effects()) {
            String original = format(effect.minimum());
            String oldValue = format(oldLevel == 0 ? 0 : effect.atLevel(oldLevel));
            String newValue = format(effect.atLevel(newLevel));
            Pattern stat = Pattern.compile("<sbstat:([^:>]+):([+-]?)" + Pattern.quote(original) + ">");
            Matcher matcher = stat.matcher(result);
            if (matcher.find()) {
                String sign = matcher.group(2);
                String replacement = "<dark_gray>" + (oldLevel == 0 ? "" : sign) + oldValue
                        + "<bold>→</bold><green><sbstat:" + matcher.group(1) + ":" + sign + newValue + ">";
                result = matcher.replaceFirst(Matcher.quoteReplacement(replacement));
                continue;
            }
            Pattern plain = Pattern.compile("(?<![0-9.])([+-]?)" + Pattern.quote(original) + "(?![0-9.])");
            matcher = plain.matcher(result);
            if (matcher.find()) {
                String sign = matcher.group(1);
                result = matcher.replaceFirst(Matcher.quoteReplacement("<dark_gray>" + (oldLevel == 0 ? "" : sign)
                        + oldValue + "<bold>→</bold><green>" + sign + newValue + "</green>"));
            }
        }
        return result;
    }

    public static List<String> huntInfo(AttributeDefinition definition) {
        if (definition.huntInfo().isEmpty()) return List.of("§7 §7- §7Unknown");
        List<String> result = new ArrayList<>();
        for (String entry : definition.huntInfo()) {
            List<String> wrapped = wrap(entry, "§7", 34);
            for (int i = 0; i < wrapped.size(); i++)
                result.add((i == 0 ? "§7 §7- " : "§7   ") + wrapped.get(i));
        }
        return result;
    }

    private static String format(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}
