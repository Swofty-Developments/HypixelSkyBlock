package net.swofty.type.skyblockgeneric.abiphone.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIRusty;

public class AbiphoneRusty extends AbiphoneNPC {

	public AbiphoneRusty() {
		super("rusty", "Rusty", "SkyBlock's Janitor");
	}

	@Override
	public void onCall(HypixelPlayer player) {
		new GUIRusty().open(player);
	}

	@Override
	public ItemStack.Builder getIcon() {
		return ItemStack.builder(Material.VILLAGER_SPAWN_EGG);
	}

}
