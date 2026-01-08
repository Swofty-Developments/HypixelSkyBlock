package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.lobby.LobbyOrchestratorConnector;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.commons.party.FullParty;

public class GUIPlay extends HypixelInventoryGUI {

	private final BedwarsGameType type;

	public GUIPlay(BedwarsGameType type) {
		super("Play Bed Wars", InventoryType.CHEST_4_ROW);
		this.type = type;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		int playSlot = type == BedwarsGameType.FOUR_FOUR ? 13 : 12;
		set(new GUIClickableItem(playSlot) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getSingleLoreStackLineSplit(
						"§aBed Wars " + type.getDisplayName(), "§7",
						Material.RED_BED, 1,
						"§7Play a game of Bed Wars " + type.getDisplayName() + "\n\n" + lore() + "\n\n§eClick to play!"
				);
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				player.closeInventory();

				if (LobbyOrchestratorConnector.isSearching(player.getUuid())) {
					player.sendMessage("§cYou are already searching for a game!");
					return;
				}

				// Party check - non-leaders cannot queue
				if (PartyManager.isInParty(player)) {
					FullParty party = PartyManager.getPartyFromPlayer(player);
					if (party != null && !party.getLeader().getUuid().equals(player.getUuid())) {
						player.sendMessage("§cYou are in a party! Ask your leader to start the game, or /p leave");
						return;
					}
				}

				LobbyOrchestratorConnector connector = new LobbyOrchestratorConnector(player);
				connector.sendToGame(ServerType.BEDWARS_GAME, type.toString());
			}
		});


		if (type != BedwarsGameType.FOUR_FOUR) {
			set(new GUIClickableItem(14) {

				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					return ItemStackCreator.getStack("§aMap Selector " + type.getDisplayName(),
							Material.OAK_SIGN, 1,
							"§7Pick which map you want to play from",
							"§7a list of available maps.",
							"",
							"§eClick to browse!");
				}

				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer player) {
					new GUIMapSelection(type).open(player);
				}
			});
		}

		set(GUIClickableItem.getCloseItem(31));
		updateItemStacks(getInventory(), getPlayer());
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
					"Fight against 7 other players!\nDestroy enemy beds to stop them\nfrom respawning!\nProtect your bed from destruction!";
			case DOUBLES ->
					"Team up with 1 other player to\ndefeat 7 enemy teams!\nDestroy enemy beds to stop them\nfrom respawning!\nProtect your bed from destruction!";
			case THREE_THREE_THREE_THREE ->
					"Team up with 2 other players to\ndefeat 3 other groups of players!\nDestroy enemy beds to stop them\nfrom respawning!\nProtect your bed from destruction!";
			case FOUR_FOUR_FOUR_FOUR ->
					"Team up with 3 other players to\ndefeat 3 other groups of players!\nDestroy enemy beds to stop them\nfrom respawning!\nProtect your bed from destruction!";
			case FOUR_FOUR -> "4v4 is the classic Bed Wars\neveryone knows and lovees, but with\nonly 1 enemy team!";
			case ULTIMATE_DOUBLES -> "Ultimate Doubles";
			case ULTIMATE_FOURS -> "Ultimate 4v4v4v4";
		};
	}
}
