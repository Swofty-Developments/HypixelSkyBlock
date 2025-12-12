package net.swofty.type.goldmine.villagers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.entity.villager.NPCVillagerParameters;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class VillagerRusty extends HypixelVillagerNPC {
	public VillagerRusty() {
		super(new NPCVillagerParameters() {
			@Override
			public String[] holograms() {
				return new String[]{"Rusty", "§e§lCLICK"};
			}

			@Override
			public Pos position() {
				return new Pos(-20.00, 78.00, -325.41, -50f, 0f);
			}

			@Override
			public boolean looking() {
				return true;
			}

			@Override
			public VillagerProfession profession() {
				return VillagerProfession.LIBRARIAN;
			}
		});
	}

	@Override
	public void onClick(PlayerClickVillagerNPCEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.player();
		player.sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
				.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
	}
}
