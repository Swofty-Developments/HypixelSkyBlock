package net.swofty.type.skywarslobby;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skywars.SkywarsLeaderboardMode;
import net.swofty.commons.skywars.SkywarsLeaderboardPeriod;
import net.swofty.commons.skywars.SkywarsLevelColor;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SkywarsLobbyScoreboard {
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
                lines.add(HypixelScoreboard.legacy("§7" + new SimpleDateFormat(I18n.string("scoreboard.common.date_format")).format(new Date()) + " §8" + HypixelConst.getServerName()));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_lobby.your_level_label") + " " + SkywarsLevelColor.getLevelDisplay(level)));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_lobby.solo_kills_label") + soloKills));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_lobby.solo_wins_label") + soloWins));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_lobby.doubles_kills_label") + doublesKills));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_lobby.doubles_wins_label") + doublesWins));
                lines.add(HypixelScoreboard.legacy("§7 "));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_lobby.coins_label") + coins));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_lobby.souls_label") + souls));
                lines.add(HypixelScoreboard.legacy(I18n.string("scoreboard.skywars_lobby.tokens_label") + tokens));
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
            I18n.string("scoreboard.skywars_lobby.title_base"),
            counter,
            NamedTextColor.WHITE,
            NamedTextColor.YELLOW,
            NamedTextColor.GOLD,
            NamedTextColor.WHITE,
            NamedTextColor.YELLOW,
            7,
            8,
            18,
            25,
            29,
            Component.empty()
        );
	}
}
