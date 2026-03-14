package net.swofty.type.skyblockgeneric.abiphone.impl;

import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.ViewNavigator;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;
import net.swofty.type.skyblockgeneric.gui.inventories.shop.GUIShopAlda;

public class AbiphoneAlda extends AbiphoneNPC {

	public AbiphoneAlda() {
		super("alda", "§6Alda", "Sells §aAbiphones §7for beginners.");
	}

	@Override
	public void onCall(HypixelPlayer player) {
		player.openView(new GUIShopAlda());
	}

	@Override
	public ItemStack.Builder getIcon() {
		return ItemStackCreator.getStackHead("db5647f93fd8e1da9cdb151dd9bdf4f48bb59a1d11748f1918c136c86804b2");
	}
}
