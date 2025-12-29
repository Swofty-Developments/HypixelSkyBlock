package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class GUIClaimReward extends HypixelInventoryGUI {

	private final ItemType rewardItem;
	private final Runnable onClaim;
	private boolean claimed = false;

	public GUIClaimReward(ItemType rewardItem, Runnable onClaim) {
		super("Claim Reward", InventoryType.CHEST_6_ROW);
		this.rewardItem = rewardItem;
		this.onClaim = onClaim;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		fill(FILLER_ITEM);
		set(new GUIClickableItem(22) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				claimed = true;
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				onClaim.run();
				SkyBlockItem item = new SkyBlockItem(rewardItem);
				player.addAndUpdateItem(item);
				player.sendMessage("§aYou claimed §f" + item.getDisplayName() + "§a!");
				p.closeInventory();
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.appendLore(
						new SkyBlockItem(rewardItem).getDisplayItem(),
						List.of(
								"",
								"§eClick to claim!"
						)
				);
			}
		});
		set(GUIClickableItem.getCloseItem(49));
		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onClose(InventoryCloseEvent e, CloseReason reason) {
		if (claimed) return;
		SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
		onClaim.run();
		player.addAndUpdateItem(rewardItem);
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {
		e.setCancelled(true);
	}
}
