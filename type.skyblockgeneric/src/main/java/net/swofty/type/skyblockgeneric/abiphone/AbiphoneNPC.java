package net.swofty.type.skyblockgeneric.abiphone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.components.AbiphoneComponent;
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
		sendNPCMessage(player, Component.text(message));
	}

	public void sendNPCMessage(HypixelPlayer player, Component message) {
		player.sendMessage(Component.text()
			.append(Component.text("[NPC] ", NamedTextColor.YELLOW))
			.append(Component.text(getName(), NamedTextColor.YELLOW))
			.append(Component.text(": ", NamedTextColor.WHITE))
			.append(Component.text("✆ ", NamedTextColor.AQUA))
			.append(message)
			.build());
	}

	@Builder
	public record DialogueSet(String key, Component[] lines, boolean abiPhone) {
		public static final DialogueSet[] EMPTY = new DialogueSet[0];

		public static class DialogueSetBuilder {
			public DialogueSetBuilder lines(Component[] lines) {
				this.lines = lines;
				return this;
			}

			public DialogueSetBuilder lines(String[] lines) {
				if (lines == null) {
					this.lines = null;
					return this;
				}

				Component[] components = new Component[lines.length];
				for (int i = 0; i < lines.length; i++) {
					components[i] = Component.text(lines[i]);
				}
				this.lines = components;
				return this;
			}
		}
	}

}
