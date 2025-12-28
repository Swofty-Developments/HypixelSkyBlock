package net.swofty.type.goldmine.npcs.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.VillagerConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIReforge;
import net.swofty.type.skyblockgeneric.mission.missions.blacksmith.MissionTalkToBlacksmith;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class VillagerBlacksmith extends HypixelNPC {
	public VillagerBlacksmith() {
		super(new VillagerConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Blacksmith", "§e§lCLICK"};
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-39.50, 77.00, -299.50, -125f, 0f);
			}

			@Override
			public boolean looking() {
				return true;
			}

			@Override
			public VillagerProfession profession() {
				return VillagerProfession.WEAPONSMITH;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent e) {
		SkyBlockPlayer player = (SkyBlockPlayer) e.player();
		if (!player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_GOLD_BLACKSMITH)) {
			setDialogue(player, "initial-hello").thenRun(() -> {
				player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_GOLD_BLACKSMITH, true);
				new GUIReforge().open(player);
			});
		} else {
			new GUIReforge().open(player);
		}
	}

	@Override
	public DialogueSet[] dialogues(HypixelPlayer player) {
		return Stream.of(
				DialogueSet.builder()
						.key("initial-hello").lines(new String[]{
								"I'm a friend of Blacksmith in the §bVillage",
								"My name?",
								"Blacksmith"
						}).build()
		).toArray(DialogueSet[]::new);
	}
}
