package net.swofty.type.skyblockgeneric.abiphone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.components.AbiphoneComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@Getter
@AllArgsConstructor
public abstract class AbiphoneNPC {
	private final String id;
	private final String name;
	private final String description;
	private final AbiphoneDialogueController dialogueController = new AbiphoneDialogueController(this);

	public abstract void onCall(HypixelPlayer player);

	public void onAdd(SkyBlockPlayer player, int slot) {
		player.updateItemInSlot(slot, (i) -> {
			if (!i.hasComponent(AbiphoneComponent.class)) return;
			i.getAttributeHandler().addAbiphoneNPC(this);
			player.sendMessage("§b✆ " + getName() + " §fhas been added to your Abiphone's contacts!");
		});
	}

	protected AbiphoneDialogueController dialogue() {
		return dialogueController;
	}

	public DialogueSet[] dialogues(HypixelPlayer player) {
		return DialogueSet.EMPTY;
	}

	public abstract ItemStack.Builder getIcon();

	public void sendNPCMessage(HypixelPlayer player, String message) {
		player.sendMessage("§e[NPC] " + getName() + "§f: §b✆ §f" + message);
	}

	@Builder
	public record DialogueSet(String key, String[] lines, boolean abiPhone) {
		public static final DialogueSet[] EMPTY = new DialogueSet[0];
	}

}
