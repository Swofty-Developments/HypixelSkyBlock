package net.swofty.type.skyblockgeneric.event.actions.npc;

import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.trait.NPCAbiphoneTrait;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneRegistry;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointGardenPersonal;
import net.swofty.type.skyblockgeneric.garden.GardenData;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.AbiphoneComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class ActionPlayerInteractNPC implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
	public void run(NPCInteractEvent event) {
		final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		HypixelNPC npc = event.getNpc();
		markNpcInteraction(player, npc);
		SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
		if (item.hasComponent(AbiphoneComponent.class)) {
			if (!(npc instanceof NPCAbiphoneTrait trait)) {
				player.sendMessage("§7[§b✆§7] §7This NPC doesn't own an Abiphone...");
				event.setCancelled(true);
				return;
			}
			AbiphoneNPC abiphoneNPC = AbiphoneRegistry.getFromId(trait.getAbiphoneKey());
			if (abiphoneNPC != null) {
				if (item.getAttributeHandler().hasAbiphoneNPC(abiphoneNPC)) {
					player.sendMessage("§7[§b✆§7] §7This NPC is already in your contacts list!");
					return;
				}
				abiphoneNPC.onAdd(player, player.getHeldSlot());
			} else {
				Logger.warn("NPC " + npc.getClass().getName() + " has an invalid Abiphone key: " + trait.getAbiphoneKey());
			}
			event.setCancelled(true);
		}
	}

	private static void markNpcInteraction(SkyBlockPlayer player, HypixelNPC npc) {
		GardenData.GardenPersonalData personal = player.getSkyblockDataHandler()
			.get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.GARDEN_PERSONAL, DatapointGardenPersonal.class)
			.getValue();
		aliasesFor(npc).forEach(alias -> {
			if (!alias.isBlank()) {
				personal.getSpokenNpcFlags().add(alias);
			}
		});
	}

	private static Set<String> aliasesFor(HypixelNPC npc) {
		Set<String> aliases = new LinkedHashSet<>();
		aliases.add(normalize(npc.getName()));
		String simpleName = npc.getClass().getSimpleName()
			.replaceFirst("^NPC", "")
			.replaceFirst("^Villager", "");
		aliases.add(normalize(simpleName.replaceAll("(?<=.)(?=\\p{Lu})", " ")));
		if (simpleName.endsWith("TheTrapper")) {
			aliases.add("TREVOR");
		}
		if (simpleName.endsWith("LumberJack")) {
			aliases.add("LUMBERJACK");
		}
		return aliases;
	}

	private static String normalize(String value) {
		if (value == null) {
			return "";
		}
		return value.replaceAll("§.", "")
			.trim()
			.replaceAll("[^A-Za-z0-9]+", "_")
			.replaceAll("^_+|_+$", "")
			.toUpperCase(Locale.ROOT);
	}

}
