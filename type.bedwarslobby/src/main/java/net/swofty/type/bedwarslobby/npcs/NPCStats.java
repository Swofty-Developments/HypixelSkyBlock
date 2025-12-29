package net.swofty.type.bedwarslobby.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.bedwars.BedWarsModeStats;
import net.swofty.commons.bedwars.BedwarsLeaderboardMode;
import net.swofty.commons.bedwars.BedwarsLeaderboardPeriod;
import net.swofty.commons.bedwars.BedwarsLevelColor;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.type.bedwarslobby.gui.GUIBedWarsStatistics;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsModeStats;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;

import static net.swofty.commons.bedwars.BedwarsLevelUtil.suffix;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCStats extends HypixelNPC {

	public NPCStats() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				BedWarsDataHandler handler = BedWarsDataHandler.getUser(player);
				int level = 0;
				int progress = 0;
				int maxExperience = 0;
				long totalWins = 0;
				long winstreak = 0;
				if (handler != null) {
					long experience = handler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class).getValue();
					level = BedwarsLevelUtil.calculateLevel(experience);
					progress = BedwarsLevelUtil.calculateExperienceSinceLastLevel(experience);
					maxExperience = BedwarsLevelUtil.calculateMaxExperienceFromExperience(experience);

					BedWarsModeStats modeStats = handler.get(BedWarsDataHandler.Data.MODE_STATS, DatapointBedWarsModeStats.class).getValue();
					totalWins = modeStats.getWins(BedwarsLeaderboardMode.ALL, BedwarsLeaderboardPeriod.LIFETIME);
					winstreak = modeStats.getWinstreak(BedwarsLeaderboardMode.ALL);
				}
				return new String[]{
						"§6§lBed Wars Profile",
						"§fYour Level: " + BedwarsLevelColor.constructLevelBrackets(level),
						"§fProgress: §b" + suffix(progress) + "§7/§a" + suffix(maxExperience),
						"§fAchievements: §e" + 0 + "§a/" + 0,
						"§fTotal Wins: §a" + suffix(totalWins),
						"§fCurrent Winstreak: §a" + suffix(winstreak),
						"§e§lCLICK FOR STATS",
				};
			}

			@Override
			public String texture(HypixelPlayer player) {
				return player.getPlayerSkin().textures();
			}

			@Override
			public String signature(HypixelPlayer player) {
				return player.getPlayerSkin().signature();
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(2, 67, -17, 65, 0);
			}

			@Override
			public boolean looking(HypixelPlayer player) {
				return false;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		new GUIBedWarsStatistics().open(event.player());
	}
}
