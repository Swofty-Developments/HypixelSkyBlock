package net.swofty.type.bedwarslobby.npcs.villagers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;

public class QuestMasterNPC extends HypixelNPC {

	public QuestMasterNPC() {
		super(new VillagerConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{
						"§bQuest Master",
						"§e§lRIGHT CLICK",
				};
			}


			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-23.5, 67, 6.15, 110, 0);
			}

			@Override
			public boolean looking() {
				return false;
			}

			@Override
			public VillagerProfession profession() {
				return VillagerProfession.LIBRARIAN;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		event.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
				.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
	}
}
