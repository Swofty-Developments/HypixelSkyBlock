package net.swofty.type.replayviewer.playback.scoreboard;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.swofty.type.game.replay.ReplayMetadata;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class BedWarsReplayScoreboard implements ReplayScoreboard {

    private final ReplaySession session;
    private Sidebar sidebar;

    // Team state tracking
    private final Map<String, TeamState> teamStates = new HashMap<>();
    private String nextEventName = "Waiting...";
    private int nextEventSeconds = 0;
    private int titleAnimationCounter = 0;

    public BedWarsReplayScoreboard(ReplaySession session) {
        this.session = session;
        initializeTeamStates();
    }

    private void initializeTeamStates() {
        ReplayMetadata metadata = session.getMetadata();
        if (metadata.getTeamInfo() != null) {
            for (Map.Entry<String, ReplayMetadata.TeamInfo> entry : metadata.getTeamInfo().entrySet()) {
                String teamId = entry.getKey();
                ReplayMetadata.TeamInfo info = entry.getValue();
                List<UUID> players = metadata.getTeams() != null ?
                    metadata.getTeams().getOrDefault(teamId, List.of()) :
                    List.of();

                teamStates.put(teamId, new TeamState(
                    info.name(),
                    info.colorCode(),
                    true, // bed starts alive
                    players.size(),
                    players.size()
                ));
            }
        }
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

        titleAnimationCounter++;
        if (titleAnimationCounter > 50) {
            titleAnimationCounter = 0;
        }

        // Update title with animation
        sidebar.setTitle(Component.text(getAnimatedTitle()));

        List<String> lines = getLines(session);

        // Clear and rebuild lines
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
        return "§f§lBED WARS";
    }

    private String getAnimatedTitle() {
        String baseText = "BED WARS";
        String[] colors = {"§f§l", "§6§l", "§e§l"};
        String endColor = "§a§l";

        int counter = titleAnimationCounter;
        if (counter > 0 && counter <= 8) {
            return colors[0] + baseText.substring(0, counter - 1) +
                colors[1] + baseText.charAt(counter - 1) +
                colors[2] + baseText.substring(counter) +
                endColor;
        } else if ((counter >= 9 && counter <= 19) ||
            (counter >= 25 && counter <= 29)) {
            return colors[0] + baseText + endColor;
        } else {
            return colors[2] + baseText + endColor;
        }
    }

    @Override
    public List<String> getLines(ReplaySession session) {
        List<String> lines = new ArrayList<>();

        // Date line
        lines.add("§7" + new SimpleDateFormat("MM/dd/yy").format(
            new Date(session.getMetadata().getStartTime())));
        lines.add("§7 ");

        // Event timer
        String timeLeft = formatTime(nextEventSeconds);
        lines.add("§f" + nextEventName + " in §a" + timeLeft);
        lines.add("§7 ");

        // Team statuses
        for (Map.Entry<String, TeamState> entry : teamStates.entrySet()) {
            lines.add(formatTeamLine(entry.getValue()));
        }

        lines.add("§7 ");

        // Replay info overlay
        lines.add("§8§m                    ");
        lines.add("§fTime: §e" + session.getFormattedTime() + "§7/" + session.getFormattedTotalTime());
        lines.add("§fSpeed: §a" + session.getPlaybackSpeed() + "x");
        lines.add("§7 ");

        lines.add("§ewww.hypixel.net");

        return lines;
    }

    private String formatTeamLine(TeamState team) {
        String initial = team.name.substring(0, 1).toUpperCase();
        String status;

        if (team.alivePlayers <= 0) {
            status = "§c✖";
        } else if (team.bedAlive) {
            status = "§a✔";
        } else {
            status = "§c" + team.alivePlayers;
        }

        return team.colorCode + initial + " §f" + team.name + " " + status;
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public void onGameEvent(String eventType, Object data) {
        switch (eventType) {
            case "bed_destruction" -> {
                if (data instanceof String teamId) {
                    TeamState state = teamStates.get(teamId);
                    if (state != null) {
                        state.bedAlive = false;
                    }
                }
            }
            case "player_eliminated" -> {
                if (data instanceof Map<?, ?> eventData) {
                    String teamId = (String) eventData.get("teamId");
                    TeamState state = teamStates.get(teamId);
                    if (state != null) {
                        state.alivePlayers = Math.max(0, state.alivePlayers - 1);
                    }
                }
            }
            case "team_eliminated" -> {
                if (data instanceof String teamId) {
                    TeamState state = teamStates.get(teamId);
                    if (state != null) {
                        state.alivePlayers = 0;
                        state.bedAlive = false;
                    }
                }
            }
            case "event_update" -> {
                if (data instanceof Map<?, ?> eventData) {
                    Object nameObj = eventData.get("name");
                    Object secondsObj = eventData.get("seconds");
                    nextEventName = nameObj instanceof String s ? s : "";
                    nextEventSeconds = secondsObj instanceof Integer i ? i : 0;
                }
            }
            case "generator_upgrade" -> {
                if (data instanceof Map<?, ?> eventData) {
                    Object typeObj = eventData.get("type");
                    Object tierObj = eventData.get("tier");
                    String genType = typeObj instanceof String s ? s : "";
                    int tier = tierObj instanceof Integer i ? i : 1;
                    nextEventName = genType + " Tier " + (tier + 1);
                }
            }
        }
    }

    /**
     * Updates the event countdown timer.
     *
     * @param eventName name of the next event
     * @param seconds   seconds until the event
     */
    public void updateEventTimer(String eventName, int seconds) {
        this.nextEventName = eventName;
        this.nextEventSeconds = seconds;
    }

    /**
     * Marks a team's bed as destroyed.
     *
     * @param teamId the team whose bed was destroyed
     */
    public void onBedDestroyed(String teamId) {
        TeamState state = teamStates.get(teamId);
        if (state != null) {
            state.bedAlive = false;
        }
    }

    /**
     * Updates a team's alive player count.
     *
     * @param teamId       the team to update
     * @param alivePlayers number of alive players
     */
    public void updateTeamAlivePlayers(String teamId, int alivePlayers) {
        TeamState state = teamStates.get(teamId);
        if (state != null) {
            state.alivePlayers = alivePlayers;
        }
    }

    /**
     * Marks a team as eliminated.
     *
     * @param teamId the team that was eliminated
     */
    public void onTeamEliminated(String teamId) {
        TeamState state = teamStates.get(teamId);
        if (state != null) {
            state.alivePlayers = 0;
            state.bedAlive = false;
        }
    }

    /**
     * Internal class to track team state during replay.
     */
    private static class TeamState {
        final String name;
        final String colorCode;
        boolean bedAlive;
        int alivePlayers;
        final int totalPlayers;

        TeamState(String name, String colorCode, boolean bedAlive, int alivePlayers, int totalPlayers) {
            this.name = name;
            this.colorCode = colorCode;
            this.bedAlive = bedAlive;
            this.alivePlayers = alivePlayers;
            this.totalPlayers = totalPlayers;
        }
    }
}
