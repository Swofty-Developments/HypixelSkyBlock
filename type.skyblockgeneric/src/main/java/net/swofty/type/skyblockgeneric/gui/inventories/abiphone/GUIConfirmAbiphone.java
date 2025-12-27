package net.swofty.type.skyblockgeneric.gui.inventories.abiphone;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.abiphone.AbiphoneNPC;

public class GUIConfirmAbiphone extends HypixelInventoryGUI {

	private final AbiphoneNPC npc;
	private final Runnable onAccept;

	public GUIConfirmAbiphone(AbiphoneNPC npc, Runnable onAccept) {
		super("Confirm", InventoryType.CHEST_3_ROW);
		this.npc = npc;
		this.onAccept = onAccept;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		set(new GUIClickableItem(11) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				onAccept.run();
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack("§aConfirm", Material.GREEN_TERRACOTTA, 1, "§eClick to remove " + npc.getName(), "§efrom your", "contacts!");
			}
		});
		set(new GUIClickableItem(15) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				player.closeInventory();
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.createNamedItemStack(Material.RED_TERRACOTTA, "§cCancel");
			}
		});
		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {

	}
}
