package net.swofty.type.thepark.npcs;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.NPCOption;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.jungle.*;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class NPCMolbert extends HypixelNPC {

	public NPCMolbert() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Molbert", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "W0e+/EVqkTyw3gQZgRQbswV8EA1K/YE557v6VpVu/a0B7XzcMMtS8aUyzsljpBiXM3pjPo36D4Wjq45AB/Ij8GaCV2M491lTk0OLzZnRYvGOZ0HvT8K0TdWkOZQtFfh45IX97yvrnfSx1P5XYvJxXSBCKFn6lG9CpYT4Oy5RQG2ARiBkwq3NNGbgJFEfgMQl5KyszgyWCvyXsEQJo9UVYvlGLgrMML+/bORdDSzlnm3R8/tQtCbCfKfRNKlNiv03c02vDGrWDdCB6JYXd+58V5uRwP4NrzSer6GTd59sHLNx1lWMvvag/5m+1A+XFaBTs6wK9m6PB5AVEm9ROhIHjKRpuDjUBHnKnc/docuV+JKlqu3z1BQ/nUccznZmOvKJ8eewtawtfd4/HalVsIPrE5f4nUoH7C1t50xO20fZ3Z7B7hurdSwJEuP1vV1AxhM21Y8ux42yQOqQ5l0pYeIPJTIs99MUaYJYqdWJll1Mp3AHuNelDbTVBvqzt0wtFvN5I5ULoeJrbV7t1PmCUJIP6pcyg9wGPRBW5ZauR2sp8TRNhrZOCRTiaWST+XbVnw7HkprqaoHqutxyascDQej38AknySBbMzcCZYS8CEwQYfMTbW7E/6SO5USapx0K29t+nh1ZOvfx2mI8hFW/qH5HFP2FhyZ1dSISfpWDCq7LWUg=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "ewogICJ0aW1lc3RhbXAiIDogMTU5MDQ1OTI1MTYwOCwKICAicHJvZmlsZUlkIiA6ICI3M2ZkNzU2NWJkZTY0MGQzYWE4MGUxMWUwMTMwMjc3OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJHYUJySWVMVnR6IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2YyMDc4M2UyNzQzYjg5NzZiMDBmODI3ZWUyODM2NzVkNzkxMTE0MGM5ODI4NDIzOWY2YmMxYzhkZDFkMjdjODEiCiAgICB9CiAgfQp9";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				if (!((SkyBlockPlayer) player).getMissionData().hasCompleted(MissionPlaceTraps.class)) {
					return new Pos(-465.500, 120.000, -42.500, 10, 0);
				}
				return new Pos(-447.5, 120, -63.5, 45, 0);
			}

			@Override
			public boolean looking(HypixelPlayer player) {
				return true;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		if (isInDialogue(player)) return;

		MissionData data = player.getMissionData();
		if (data.isCurrentlyActive(MissionTalkToMolbert.class)) {
			String key = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SAID_MOLBERT_IS_MOLE)
					? "quick-intro" : "intro";
			setDialogue(player, key).thenRun(() -> {
				NPCOption.sendOption(player, "molbert", true, List.of(
						new NPCOption.Option(
								"yes",
								NamedTextColor.GREEN,
								false,
								"Sure...",
								(p) -> {
									setDialogue(player, "option-sure").thenRun(() -> {
										data.endMission(MissionTalkToMolbert.class);
									});
								}
						),
						new NPCOption.Option(
								"no",
								NamedTextColor.RED,
								false,
								"You look like a mole yourself",
								(p) -> {
									setDialogue(player, "option-mole").thenRun(() -> {
										player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SAID_MOLBERT_IS_MOLE, true);
									});
								}
						)
				));
			});
			return;
		}

		if (data.isCurrentlyActive(MissionCollectJungleLogs.class)) {
			sendNPCMessage(player, "If you can bring me §a512 Jungle Logs§f, we can make some §5traps§f!");
			return;
		}

		if (data.isCurrentlyActive(MissionGiveMolbertJungleLogs.class)) {
			if (!player.removeItemFromPlayer(ItemType.JUNGLE_LOG, 512)) {
				sendNPCMessage(player, "You don't have §a512 Jungle Logs§f on you.");
				return;
			}

			setDialogue(player, "after-resources").thenRun(() -> {
				data.endMission(MissionGiveMolbertJungleLogs.class);
			});
			return;
		}

		if (data.isCurrentlyActive(MissionLeaveTheArea.class)) {
			sendNPCMessage(player, "Come back later.");
			return;
		}

		if(data.isCurrentlyActive(MissionTalkToMolbertAgain.class)) {
			setDialogue(player, "after-coming-back").thenRun(() -> {
				data.endMission(MissionTalkToMolbertAgain.class);
			});
			return;
		}

		if (data.isCurrentlyActive(MissionPlaceTraps.class)) {
			setDialogue(player, "after-coming-back");
			return;
		}

	}

	@Override
	protected DialogueSet[] dialogues(HypixelPlayer player) {
		return List.of(
				DialogueSet.builder().key("intro").lines(new String[]{
						"§6Moles§f, you see. They have been burrowing everywhere, their tunnels are disrupting the landscape, they ruined the §5§obeauty §fof this place. It's gotten hard to ignore.",
						"You need an experienced person to handle the situation.",
						"Those troublesome creatures are ruining everything and must be dealt with. Do you understand?",
						"This park deserves better and I want see it flourish like it used to be.",
						"Let us keep this between us, and I will §agenerously reward §fyou for your efforts.",
						"The moles must be dealt with, and I am sure you will do so quickly and quietly."
				}).build(),
				DialogueSet.builder().key("option-mole").lines(new String[]{
						"§c§lRidiculous! §fHow could I be a mole if I am §bwearing human clothes§f, huh?",
						"Have you seen moles wearing human clothes before?",
						"I don't think so. Now get back to the issue!"
				}).build(),
				DialogueSet.builder().key("quick-intro").lines(new String[]{
						"The moles must be dealt with, and I am sure you will do so quickly and quietly."
				}).build(),
				DialogueSet.builder().key("option-sure").lines(new String[]{
						"Great. First thing we need is to build some §5traps§f.",
						"For that however, I need §a512 Jungle Logs§f, should be enough to get things going.",
						"Moles §6loooove carrots§f, they are just too good to resist.",
						"Don't worry, I've got a stash ready.",
						"Just thinking about them §omakes me§f... Uhm I mean §omakes them §fcome out of their hiding space."
				}).build(),
				DialogueSet.builder().key("after-resources").lines(new String[]{
						"Fantastic, that's all I needed to build the §5traps§f.",
						"It will take some time to assemble them, so you should §acome back later§f."
				}).build(),
				DialogueSet.builder().key("after-coming-back").lines(new String[]{
						"The §5traps §fare ready for use; All that remains is to set them up in the §aright place§f. Once you find the ideal spots, go ahead and deploy them."
				}).build()
		).toArray(DialogueSet[]::new);
	}
}
