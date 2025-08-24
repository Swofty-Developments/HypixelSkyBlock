package net.swofty.type.bedwarslobby.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.type.bedwarsgeneric.game.GameType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIPlay extends HypixelInventoryGUI {

	private final GameType type;

	public GUIPlay(GameType type) {
		super("Play Bed Wars", InventoryType.CHEST_3_ROW);
		this.type = type;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		HypixelPlayer player = e.player();

		int playSlot = type == GameType.FOUR_FOUR ? 13 : 12;
		set(new GUIClickableItem(playSlot) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStack.builder(Material.OAK_SIGN).customName(
						Component.text("Bed Wars " + type.getDisplayName()).color(NamedTextColor.GREEN)
				).lore(
						Component.text("Play a game of Bed Wars " + type.getDisplayName()).color(NamedTextColor.GRAY),
						Component.space(),
						Component.text(lore()),
						Component.space(),
						Component.text("Click to play!").color(NamedTextColor.YELLOW)
				);
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				player.sendTo(ServerType.BEDWARS_GAME);
			}
		});


		if (type == GameType.FOUR_FOUR) return;

		set(new GUIClickableItem(14) {

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStack.builder(Material.OAK_SIGN).customName(
						Component.text("Map Selector " + type.getDisplayName()).color(NamedTextColor.GREEN)
				).lore(
						Component.text("Pick which map you want to play from").color(NamedTextColor.GRAY),
						Component.text("a list of available maps.").color(NamedTextColor.GRAY),
						Component.space(),
						Component.text("Click to browse!").color(NamedTextColor.YELLOW)
				);
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {

			}
		});
	}


	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {

	}

	private String lore() {
		return switch (type) {
			case SOLO ->
					"§7Fight against 7 other players!\nDestroy enemy beds to stop them\nfrom respawning!\nProtect your bed from destruction!";
			case DOUBLES ->
					"§7Team up with 1 other player to\ndefeat 7 enemy teams!\nDestroy enemy beds to stop them\nfrom respawning!\nProtect your bed from destruction!";
			case THREE_THREE_THREE_THREE ->
					"§7Team up with 2 other players to\ndefeat 3 other groups of players!\nDestroy enemy beds to stop them\nfrom respawning!\nProtect your bed from destruction!";
			case FOUR_FOUR_FOUR_FOUR ->
					"§7Team up with 3 other players to\ndefeat 3 other groups of players!\nDestroy enemy beds to stop them\nfrom respawning!\nProtect your bed from destruction!";
			case FOUR_FOUR -> "§74v4 is the classic Bed Wars\neveryone knows and lovees, but with\nonly 1 enemy team!";
			case ULTIMATE_DOUBLES -> "Ultimate Doubles";
			case ULTIMATE_FOURS -> "Ultimate 4v4v4v4";
		};
	}
}
