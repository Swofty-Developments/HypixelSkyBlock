package net.swofty.type.skyblockgeneric.event.actions.npc;

import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.NPCAbiphoneTrait;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneRegistry;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.AbiphoneComponent;
import org.tinylog.Logger;

public class ActionPlayerInteractNPC implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
	public void run(NPCInteractEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		HypixelNPC npc = event.getNpc();
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
				item.getAttributeHandler().addAbiphoneNPC(abiphoneNPC);
				player.sendMessage("§b✆ " + abiphoneNPC.getName() + " §fhas been added to your Abiphone's contacts!");
			} else {
				Logger.warn("NPC " + npc.getClass().getName() + " has an invalid Abiphone key: " + trait.getAbiphoneKey());
			}
			event.setCancelled(true);
		}
	}

}
