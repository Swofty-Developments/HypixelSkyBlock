package net.swofty.type.skywarslobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skywars.SkyWarsLevelColor;
import net.swofty.commons.skywars.SkywarsLeaderboardMode;
import net.swofty.commons.skywars.SkywarsLeaderboardPeriod;
import net.swofty.commons.skywars.SkywarsModeStats;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsModeStats;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.scoreboard.HypixelScoreboard;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.level.SkywarsLevelRegistry;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SkyWarsLobbyScoreboard {
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
				Locale l = player.getLocale();
				SkywarsDataHandler swDataHandler = SkywarsDataHandler.getUser(player);

				if (swDataHandler == null) {
					continue;
				}

				long experience = swDataHandler.get(SkywarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue();
				long souls = swDataHandler.get(SkywarsDataHandler.Data.SOULS, DatapointLong.class).getValue();
				long coins = swDataHandler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
				long tokens = swDataHandler.get(SkywarsDataHandler.Data.TOKENS, DatapointLong.class).getValue();

				SkywarsModeStats modeStats = swDataHandler.get(SkywarsDataHandler.Data.MODE_STATS, DatapointSkywarsModeStats.class).getValue();

				long soloKills = modeStats.getKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
						+ modeStats.getKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
				long soloWins = modeStats.getWins(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
						+ modeStats.getWins(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);

				long doublesKills = modeStats.getKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
				long doublesWins = modeStats.getWins(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);

				int level = SkywarsLevelRegistry.calculateLevel(experience);

				List<Component> lines = new ArrayList<>();
				lines.add(I18n.t("scoreboard.common.date_line", Argument.tagResolver(Formatter.date("date", LocalDateTime.now(ZoneId.systemDefault()))), Argument.string("id", HypixelConst.getServerName())));
				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.skywars_lobby.your_level_line",
					Component.text(SkyWarsLevelColor.getLevelDisplay(level))));
				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.skywars_lobby.solo_kills_line", Component.text(String.valueOf(soloKills))));
				lines.add(I18n.t("scoreboard.skywars_lobby.solo_wins_line", Component.text(String.valueOf(soloWins))));
				lines.add(I18n.t("scoreboard.skywars_lobby.doubles_kills_line", Component.text(String.valueOf(doublesKills))));
				lines.add(I18n.t("scoreboard.skywars_lobby.doubles_wins_line", Component.text(String.valueOf(doublesWins))));
				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.skywars_lobby.coins_line", Component.text(String.valueOf(coins))));
				lines.add(I18n.t("scoreboard.skywars_lobby.souls_line", Component.text(String.valueOf(souls))));
				lines.add(I18n.t("scoreboard.skywars_lobby.tokens_line", Component.text(String.valueOf(tokens))));
				lines.add(Component.space());
				lines.add(I18n.t("scoreboard.common.footer"));

				if (!scoreboard.hasScoreboard(player)) {
					scoreboard.createScoreboard(player, Component.text(getSidebarName(animationFrame, l)));
				}

				scoreboard.updateLines(player, lines);
				scoreboard.updateTitle(player, Component.text(getSidebarName(animationFrame, l)));
			}
			return TaskSchedule.tick(4);
		});
	}

	public static void removeCache(Player player) {
		scoreboard.removeScoreboard(player);
	}

	private static String getSidebarName(int counter, Locale locale) {
		String baseText = I18n.string("scoreboard.skywars_lobby.title_base", locale);
		String[] colors = {"§f§l", "§e§l", "§6§l"};

		if (counter > 0 && counter <= 7) {
			return colors[0] + baseText.substring(0, counter - 1) +
					colors[1] + baseText.charAt(counter - 1) +
					colors[2] + baseText.substring(counter);
		} else if ((counter >= 8 && counter <= 18) ||
				(counter >= 25 && counter <= 29)) {
			return colors[0] + baseText;
		} else {
			return colors[1] + baseText;
		}
	}
}
