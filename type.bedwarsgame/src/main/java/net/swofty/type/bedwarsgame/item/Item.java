package net.swofty.type.bedwarsgame.item;

import lombok.Getter;

/**
 * WIP: Represents a custom item in a BedWars game. Handles in-game events and properties of items.
 */
@Getter
public abstract class Item {

	private final String id;

	public Item(String id) {
		this.id = id;
	}

	public abstract void getItemStack();

}
