package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgeneric.item.SimpleInteractableItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class LeaveGameBed extends SimpleInteractableItem {

	public LeaveGameBed() {
		super("leave_game");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.createNamedItemStack(Material.RED_BED, "§cLeave").build();
	}

	@Override
	public void onItemUse(PlayerUseItemEvent event) {
		Game game = TypeBedWarsGameLoader.getPlayerGame(event.getPlayer());
		if (game != null) {
			game.leave((BedWarsPlayer) event.getPlayer());
		}
	}
}
