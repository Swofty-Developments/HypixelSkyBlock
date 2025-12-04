package net.swofty.type.bedwarslobby.item.impl;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.bedwarslobby.gui.GUIGameMenu;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;

public class PlayCompass extends BedWarsItem {

	public PlayCompass() {
		super("play_compass");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.getSingleLoreStackLineSplit(
				"§aGame Menu §7(Right Click)",
				"§7",
				Material.COMPASS,
				1,
				"Right Click to bring up the Game Menu!"
		).build();
	}

	@Override
	public void onItemDrop(ItemDropEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onItemInteract(PlayerInstanceEvent event) {
		((CancellableEvent) event).setCancelled(true);
		new GUIGameMenu().open((HypixelPlayer) event.getPlayer());
	}

}
