package net.swofty.type.thepark.npcs;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.ClaimRewardView;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce.race.*;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class NPCGustave extends HypixelNPC {

	public NPCGustave() {
		super(new HumanConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"§aGustave", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "hYkAj4Ea+VVW3eHacfAWRGEKXTVRPntdfC97KP6awi61bNDTPMocvoODrstFPSZMBxueybB1GhX5Z3RVBpo08EQk2P7QXHyUuFUreq/19L0UFsIUYPdnS4sJ5SU+Vva8dUMT8zEt65W63ToejHlCocioe7Q8c+1FttkvEG/M+3CzrbDBjvGKvTRSTQPoQxMNbtRCxQ+Jd4ek6TJsjNns7eUp0r/vHQZXcARUIMeo0doaKsWhGYCw9MUvNYj9olIlDEUxuWi1o+zBk1s23LsOZ/ZOeJYsJD+4cHQpTfkdDzIw1l9ZynkFJmxetcBe+y6CAjMuRJk/rNrIQ3NbRayFZF8RzfjqxpGUAJipYigP8zj0+mgYRQ1pwC8s4egVoS9a82RR9EOsXi5nWeMGfio9qn6DRPJpsmkjdn2WLsTsTEVqJD4VfZ+mduPZF0ZO69SnoO9YkkvAKGp9RkIRIlR3mZpDc2gV+01v8sgOuDh7Nii/0V7yeGweSHYUy9Yd7wl9pEN4UIxoaQdOTQ4RrjeYX46gc11cSqqtipb9c/2PsiUT7fA12AhalC4n6+0enW7i/KO8J+UqDVjGJ4Ao+viAQHqee/7RtR5dIbVFjIPZrZbMDE8Uqluf9LWEPAC46PH9Z4EvGFd1827n99XLGltB2IE3lKEEQWbqTcMDZ/oH90M=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "eyJ0aW1lc3RhbXAiOjE1NjQxODQ0NjgzODUsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NmYWZlNmY1MDE0YzUyYzBlOTczNTkyOTg0OTA2YWUxZDE5ZTgxMDVlMmRjZmQ3ZWFjYmUwMWVmODdjZTMyMGMifX19";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-363.5, 89, 44.5, -135, 0);
			}

			@Override
			public boolean looking(HypixelPlayer player) {
				return true;
			}

			@Override
			public @NonNull String chatName() {
				return "§aGustave";
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		if (isInDialogue(player)) return;

		MissionData missionData = player.getMissionData();
		if (missionData.isCurrentlyActive(MissionTalkToGustave.class)) {
			setDialogue(player, "first-interaction").thenRun(() -> {
				missionData.endMission(MissionTalkToGustave.class);
			});
			return;
		}
		if (missionData.isCurrentlyActive(MissionCompleteTheRaceTwoMinutes.class)) {
			sendNPCMessage(player, "Run over the pressure plate to start racing! Run up to the §eopposite §fside of the island AND back to complete the race. Can you finish it in §e2 minutes§f?");
			return;
		}
		if (missionData.isCurrentlyActive(MissionTalkToGustaveAgain.class)) {
			setDialogue(player, "completed-1").thenRun(() -> {
				// TODO: give polished pebble
				missionData.endMission(MissionTalkToGustaveAgain.class);
			});
			return;
		}
		if (missionData.isCurrentlyActive(MissionCompleteTheRaceOneMinute.class)) {
			sendNPCMessage(player, "Keep it up! Can you finish the race in 1 minute or less?");
			return;
		}
		if (missionData.isCurrentlyActive(MissionTalkToGustaveAgainAgain.class)) {
			setDialogue(player, "completed-2").thenRun(() -> {
				player.openView(new ClaimRewardView(), new ClaimRewardView.State(ItemType.HUNTER_KNIFE, () -> {
					missionData.endMission(MissionTalkToGustaveAgainAgain.class);
				}));
			});
			return;
		}
		if (missionData.isCurrentlyActive(MissionCompleteTheRaceThird.class)) {
			sendNPCMessage(player, "Keep at it! Can you finish the race in 32 seconds or less?");
			return;
		}
		if (missionData.isCurrentlyActive(MissionTalkToGustaveFourth.class)) {
			setDialogue(player, "completed-3").thenRun(() -> {
				// TODO: give trinket (wolf paw)
				missionData.endMission(MissionTalkToGustaveFourth.class);
			});
			return;
		}
		if (missionData.isCurrentlyActive(MissionCompleteTheRaceFourth.class)) {
			sendNPCMessage(player, "Real challenge! Can you finish the race in 18 seconds or less and beat my personal record?");
			return;
		}
		if (missionData.isCurrentlyActive(MissionTalkToGustaveFifth.class)) {
			setDialogue(player, "completed-4").thenRun(() -> {
				// TODO: give silky lichen
				missionData.endMission(MissionTalkToGustaveFifth.class);
			});
			return;
		}
		setDialogue(player, "idle-" + (1 + (int) (Math.random() * 2)));
	}

	@Override
	protected DialogueSet[] dialogues(HypixelPlayer player) {
		return List.of(
				DialogueSet.builder().key("first-interaction").lines(new String[]{
						"There's nothing like island racing!",
						"I've traveled many islands and this is my favorite to race on. Want to try?",
						"To complete the race, you'll need to reach the §eopposite side of the island §fAND come all the way back.",
						"Start the race by walking over the pressure plate. If you finish in under 2 minutes, I'll reward you for it! Good luck!",
						"The other end of the race can be found at -407 128 -119 and is also marked by a beacon.",
						"There are also crit-particle trails to follow."
				}).build(),
				DialogueSet.builder().key("not-too-fast").lines(new String[]{
						"Hey! Come talk to me over here! I'll tell you all about racing!"
				}).build(),
				DialogueSet.builder().key("completed-1").lines(new String[]{
						"Not bad, " + LegacyComponentSerializer.legacySection().serialize(player.getColouredName()) + "§f!",
						"I hope you can get better than this!",
						"Here's a §aPolished Pebble§f. I found it laying around...",
						"Try again, but this time come back in 1 minute or less!"
				}).build(),
				DialogueSet.builder().key("completed-2").lines(new String[]{
						"You're getting much faster!",
						"Use my §aHunter Knife§f. Holding it somehow makes me go faster.",
						"See if you can beat 32 seconds!"
				}).build(),
				DialogueSet.builder().key("completed-3").lines(new String[]{
						"Now we're cooking! You're getting close to my record!",
						"Here's an trinket I've found in the caverns behind the waterfall.",
						"Finish the race in under 18 seconds and come talk to me!"
				}).build(),
				DialogueSet.builder().key("completed-4").lines(new String[]{
						"You did it! Congratulations, " + LegacyComponentSerializer.legacySection().serialize(player.getColouredName()) + "§f. You are now faster than §aGustave§f!",
						"I've been racing this island so fast that some bark from the trees snatched right off!",
						"Here's a rare piece of §9Silky Lichen§f. Maybe you can do something with it.",
						"I have this lichen could improve some weapons!"
				}).build(),
				DialogueSet.builder().key("idle-1").lines(new String[]{
						"You're always welcome to try the race again. Just run over the pressure plate to begin!"
				}).build(),
				DialogueSet.builder().key("idle-2").lines(new String[]{
						"It's a great day to go for a run!"
				}).build()
		).toArray(DialogueSet[]::new);
	}
}
