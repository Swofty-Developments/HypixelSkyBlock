package net.swofty.type.murdermysterygame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.role.GameRole;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MurderMysteryGameScoreboard {
    private static final Map<UUID, Sidebar> sidebarCache = new HashMap<>();
    private static Integer animationFrame = 0;

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        scheduler.submitTask(() -> {
            animationFrame++;
            if (animationFrame > 50) {
                animationFrame = 0;
            }

            for (Game game : TypeMurderMysteryGameLoader.getGames()) {
                for (MurderMysteryPlayer player : game.getPlayers()) {
                    if (player.getInstance() == null) continue;

                if (sidebarCache.containsKey(player.getUuid())) {
                    sidebarCache.get(player.getUuid()).removeViewer(player);
                }

                Sidebar sidebar = new Sidebar(Component.text(getSidebarName(animationFrame)));

                addLine("§7" + new SimpleDateFormat("MM/dd/yy").format(new Date()) + " §8" + HypixelConst.getServerName(), sidebar);
                addLine("§7 ", sidebar);

                if (game.getGameStatus() == GameStatus.WAITING) {
                    addLine("§fMap: §a" + game.getMapEntry().getName(), sidebar);
                    addLine("§fPlayers: §a" + game.getPlayers().size() + "/" + game.getGameType().getMaxPlayers(), sidebar);
                    addLine("§7 ", sidebar);
                    if (game.getCountdown().isActive()) {
                        addLine("§fStarting in §a" + game.getCountdown().getSecondsRemaining() + "s", sidebar);
                    } else {
                        addLine("§fWaiting for players...", sidebar);
                    }
                    addLine("§7 ", sidebar);
                    addLine("§fMode: §a" + game.getGameType().getDisplayName(), sidebar);

                    // Send actionbar with role chances
                    int playerCount = game.getPlayers().size();
                    int murdererChance = playerCount > 0 ? Math.round(100f / playerCount) : 0;
                    int detectiveChance = playerCount > 0 ? Math.round(100f / playerCount) : 0;
                    Component actionBar = Component.empty()
                            .append(Component.text("Murderer Chance: " + murdererChance + "%", NamedTextColor.RED))
                            .append(Component.text("    ", NamedTextColor.GRAY))
                            .append(Component.text("Detective Chance: " + detectiveChance + "%", NamedTextColor.AQUA));
                    player.sendActionBar(actionBar);
                } else if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
                    GameRole role = game.getRoleManager().getRole(player.getUuid());

                    // Check if player is spectating (eliminated)
                    if (player.isEliminated()) {
                        // Spectator scoreboard
                        addLine("§7§lSPECTATING", sidebar);
                        addLine("§7 ", sidebar);

                        if (role != null) {
                            addLine("§fYour Role: " + getScoreboardRoleColor(role) + role.getDisplayName(), sidebar);
                        }
                        addLine("§7 ", sidebar);

                        // Players alive
                        int playersAlive = game.getRoleManager().countAliveWithRole(GameRole.INNOCENT)
                                + game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE)
                                + game.getRoleManager().countAliveWithRole(GameRole.MURDERER);
                        addLine("§fPlayers Alive: §a" + playersAlive, sidebar);

                        // Time Left
                        String timeLeft = formatTimeRemaining(game.getGameStartTime());
                        addLine("§fTime Left: §a" + timeLeft, sidebar);
                        addLine("§7 ", sidebar);

                        // Detective status
                        boolean detectiveAlive = game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE) > 0;
                        String detectiveStatus = detectiveAlive ? "§aAlive" : "§cDead";
                        addLine("§fDetective: " + detectiveStatus, sidebar);
                        addLine("§7 ", sidebar);

                        // Map name
                        addLine("§fMap: §a" + game.getMapEntry().getName(), sidebar);
                    } else {
                        // Normal in-game scoreboard for alive players
                        if (role != null) {
                            addLine("§fRole: " + getScoreboardRoleColor(role) + role.getDisplayName(), sidebar);
                        }
                        addLine("§7 ", sidebar);

                        // Innocents Left (includes innocents and detectives that aren't murderer)
                        int innocentsLeft = game.getRoleManager().countAliveWithRole(GameRole.INNOCENT)
                                + game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE);
                        addLine("§fInnocents Left: §a" + innocentsLeft, sidebar);

                        // Time Left
                        String timeLeft = formatTimeRemaining(game.getGameStartTime());
                        addLine("§fTime Left: §a" + timeLeft, sidebar);
                        addLine("§7 ", sidebar);

                        // Detective status
                        boolean detectiveAlive = game.getRoleManager().countAliveWithRole(GameRole.DETECTIVE) > 0;
                        String detectiveStatus = detectiveAlive ? "§aAlive" : "§cDead";
                        addLine("§fDetective: " + detectiveStatus, sidebar);
                        addLine("§7 ", sidebar);

                        // Map name
                        addLine("§fMap: §a" + game.getMapEntry().getName(), sidebar);
                    }
                } else if (game.getGameStatus() == GameStatus.ENDING) {
                    // End game scoreboard
                    addLine("§a§lGAME OVER!", sidebar);
                    addLine("§7 ", sidebar);

                    // Show the player's role
                    GameRole role = game.getRoleManager().getRole(player.getUuid());
                    if (role != null) {
                        addLine("§fYour Role: " + getScoreboardRoleColor(role) + role.getDisplayName(), sidebar);
                    }
                    addLine("§7 ", sidebar);

                    // Show kills if any
                    int kills = player.getKillsThisGame();
                    if (kills > 0) {
                        addLine("§fYour Kills: §a" + kills, sidebar);
                    }

                    // Show tokens earned
                    int tokens = player.getTokensEarnedThisGame();
                    addLine("§fTokens Earned: §6" + tokens, sidebar);
                    addLine("§7 ", sidebar);

                    // Map name
                    addLine("§fMap: §a" + game.getMapEntry().getName(), sidebar);
                    addLine("§fMode: §a" + game.getGameType().getDisplayName(), sidebar);
                }

                addLine("§7 ", sidebar);
                addLine("§ewww.hypixel.net", sidebar);

                sidebar.addViewer(player);
                sidebarCache.put(player.getUuid(), sidebar);
                }
            }
            return TaskSchedule.tick(5);
        });
    }

    private static final long GAME_DURATION_MS = 5 * 60 * 1000; // 5 minutes

    private static String getScoreboardRoleColor(GameRole role) {
        return switch (role) {
            case MURDERER -> "§c";      // Red
            case DETECTIVE -> "§b";     // Aqua
            case INNOCENT -> "§a";      // Green
            case ASSASSIN -> "§6";      // Gold
        };
    }

    private static String formatTimeRemaining(long gameStartTime) {
        if (gameStartTime == 0) return "5:00";
        long elapsed = System.currentTimeMillis() - gameStartTime;
        long remaining = GAME_DURATION_MS - elapsed;
        if (remaining < 0) remaining = 0;

        long minutes = remaining / 60000;
        long seconds = (remaining % 60000) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }

    public static void removeCache(Player player) {
        sidebarCache.remove(player.getUuid());
    }

    private static void addLine(String text, Sidebar sidebar) {
        for (Sidebar.ScoreboardLine existingLine : sidebar.getLines()) {
            sidebar.updateLineScore(existingLine.getId(), existingLine.getLine() + 1);
        }
        sidebar.createLine(new Sidebar.ScoreboardLine(UUID.randomUUID().toString(), Component.text(text), 0));
    }

    private static String getSidebarName(int counter) {
        String baseText = "MURDER MYSTERY";
        String[] colors = {"§f§l", "§6§l", "§e§l"};
        String endColor = "§a§l";

        if (counter > 0 && counter <= 14) {
            return colors[0] + baseText.substring(0, Math.min(counter - 1, baseText.length())) +
                    colors[1] + (counter <= baseText.length() ? String.valueOf(baseText.charAt(counter - 1)) : "") +
                    colors[2] + (counter < baseText.length() ? baseText.substring(counter) : "") +
                    endColor;
        } else if ((counter >= 15 && counter <= 25) || (counter >= 35 && counter <= 45)) {
            return colors[0] + baseText + endColor;
        } else {
            return colors[2] + baseText + endColor;
        }
    }
}
