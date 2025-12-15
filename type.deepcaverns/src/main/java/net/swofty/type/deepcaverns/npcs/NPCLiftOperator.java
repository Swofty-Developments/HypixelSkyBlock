package net.swofty.type.deepcaverns.npcs;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.deepcaverns.gui.GUILiftOperator;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.NPCParameters;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.MissionTalkToLiftOperator;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NPCLiftOperator extends HypixelNPC { // TODO: Honestly rewrite this entire class it sucks

	private final Map<HypixelPlayer, Map.Entry<DialogueSet, CompletableFuture<String>>> dialogueSets = new HashMap<>();

	private boolean isInDialogue(HypixelPlayer player) {
		return dialogueSets.containsKey(player);
	}

	private CompletableFuture<String> setDialogue(HypixelPlayer player, DialogueSet set) {
		CompletableFuture<String> future = new CompletableFuture<>();
		dialogueSets.put(player, Map.entry(set, future));
		handleLineSendingLoop(player, set);
		return future;
	}

	private void handleLineSendingLoop(HypixelPlayer player, DialogueSet dialogueSet) {
		player.sendMessage(dialogueSet.lines[0]);

		if (dialogueSet.lines.length == 1) {
			Map.Entry<DialogueSet, CompletableFuture<String>> entry = dialogueSets.remove(player);
			if (entry != null) {
				entry.getValue().complete(dialogueSet.key);
			}
			return;
		}

		String[] remaining = new String[dialogueSet.lines.length - 1];
		System.arraycopy(dialogueSet.lines, 1, remaining, 0, remaining.length);

		Scheduler scheduler = MinecraftServer.getSchedulerManager();
		scheduler.buildTask(() -> {
			handleLineSendingLoop(
					player,
					new DialogueSet(dialogueSet.key, remaining)
			);
		}).delay(TaskSchedule.seconds(2)).schedule();
	}

	private record DialogueSet(String key, String[] lines) {}

	public NPCLiftOperator() {
		super(new NPCParameters() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Lift Operator", "§e§lCLICK"};
			}

			@Override
			public String signature(HypixelPlayer player) {
				return "W8e6ip++dGgqglbYiPyLxCEHUXpOMioWrf6bCJwMNPHmEFTJux+M0H4GZaDjs1+d7FcSHjg6rEbMWS1sZAL4TUh7Q9vCtEYlx0PfnkY42l1Qqo/tIpJvgY5J6RHZ1j1cvtfrgXfKU8AKpjZVDNNyAqc5iJWcgqAm1gj3SPH6SoVvfzZgx9avAKo3z440CRsIhLYwgwEtq8/sbkqi0y6cuwlCZ+reo7yy2Ohe5AwG0Sx7Tkkv0DIVC4wO2RYoP+4xw2MYi1SRWk9yv3ZKqjwhTP8ugB/xqK/R480vVrr7MLhCpLrbUzpuLiaAbfruF9/TmvBV2hXFSCrlqypo4EmLk1E3WSuJX5ls11+juht0M12MIUlmmYcsFjgnAux5tgJ+fQq3KWhrbYedEY4CG7swavIG71/ZZI/ugRCXz/KONOq7bXKn939CyIpPdfBBAo2RZhjk2QXG/bWODeTfJ6z5VlIivNzo65FCAdwJ54VQzmUcP86ID3/jrWQ5fE5fhPkGhpYtOlVn5S4xKdFtu6RfYYUX6cEPD6MRPcOzXB4ZvCzgD7QhKxIQDBc1S9XY35c6+ZDYHPokQa87iD053Yfft4PH/pZA21ovOcf7+Xa+AFu72wnsjbynkaZUqSrFz3mCdOl+TFynZe89SwgDX0t1mIPtGtJmtK5jbZpwFA4bqDE=";
			}

			@Override
			public String texture(HypixelPlayer player) {
				return "eyJ0aW1lc3RhbXAiOjE1NTQ1NzE2OTkxOTAsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg2NTJhMDZmN2I5OWU0ODIyYjQ5NWJhOWI2MDg5ZDRkNTE1MWU4M2JlZDk1NmE3Y2NjYjg0N2VhNDZhMjU3MjUifX19";
			}

			@Override
			public Pos position(HypixelPlayer player) {
				final Pos[] positions = new Pos[]{
						new Pos(45.500, 150.000, 15.500, 90f, 0f),
						new Pos(45.500, 121.000, 15.500, 90f, 0f),
						new Pos(45.500, 101.000, 17.500, 90f, 0f),
						new Pos(45.500, 66.000, 15.500, 90f, 0f),
						new Pos(45.500, 38.000, 15.500, 90f, 0f),
						new Pos(45.500, 13.0, 15.500, 90f, 0f),
				};

				final Pos playerPos = player.getPosition();
				double bestDistSq = Double.POSITIVE_INFINITY;
				int closestIndex = 0;

				for (int i = 0; i < positions.length; i++) {
					Pos pos = positions[i];
					double dx = playerPos.x() - pos.x();
					double dy = playerPos.y() - pos.y();
					double dz = playerPos.z() - pos.z();
					double distSq = dx * dx + dy * dy + dz * dz;

					if (distSq < bestDistSq) {
						bestDistSq = distSq;
						closestIndex = i;
					}
				}

				return positions[closestIndex];
			}


			@Override
			public boolean looking() {
				return true;
			}
		});
	}

	@Override
	public void onClick(PlayerClickNPCEvent event) {
		SkyBlockPlayer player = (SkyBlockPlayer) event.player();
		MissionData data = player.getMissionData();

		if (isInDialogue(player)) return;

		if (!data.hasCompleted(MissionTalkToLiftOperator.class)) {
			if (!data.isCurrentlyActive(MissionTalkToLiftOperator.class)) {
				data.startMission(MissionTalkToLiftOperator.class);
			}

			setDialogue(player, new DialogueSet(
					"intro",
					new String[]{
							"§e[NPC] Lift Operator§f: Hey Feller!",
							"§e[NPC] Lift Operator§f: I control this lift here behind me.",
							"§e[NPC] Lift Operator§f: Once you've explored an area I can give you a safe ride back there.",
							"§e[NPC] Lift Operator§f: Be careful not to fall down the shaft though, it's a long fall!",
							"§e[NPC] Lift Operator§f: Good luck on your adventures."
					}
			)).thenRun(() -> data.endMission(MissionTalkToLiftOperator.class));

			return;
		}

		new GUILiftOperator().open(player);
	}
}