package net.swofty.type.murdermysterylobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardPeriod;
import net.swofty.commons.murdermystery.MurderMysteryModeStats;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MurderMysteryLobbyScoreboard {
	private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
	private static Integer animationFrame = 0;

	public static void start() {
		Scheduler scheduler = MinecraftServer.getSchedulerManager();

		scheduler.submitTask(() -> {
			animationFrame++;
			if (animationFrame > 50) {
				animationFrame = 0;
			}

			for (HypixelPlayer player : HypixelGenericLoader.getLoadedPlayers()) {
				if (player.getDataHandler() == null) {
					continue;
				}

				MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(player);
				long totalKills = 0;
				long totalWins = 0;
				long detectiveWins = 0;
				long murdererWins = 0;
				long tokens = 0;

				if (handler != null) {
					DatapointMurderMysteryModeStats statsDP = handler.get(
							MurderMysteryDataHandler.Data.MODE_STATS,
							DatapointMurderMysteryModeStats.class);
					MurderMysteryModeStats stats = statsDP.getValue();
					totalKills = stats.getTotalKills(MurderMysteryLeaderboardPeriod.LIFETIME);
					totalWins = stats.getTotalWins(MurderMysteryLeaderboardPeriod.LIFETIME);
					detectiveWins = stats.getTotalDetectiveWins(MurderMysteryLeaderboardPeriod.LIFETIME);
					murdererWins = stats.getTotalMurdererWins(MurderMysteryLeaderboardPeriod.LIFETIME);
					tokens = stats.getTotalTokens(MurderMysteryLeaderboardPeriod.LIFETIME);
				}

                List<Component> lines = new ArrayList<>();
                lines.add(HypixelScoreboard.legacy("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()) + " §8" + HypixelConst.getServerName()));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_lobby.total_kills_label") + totalKills));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_lobby.total_wins_label") + totalWins));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_lobby.wins_as_detective_label") + detectiveWins));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_lobby.wins_as_murderer_label") + murdererWins));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.murdermystery_lobby.tokens_label") + tokens));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.common.footer")));

				if (!scoreboard.hasScoreboard(player)) {
					scoreboard.createScoreboard(player, getSidebarName(animationFrame));
				}

				scoreboard.updateLines(player, lines);
				scoreboard.updateTitle(player, getSidebarName(animationFrame));
			}
			return TaskSchedule.tick(4);
		});
	}

	public static void removeCache(Player player) {
		scoreboard.removeScoreboard(player);
	}

    private static Component getSidebarName(int counter) {
        return HypixelScoreboard.animatedSidebarName(
            I18n.string("scoreboard.murdermystery_lobby.title_base"),
            counter,
            NamedTextColor.WHITE,
            NamedTextColor.GOLD,
            NamedTextColor.YELLOW,
            NamedTextColor.WHITE,
            NamedTextColor.YELLOW,
            14,
            15,
            25,
            35,
            45,
            Component.text("MYSTERY", NamedTextColor.GREEN, net.kyori.adventure.text.format.TextDecoration.BOLD)
        );
	}
}
