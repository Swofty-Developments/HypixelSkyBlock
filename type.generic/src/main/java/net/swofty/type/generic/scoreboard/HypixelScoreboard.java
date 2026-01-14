package net.swofty.type.generic.scoreboard;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

import java.util.*;

public class HypixelScoreboard {
    private final Map<UUID, Sidebar> sidebarCache = new HashMap<>();
    private final Map<UUID, List<String>> lineCache = new HashMap<>();
    private final Map<UUID, String> titleCache = new HashMap<>();

    public void createScoreboard(Player player, String title) {
        Sidebar sidebar = sidebarCache.get(player.getUuid());
        if (sidebar == null) {
            sidebar = new Sidebar(Component.text(title));
            sidebar.addViewer(player);
            sidebarCache.put(player.getUuid(), sidebar);
            titleCache.put(player.getUuid(), title);
            lineCache.put(player.getUuid(), new ArrayList<>());
        }
    }

    public void updateTitle(Player player, String title) {
        String currentTitle = titleCache.get(player.getUuid());
        if (currentTitle != null && currentTitle.equals(title)) {
            return;
        }

        List<String> cachedLines = lineCache.get(player.getUuid());
        if (cachedLines == null) {
            cachedLines = new ArrayList<>();
        }

        Sidebar sidebar = sidebarCache.get(player.getUuid());
        if (sidebar != null) {
            sidebar.removeViewer(player);
            sidebarCache.remove(player.getUuid());
        }

        sidebar = new Sidebar(Component.text(title));
        sidebar.addViewer(player);
        sidebarCache.put(player.getUuid(), sidebar);
        titleCache.put(player.getUuid(), title);

        if (!cachedLines.isEmpty()) {
            for (int i = 0; i < cachedLines.size(); i++) {
                String lineText = cachedLines.get(i);
                sidebar.createLine(new Sidebar.ScoreboardLine(
                        UUID.randomUUID().toString(),
                        Component.text(lineText),
                        cachedLines.size() - i - 1
                ));
            }
        }
    }

    public void updateLines(Player player, List<String> lines) {
        Sidebar sidebar = sidebarCache.get(player.getUuid());
        if (sidebar == null) {
            return;
        }

        List<String> cachedLines = lineCache.getOrDefault(player.getUuid(), new ArrayList<>());

        if (cachedLines.equals(lines)) {
            return;
        }

        for (Sidebar.ScoreboardLine line : new ArrayList<>(sidebar.getLines())) {
            sidebar.removeLine(line.getId());
        }

        for (int i = 0; i < lines.size(); i++) {
            String lineText = lines.get(i);
            sidebar.createLine(new Sidebar.ScoreboardLine(
                    UUID.randomUUID().toString(),
                    Component.text(lineText),
                    lines.size() - i - 1
            ));
        }

        lineCache.put(player.getUuid(), new ArrayList<>(lines));
    }

    public void removeScoreboard(Player player) {
        Sidebar sidebar = sidebarCache.remove(player.getUuid());
        if (sidebar != null) {
            sidebar.removeViewer(player);
        }
        lineCache.remove(player.getUuid());
        titleCache.remove(player.getUuid());
    }

    public boolean hasScoreboard(Player player) {
        return sidebarCache.containsKey(player.getUuid());
    }

    public Sidebar getSidebar(Player player) {
        return sidebarCache.get(player.getUuid());
    }
}

