package net.swofty.type.skyblockgeneric.abiphone;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;

public class AbiphoneRegistry {
	@Getter
	private static final List<AbiphoneNPC> registeredContactNPCs = new ArrayList<>();

	public static void registerContact(AbiphoneNPC npc) {
		registeredContactNPCs.add(npc);
	}

	public static AbiphoneNPC getFromId(String id) {
		for (AbiphoneNPC npc : registeredContactNPCs) {
			if (npc.getId().equalsIgnoreCase(id)) {
				return npc;
			}
		}
		return null;
	}

}
