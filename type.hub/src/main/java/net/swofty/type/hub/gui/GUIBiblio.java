package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIBiblio extends HypixelInventoryGUI {
	public GUIBiblio() {
		super("Skyblock Wiki", InventoryType.CHEST_4_ROW);
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
		set(GUIClickableItem.getCloseItem(31));
		set(new GUIClickableItem(11) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(Component.text("§7Click §e§lHERE §7to visit the §6Official SkyBlock Wiki§7!§r")
						.clickEvent(ClickEvent.openUrl("https://wiki.hypixel.net")));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				return ItemStackCreator.getStack("§dWiki Command", Material.PAINTING, 1,
						"§7Visit the Wiki using §a/wiki §7and browse", "§7the many pages and utilities.",
						"", "§7You can also specify an extra", "§7argument when using §6/wiki <id> §7to",
						"§7search via an item ID.", "", "§eClick to visit the Wiki!");
			}
		});
		set(new GUIClickableItem(13) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;

			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				return ItemStackCreator.getStack("§6The Skyblock Wiki", Material.WRITABLE_BOOK, 1,
						"§7The newly finished §aOfficial SkyBlock", "§aWiki §7has launched and contains lots",
						"§7of useful information on items, mobs,", "§7drop rates, areas, trivia, and more.",
						"§7This is a §6Hypixel-led§7, §dcommunity", "§dmaintained §7Wiki which aims to provide",
						"§7the most accurate information in the", "§7best way possible.", "§8Edits",
						" §6> 100,000+", "", "§8Pages", " §d> 27,000+", "", "§8Files", " §a> 15,000+", "",
						"§eSee more @ §fwiki.hypixel.net");
			}
		});
		set(new GUIClickableItem(15) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				player.sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
						.clickEvent(ClickEvent.openUrl("https://github.com/Swofty-Developments/HypixelSkyBlock")));
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer p) {
				SkyBlockPlayer player = (SkyBlockPlayer) p;
				return ItemStackCreator.getStack("§aWikithis Command", Material.OAK_SIGN, 1,
						"§7Want to view more information about",
						"§7the item you are currently §dholding §7?", "§7Then this is the command for §eyou§7!",
						"", "§7Running §6/wikithis §7whilst §aholding an", "§aitem §7will attempt to find a Wiki page",
						"§7for the item and then link you to it", "§7in-game.", "", "§eClick to search your held item!");
			}
		});
		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onClose(InventoryCloseEvent e, CloseReason reason) {
	}

	@Override
	public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {
		e.setCancelled(true);
	}
}

