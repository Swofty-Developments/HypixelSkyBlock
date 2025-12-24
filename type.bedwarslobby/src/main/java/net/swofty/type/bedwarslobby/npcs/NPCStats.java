package net.swofty.type.bedwarslobby.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.bedwars.BedwarsLevelColor;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;

import static net.swofty.commons.bedwars.BedwarsLevelUtil.suffix;

public class NPCStats extends HypixelNPC {

	public NPCStats() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				BedWarsDataHandler handler = BedWarsDataHandler.getUser(player);
				int level = 0;
				int progress = 0;
				int maxExperience = 0;
				long experience = 0;
				if (handler != null) {
					experience = handler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class).getValue();
					level = BedwarsLevelUtil.calculateLevel(experience);
					progress = BedwarsLevelUtil.calculateExperienceSinceLastLevel(experience);
					maxExperience = BedwarsLevelUtil.calculateMaxExperienceFromExperience(experience);
				}
				return new String[]{
						"§6§lBed Wars Profile",
						"§fYour Level: " + BedwarsLevelColor.constructLevelBrackets(level),
						"§fProgress: §b" + suffix(progress) + "§7/§a" + suffix(maxExperience),
						"§fAchievements: §e" + 0 + "§a/" + 0,
						"§fTotal Wins: §a" + 0,
						"§fCurrent Winstreak: §a" + 0,
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
			public boolean looking() {
				return false;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		event.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
				.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
	}
}
