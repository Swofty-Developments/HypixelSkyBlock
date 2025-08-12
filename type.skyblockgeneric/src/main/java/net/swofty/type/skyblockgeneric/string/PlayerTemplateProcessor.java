package net.swofty.type.skyblockgeneric.string;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import SkyBlockPlayer;

public class PlayerTemplateProcessor {
    private final SkyBlockPlayer player;

    public PlayerTemplateProcessor(SkyBlockPlayer player) {
        this.player = player;
    }

    public String parseMessage(String string) {
        boolean inTag = false;
        boolean hasEscapedPercentage = false;
        StringBuilder currentTag = new StringBuilder();
        StringBuilder processedString = new StringBuilder();

        for (char c : string.toCharArray()) {
            if (c == '\\') {
                hasEscapedPercentage = true;
                continue;
            } else if (c == '%') {
                if (hasEscapedPercentage) {
                    processedString.append('%');
                    hasEscapedPercentage = false;
                    continue;
                }
                if (inTag) {
                    String tag = currentTag.toString();
                    if (tag.startsWith("player.")) {
                        String templateKey = tag.substring("player.".length());
                        String[] split = templateKey.split(":");

                        PlayerTemplates template = PlayerTemplates.valueOf(split[0].toUpperCase());
                        Component processedTag = template.process(player, templateKey);
                        processedString.append(
                                LegacyComponentSerializer.legacySection().serialize(processedTag)
                        );
                    } else {
                        processedString.append('%').append(tag).append('%');
                    }
                    currentTag = new StringBuilder();
                }
                inTag = !inTag;
                continue;
            }

            hasEscapedPercentage = false;

            if (inTag) {
                currentTag.append(c);
            } else {
                processedString.append(c);
            }
        }

        if (inTag) {
            // Handle unclosed tag
            processedString.append('%').append(currentTag);
        }

        return processedString.toString();
    }
}