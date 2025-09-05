package net.swofty.type.bedwarslobby.item.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.HeadProfile;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Arrays;

public class ProfileItem extends BedWarsItem {

	public ProfileItem() {
		super("profile");
	}

	@Override
	public ItemStack getBlandItem() {
		return null;
	}

	@Override
	public ItemStack getItemStack(HypixelPlayer... p) {
		HypixelPlayer player = Arrays.stream(p).findFirst().orElseThrow();
		return ItemStackCreator.createNamedItemStack(Material.PLAYER_HEAD, "§aMy Profile §7(Right Click)")
				.set(DataComponents.PROFILE, new HeadProfile(player.getPlayerSkin())).build();
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