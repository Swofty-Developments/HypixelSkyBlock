package net.swofty.type.deepcaverns.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUILiftOperator extends HypixelInventoryGUI {
	public GUILiftOperator() {
		super("Lift", InventoryType.CHEST_6_ROW);
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent event) {
		fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));


		set(new GUIClickableItem(10) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.createNamedItemStack(Material.GOLD_INGOT);
			}

			@Override
			public void run(InventoryPreClickEvent event, HypixelPlayer player) {

			}
		});

		set(new GUIClickableItem(12) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.createNamedItemStack(Material.LAPIS_LAZULI);
			}

			@Override
			public void run(InventoryPreClickEvent event, HypixelPlayer player) {

			}
		});

		set(new GUIClickableItem(14) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.createNamedItemStack(Material.REDSTONE);
			}

			@Override
			public void run(InventoryPreClickEvent event, HypixelPlayer player) {

			}
		});

		set(new GUIClickableItem(16) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.createNamedItemStack(Material.EMERALD);
			}

			@Override
			public void run(InventoryPreClickEvent event, HypixelPlayer player) {

			}
		});

		set(new GUIClickableItem(29) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.createNamedItemStack(Material.DIAMOND);
			}

			@Override
			public void run(InventoryPreClickEvent event, HypixelPlayer player) {

			}
		});

		set(new GUIClickableItem(31) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.createNamedItemStack(Material.OBSIDIAN);
			}

			@Override
			public void run(InventoryPreClickEvent event, HypixelPlayer player) {

			}
		});

		set(new GUIClickableItem(33) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.createNamedItemStack(Material.PRISMARINE);
			}

			@Override
			public void run(InventoryPreClickEvent event, HypixelPlayer player) {

			}
		});

		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {

	}
}
