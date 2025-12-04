package net.swofty.type.bedwarslobby.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.NPCParameters;
import net.swofty.type.generic.user.HypixelPlayer;

public class StatsNPC extends HypixelNPC {

	public StatsNPC() {
		super(new NPCParameters() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{
						"§6§lBed Wars Profile",
						"§fYour Level: ",
						"§fProgress: ",
						"§fAchievements: ",
						"§fTotal Wins: ",
						"§fCurrent Winstreak: §a0",
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
	public void onClick(PlayerClickNPCEvent event) {
		event.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
				.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
	}
}
