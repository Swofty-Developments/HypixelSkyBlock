package net.swofty.type.skyblockgeneric.user;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SkyBlockActionBar {
    private static final Map<UUID, SkyBlockActionBar> playerBars = new ConcurrentHashMap<>();

    private final EnumMap<BarSection, PriorityQueue<DisplayReplacement>> replacements = new EnumMap<>(BarSection.class);
    private final Map<BarSection, String> defaultDisplays = new EnumMap<>(BarSection.class);

    public static SkyBlockActionBar getFor(HypixelPlayer player) {
        return playerBars.computeIfAbsent(player.getUuid(), k -> new SkyBlockActionBar());
    }

    private SkyBlockActionBar() {
        for (BarSection section : BarSection.VALUES) {
            replacements.put(section, new PriorityQueue<>(Comparator.comparingInt(DisplayReplacement::priority).reversed()));
        }
    }

    public void setDefaultDisplay(BarSection section, String display) {
        defaultDisplays.put(section, display);
    }

    public @Nullable DisplayReplacement getReplacement(BarSection section) {
        return replacements.get(section).peek();
    }

    public void addReplacement(BarSection section, DisplayReplacement replacement) {
        PriorityQueue<DisplayReplacement> sectionReplacements = replacements.get(section);
        sectionReplacements.offer(replacement);

        if (replacement.duration > 0) {
            scheduleRemoval(section, replacement);
        }
    }

    private void scheduleRemoval(BarSection section, DisplayReplacement replacement) {
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            replacements.get(section).remove(replacement);
        }, TaskSchedule.tick(replacement.duration()), TaskSchedule.stop());
    }

    public String buildActionBarString() {
        StringBuilder builder = new StringBuilder(64);
        for (BarSection section : BarSection.VALUES) {
            String display = getDisplayForSection(section);
            if (!display.isEmpty()) {
                if (!builder.isEmpty()) {
                    builder.append("     ");
                }
                builder.append(display);
            }
        }
        return builder.toString();
    }

    private String getDisplayForSection(BarSection section) {
        PriorityQueue<DisplayReplacement> sectionReplacements = replacements.get(section);
        if (!sectionReplacements.isEmpty()) {
            return sectionReplacements.peek().display;
        }
        return defaultDisplays.getOrDefault(section, "");
    }

    public enum BarSection {
        HEALTH, DEFENSE, MANA,
        ;

        private static final BarSection[] VALUES = values();
    }

    public record DisplayReplacement(String display, int duration, int priority) { }
}
