package net.swofty.type.generic.entity.npc;

import lombok.Builder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class NPCDialogue extends HypixelNPC {
	HashMap<HypixelPlayer, Map.Entry<DialogueSet, CompletableFuture<String>>> dialogueSets = new HashMap<>();

	protected NPCDialogue(NPCParameters parameters) {
		super(parameters);
	}

	public static void remove(HypixelPlayer player) {
		for (HypixelNPC npc : getNpcs()) {
			if (npc instanceof NPCDialogue dialogue) {
				if (dialogue.isInDialogue(player)) {
					dialogue.dialogueSets.get(player).getValue().complete(null);
					dialogue.dialogueSets.remove(player);
				}
			}
		}
	}

	public abstract DialogueSet[] getDialogueSets(HypixelPlayer player);

	public Boolean isInDialogue(HypixelPlayer player) {
		Map.Entry<DialogueSet, CompletableFuture<String>> dialogueSet = dialogueSets.get(player);
		return dialogueSet != null;
	}

	public CompletableFuture<String> setDialogue(HypixelPlayer player, String key) {
		CompletableFuture<String> future = new CompletableFuture<>();

		for (DialogueSet dialogueSet : getDialogueSets(player)) {
			if (dialogueSet.key().equals(key)) {
				dialogueSets.put(player, Map.entry(dialogueSet, future));
				handleLineSendingLoop(player, dialogueSet);
				return future;
			}
		}

		future.completeExceptionally(new NullPointerException("Dialogue set with key '" + key + "' not found."));
		return future;
	}

	private void handleLineSendingLoop(HypixelPlayer player, DialogueSet dialogueSet) {
		if (dialogueSet.abiPhone) {
			sendNPCAbiphoneMessage(player, dialogueSet.lines()[0]);
		} else {
			sendNPCMessage(player, dialogueSet.lines()[0]);
		}

		String[] newLines = new String[dialogueSet.lines().length - 1];
		System.arraycopy(dialogueSet.lines(), 1, newLines, 0, dialogueSet.lines().length - 1);

		if (newLines.length == 0) {
			dialogueSets.get(player).getValue().complete(dialogueSet.key());
			dialogueSets.remove(player);
			return;
		}

		Scheduler scheduler = MinecraftServer.getSchedulerManager();
		scheduler.buildTask(() -> {
			handleLineSendingLoop(player, DialogueSet.builder().key(dialogueSet.key()).lines(newLines).build());
		}).delay(TaskSchedule.seconds(2)).schedule();
	}

	@Builder
	public record DialogueSet(String key, String[] lines, boolean abiPhone) {
	}


}
