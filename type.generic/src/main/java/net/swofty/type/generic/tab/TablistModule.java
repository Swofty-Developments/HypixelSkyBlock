package net.swofty.type.generic.tab;

import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

public abstract class TablistModule {

    public abstract List<TablistEntry> getEntries(HypixelPlayer player);

    public record TablistEntry(String content, TablistSkinRegistry registry) {}

    public TablistEntry getGrayEntry() {
        return new TablistEntry(" ", TablistSkinRegistry.GRAY);
    }

    public static List<TablistEntry> fillRestWithGray(List<TablistEntry> entries) {
        for (int i = entries.size(); i < 20; i++) {
            entries.add(new TablistEntry("§7", TablistSkinRegistry.GRAY));
        }

        return entries;
    }

    public static String getCentered(String text) {
        // Pad text with spaces so that the entire string is 24 characters long
        // This will center the text on the tablist

        if (text.length() > 30) {
            return text.substring(0, 30);
        }

        int spaces = (30 - text.length()) / 2;
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(spaces));

        builder.append(text);

        return builder.toString();
    }
}
