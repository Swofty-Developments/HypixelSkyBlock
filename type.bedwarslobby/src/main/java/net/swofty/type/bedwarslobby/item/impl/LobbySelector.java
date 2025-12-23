package net.swofty.type.bedwarslobby.item.impl;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgeneric.item.SimpleInteractableItem;
import net.swofty.type.bedwarslobby.gui.GUILobbySelector;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;

public class LobbySelector extends SimpleInteractableItem {

	public LobbySelector() {
		super("lobby_selector");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.getStack(
				"§aLobby Selector §7(Right Click)",
				Material.NETHER_STAR,
				1,
				"§7Right-click to switch between different lobbies!",
				"§7Use this to stay with your friends."
		).build();
	}

	@Override
	public void onItemDrop(ItemDropEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onItemInteract(PlayerInstanceEvent event) {
		((CancellableEvent) event).setCancelled(true);
		new GUILobbySelector().open((HypixelPlayer) event.getPlayer());
	}

}
