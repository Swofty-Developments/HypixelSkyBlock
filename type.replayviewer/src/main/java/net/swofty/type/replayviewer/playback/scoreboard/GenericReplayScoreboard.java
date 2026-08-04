package net.swofty.type.replayviewer.playback.scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.swofty.commons.bedwars.BedWarsGameType;
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
        sidebar = new Sidebar(getTitle());
        sidebar.addViewer(viewer);
        update(session);
    }

    @Override
    public void update(ReplaySession session) {
        if (sidebar == null) return;

        List<Component> lines = getLines(session);
        for (int i = 0; i < 15; i++) {
            sidebar.removeLine("line_" + i);
        }

        for (int i = 0; i < lines.size() && i < 15; i++) {
            sidebar.createLine(new Sidebar.ScoreboardLine(
                    "line_" + i,
                    lines.get(i),
                    lines.size() - i,
                    Sidebar.NumberFormat.blank()
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
    public Component getTitle() {
        return Component.text("REPLAY", NamedTextColor.YELLOW, TextDecoration.BOLD);
    }

    @Override
    public List<Component> getLines(ReplaySession session) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.text(new SimpleDateFormat("MM/dd/yyyy").format(new Date()), NamedTextColor.GRAY)
                .appendSpace().appendSpace().append(Component.text(HypixelConst.getServerName(), NamedTextColor.DARK_GRAY)));
        lines.add(Component.text("Replay from " + session.getMetadata().descriptor().serverId(), NamedTextColor.GRAY));

        lines.add(Component.empty());

        lines.add(Component.text("Date: ", NamedTextColor.WHITE).append(Component.text(new SimpleDateFormat("MM/dd/yyyy").format(new Date(session.getMetadata().descriptor().startTime())), NamedTextColor.GREEN)));
        lines.add(Component.text("Time: ", NamedTextColor.WHITE).append(Component.text(new SimpleDateFormat("HH:mm").format(new Date(session.getMetadata().descriptor().startTime())), NamedTextColor.GREEN)).appendSpace().append(
                Component.text("(EST)", NamedTextColor.GREEN)
        ));

        lines.add(Component.empty());

        lines.add(Component.text("Game: ", NamedTextColor.WHITE).append(Component.text("BedWars", NamedTextColor.GREEN)));
        lines.add(Component.text("Mode: ", NamedTextColor.WHITE)
                .append(Component.text(formatMode(session.gameModeId()), NamedTextColor.GREEN)));

        lines.add(Component.empty());

        lines.add(Component.text("Map: ", NamedTextColor.WHITE).append(Component.text(session.getMetadata().descriptor().mapName(), NamedTextColor.GREEN)));
        lines.add(Component.text("www.hypixel.net", NamedTextColor.YELLOW));

        return lines;
    }

    private String formatMode(String mode) {
        try {
            return BedWarsGameType.valueOf(mode.toUpperCase()).getDisplayName();
        } catch (IllegalArgumentException ignored) {
            return mode.replace('_', ' ');
        }
    }

}
