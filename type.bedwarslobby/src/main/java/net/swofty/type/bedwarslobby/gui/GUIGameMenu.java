package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIGameMenu extends HypixelInventoryGUI {

	public GUIGameMenu() {
		super("Game Menu", InventoryType.CHEST_6_ROW);
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		set(new GUIClickableItem(12) {

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStackHead("§aSkyblock §8§l0.23.5 §5DUNGEONBREAKER + PITY", "d7cc6687423d0570d556ac53e0676cb563bbdd9717cd8269bdebed6f6d4e7bf8", 1,
						"§8Persistent Game",
						" ",
						"§7SkyBlock has finally arrived on",
						"§7Hypixel! Play with friends (or solo!),",
						"§7build your private islands and",
						"§7collect all the items!",
						" ",
						"§a► Click to Connect",
						"§70 currently playing!"
				);
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				player.sendTo(ServerType.SKYBLOCK_HUB);
			}
		});

		set(new GUIClickableItem(14) {

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack("§aBed Wars §6§lv.1.10 — DREAM MODE & QOL", Material.RED_BED, 1,
						"§8Team Survival",
						" ",
						"§7Protect your bed along with your",
						"§7teammates and destroy enemy beds",
						"§7to win!",
						" ",
						"§a► Click to Connect",
						"§70 currently playing!"
				);
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				player.sendTo(ServerType.BEDWARS_LOBBY);
			}
		});

		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {
		e.setCancelled(true);
	}
}
