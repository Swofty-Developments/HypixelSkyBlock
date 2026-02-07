package net.swofty.type.replayviewer.playback.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.swofty.type.generic.HypixelConst;
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
        for (int i = 0; i < 15; i++) {
            sidebar.removeLine("line_" + i);
        }

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
        lines.add("§7" + new SimpleDateFormat("MM/dd/yyyy").format(new Date()) + "  §8" + HypixelConst.getServerName());
        lines.add("§7Replay from " + session.getMetadata().getServerId());
        lines.add("§7 ");

        lines.add("§fDate: " + new SimpleDateFormat("MM/dd/yy").format(new Date(session.getMetadata().getStartTime())));
        lines.add("§7 ");

        lines.add("§fDate: §a" + new SimpleDateFormat("MM/dd/yyyy").format(new Date(session.getMetadata().getStartTime())));
        lines.add("§fTime: §a" + new SimpleDateFormat("HH:mm").format(new Date(session.getMetadata().getStartTime())) + " (EST)");
        lines.add("§7 ");

        lines.add("§fGame: §aBedWars");
        lines.add("§fMode: §a" + session.getMetadata().getGameTypeName());

        String mapName = session.getMetadata().getMapName();
        if (mapName != null && !mapName.isEmpty()) {
            lines.add("§fMap: §a" + mapName);
        }
        lines.add("§ewww.hypixel.net");

        return lines;
    }

}
