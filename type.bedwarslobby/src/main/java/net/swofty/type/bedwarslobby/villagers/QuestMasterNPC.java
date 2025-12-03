package net.swofty.type.bedwarslobby.villagers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.entity.villager.NPCVillagerParameters;

public class QuestMasterNPC extends HypixelVillagerNPC {

	public QuestMasterNPC() {
		super(new NPCVillagerParameters() {
			@Override
			public String[] holograms() {
				return new String[]{
						"§bQuest Master",
						"§e§lRIGHT CLICK",
				};
			}


			@Override
			public Pos position() {
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
	public void onClick(PlayerClickVillagerNPCEvent e) {
		e.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
				.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
	}
}
