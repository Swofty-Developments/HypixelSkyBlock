package net.swofty.type.generic.scoreboard;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

import java.util.*;

public class HypixelScoreboard {
    private final Map<UUID, Sidebar> sidebarCache = new HashMap<>();
    private final Map<UUID, List<String>> lineCache = new HashMap<>();

    private static String lineId(int index) {
        return "line_" + index;
    }

    public void createScoreboard(Player player, String title) {
        if (sidebarCache.containsKey(player.getUuid())) return;

        Sidebar sidebar = new Sidebar(Component.text(title));
        sidebar.addViewer(player);
        sidebarCache.put(player.getUuid(), sidebar);
        lineCache.put(player.getUuid(), new ArrayList<>());
    }

    public void updateTitle(Player player, String title) {
        Sidebar sidebar = sidebarCache.get(player.getUuid());
        if (sidebar == null) return;

        sidebar.setTitle(Component.text(title));
    }

    public void updateLines(Player player, List<String> lines) {
        Sidebar sidebar = sidebarCache.get(player.getUuid());
        if (sidebar == null) return;

        List<String> cached = lineCache.getOrDefault(player.getUuid(), new ArrayList<>());
        if (cached.equals(lines)) return;

        int oldCount = cached.size();
        int newCount = lines.size();
        int commonCount = Math.min(oldCount, newCount);

        for (int i = 0; i < commonCount; i++) {
            if (!cached.get(i).equals(lines.get(i))) {
                sidebar.updateLineContent(lineId(i), Component.text(lines.get(i)));
            }
            int oldScore = oldCount - 1 - i;
            int newScore = newCount - 1 - i;
            if (oldScore != newScore) {
                sidebar.updateLineScore(lineId(i), newScore);
            }
        }

        for (int i = oldCount; i < newCount; i++) {
            sidebar.createLine(new Sidebar.ScoreboardLine(
                    lineId(i),
                    Component.text(lines.get(i)),
                    newCount - 1 - i
            ));
        }

        for (int i = newCount; i < oldCount; i++) {
            sidebar.removeLine(lineId(i));
        }

        lineCache.put(player.getUuid(), new ArrayList<>(lines));
    }

    public void removeScoreboard(Player player) {
        Sidebar sidebar = sidebarCache.remove(player.getUuid());
        if (sidebar != null) {
            sidebar.removeViewer(player);
        }
        lineCache.remove(player.getUuid());
    }

    public boolean hasScoreboard(Player player) {
        return sidebarCache.containsKey(player.getUuid());
    }

    public Sidebar getSidebar(Player player) {
        return sidebarCache.get(player.getUuid());
    }
}
