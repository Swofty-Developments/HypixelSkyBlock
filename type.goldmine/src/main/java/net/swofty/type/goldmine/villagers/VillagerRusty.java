package net.swofty.type.goldmine.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.type.generic.entity.villager.NPCVillagerParameters;
import net.swofty.type.goldmine.gui.GUIRusty;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.AbiphoneComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class VillagerRusty extends NPCVillagerDialogue {
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
		if (isInDialogue(player)) return;

		boolean hasSpokenBefore = player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_RUSTY);
		boolean isAboveSkyBlockLevel6 = player.getSkyBlockExperience().getLevel().getLevel() >= 6;

		if (!hasSpokenBefore) {
			if (isAboveSkyBlockLevel6) {
				setDialogue(player, "first-interaction-over-sb-6").thenRun(() -> {
					player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_RUSTY, true);
				});
			} else {
				setDialogue(player, "first-interaction-below-sb-6").thenRun(() -> {
					player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_RUSTY, true);
				});
			}
			return;
		}

		ItemStack itemStack = player.getItemInMainHand();
		SkyBlockItem item = new SkyBlockItem(itemStack);
		if (item.hasComponent(AbiphoneComponent.class)) {
			setDialogue(player, "abiphone").thenRun(() -> {
				// add to the Abiphone
			});
		}

		new GUIRusty().open(player);
	}

	@Override
	public DialogueSet[] getDialogueSets() {
		return Stream.of(
				NPCVillagerDialogue.DialogueSet.builder()
						.key("found-pickaxe").lines(new String[]{
								"§e[NPC] Rusty§f: You found the Lazy Miner's pickaxe!",
								"§e[NPC] Rusty§f: I'll have to put it in my stores.",
								"§e[NPC] Rusty§f: Click me again!"
						}).build(),
				NPCVillagerDialogue.DialogueSet.builder()
						.key("first-interaction-over-sb-6").lines(new String[]{
								"§e[NPC] Rusty§f: Hi, I’m the janitor of this mine.",
								"§e[NPC] Rusty§f: You would not believe how many people leave ingots and stones behind them!",
								"§e[NPC] Rusty§f: It drives me insane, but at least you unlocked §aAuto-pickup§f.",
								"§e[NPC] Rusty§f: It makes my job a lot easier, but despite that, I still find so many items on the ground.",
								"§e[NPC] Rusty§f: Maybe some of those items are yours? In which case I'll let you buy them back."
						}).build(),
				NPCVillagerDialogue.DialogueSet.builder()
						.key("first-interaction-below-sb-6").lines(new String[]{
								"§e[NPC] Rusty§f: Hi, I’m the janitor of this mine.",
								"§e[NPC] Rusty§f: You would not believe how many people leave ingots and stones behind them!",
								"§e[NPC] Rusty§f: It drives me insane, but at least you'll unlock §aAuto-pickup §fat §3SkyBlock Level 6.",
								"§e[NPC] Rusty§f: It makes my job a lot easier, but despite that, I still find so many items on the ground.",
								"§e[NPC] Rusty§f: Maybe some of those items are yours? In which case I'll let you buy them back."
						}).build(),
				NPCVillagerDialogue.DialogueSet.builder()
						.key("abiphone").lines(new String[]{ // when clicking with an Abiphone
								"§e[NPC] Rusty§f: §b✆ §fDid I find an Abiphone?",
								"§e[NPC] Rusty§f: §b✆ §fYes, sometimes I do find one lying around.",
								"§e[NPC] Rusty§f: §b✆ §fWhat?",
								"§e[NPC] Rusty§f: §b✆ §fYou?",
								"§e[NPC] Rusty§f: §b✆ §fYou want my contact?",
								"§e[NPC] Rusty§f: §b✆ §fMe?",
								"§e[NPC] Rusty§f: §b✆ §fThe janitor?",
								"§e[NPC] Rusty§f: §b✆ §fI...",
								"§e[NPC] Rusty§f: §b✆ §fI don't... don't know what to say...",
								"§e[NPC] Rusty§f: §b✆ §fYes of course you can have it!",
						}).build()
		).toArray(NPCVillagerDialogue.DialogueSet[]::new);
	}
}
