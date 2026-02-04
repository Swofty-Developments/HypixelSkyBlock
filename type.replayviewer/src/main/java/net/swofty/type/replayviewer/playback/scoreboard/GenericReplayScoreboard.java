package net.swofty.type.replayviewer.playback.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenericReplayScoreboard implements ReplayScoreboard {

    private final ReplaySession session;
    private Sidebar sidebar;

    public GenericReplayScoreboard(ReplaySession session) {
        this.session = session;
    }

    @Override
    public void create(Player viewer) {
        sidebar = new Sidebar(Component.text(getTitle(), NamedTextColor.YELLOW));
        sidebar.addViewer(viewer);
        update(session);
    }

    @Override
    public void update(ReplaySession session) {
        if (sidebar == null) return;

        List<String> lines = getLines(session);

        // Clear existing lines
        for (int i = 0; i < 15; i++) {
            sidebar.removeLine("line_" + i);
        }

        // Add new lines
        for (int i = 0; i < lines.size() && i < 15; i++) {
            sidebar.createLine(new Sidebar.ScoreboardLine(
                "line_" + i,
                Component.text(lines.get(i)),
                lines.size() - i
            ));
        }
    }

    @Override
    public void remove(Player viewer) {
        if (sidebar != null) {
            sidebar.removeViewer(viewer);
        }
    }

    @Override
    public String getTitle() {
        String gameType = session.getMetadata().getGameTypeName();
        if (gameType == null || gameType.isEmpty()) {
            return "§e§lREPLAY";
        }
        return "§e§l" + gameType.toUpperCase();
    }

    @Override
    public List<String> getLines(ReplaySession session) {
        List<String> lines = new ArrayList<>();

        // Date line
        lines.add("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date(session.getMetadata().getStartTime())));
        lines.add("§7 ");

        // Map info
        String mapName = session.getMetadata().getMapName();
        if (mapName != null && !mapName.isEmpty()) {
            lines.add("§fMap: §a" + mapName);
        }

        // Player count
        int playerCount = session.getMetadata().getPlayers().size();
        lines.add("§fPlayers: §a" + playerCount);
        lines.add("§7 ");

        // Playback info
        lines.add("§fTime: §e" + session.getFormattedTime() + " §7/ " + session.getFormattedTotalTime());
        lines.add("§fSpeed: §a" + session.getPlaybackSpeed() + "x");

        String status = session.isPlaying() ? "§a▶ Playing" : "§e⏸ Paused";
        lines.add("§fStatus: " + status);
        lines.add("§7 ");

        // Progress bar
        float progress = session.getProgress();
        lines.add(createProgressBar(progress));
        lines.add("§7 ");

        // Footer
        lines.add("§ewww.hypixel.net");

        return lines;
    }

    private String createProgressBar(float percent) {
        int filled = (int) (percent / 10);
        StringBuilder bar = new StringBuilder("§a");
        for (int i = 0; i < 10; i++) {
            if (i < filled) {
                bar.append("■");
            } else if (i == filled) {
                bar.append("§e■§7");
            } else {
                bar.append("□");
            }
        }
        return bar.toString();
    }
}
