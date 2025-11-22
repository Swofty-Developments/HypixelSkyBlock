package net.swofty.type.bedwarslobby.item.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class PlayCompass extends BedWarsItem {

	public PlayCompass() {
		super("play_compass");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.createNamedItemStack(Material.COMPASS, "§aGame Menu §7(Right Click)").lore(
				Component.text("Right Click to bring up the Game Menu!", NamedTextColor.GRAY)
		).build();
	}

	@Override
	public void onItemDrop(ItemDropEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onItemInteract(PlayerInstanceEvent event) {
		((CancellableEvent) event).setCancelled(true);
		event.getPlayer().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
				.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
	}

}
