package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.BedwarsGameType;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.GetMapsProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.bedwarslobby.OrchestratorConnector;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIMapSelection extends HypixelInventoryGUI {

	private final BedwarsGameType gameType;
	private List<String> maps = new ArrayList<>();
	private boolean mapsLoaded = false;

	public GUIMapSelection(BedwarsGameType gameType) {
		super("Map Selection - " + gameType.getDisplayName(), InventoryType.CHEST_4_ROW);
		this.gameType = gameType;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		HypixelPlayer player = e.player();

		if (!mapsLoaded) {
			// Show loading message
			set(new GUIClickableItem(13) {
				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					return ItemStackCreator.getStack("§eLoading maps...",
							Material.CLOCK, 1,
							"§7Please wait while we fetch",
							"§7available maps for " + gameType.getDisplayName());
				}

				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer player) {
					// No action while loading
				}
			});

			loadMaps(player);
		} else {
			populateMaps(player);
		}

		updateItemStacks(getInventory(), getPlayer());
	}

	private void loadMaps(HypixelPlayer player) {
		ProxyService orchestratorService = new ProxyService(ServiceType.ORCHESTRATOR);

		GetMapsProtocolObject.GetMapsMessage message =
				new GetMapsProtocolObject.GetMapsMessage(ServerType.BEDWARS_GAME, gameType.toString());

		orchestratorService.handleRequest(message)
				.thenAccept(response -> {
					if (response instanceof GetMapsProtocolObject.GetMapsResponse mapsResponse) {
						maps = mapsResponse.maps();
						mapsLoaded = true;

						// Refresh the GUI with the loaded maps
						populateMaps(player);
						updateItemStacks(getInventory(), player);
					}
				})
				.exceptionally(throwable -> {
					player.sendMessage("§cFailed to load maps: " + throwable.getMessage());
					player.closeInventory();
					return null;
				});
	}

	private void populateMaps(HypixelPlayer player) {
		if (maps.isEmpty()) {
			set(new GUIClickableItem(13) {
				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					return ItemStackCreator.getStack("§cNo maps available",
							Material.BARRIER, 1,
							"§7No maps are currently available",
							"§7for " + gameType.getDisplayName(),
							"",
							"§eClick to go back");
				}

				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer player) {
					new GUIPlay(gameType).open(player);
				}
			});
			return;
		}

		// Add back button
		set(new GUIClickableItem(31) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack("§cBack",
						Material.ARROW, 1,
						"§7Go back to game selection");
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				new GUIPlay(gameType).open(player);
			}
		});

		// Add map options
		int slot = 10;
		for (String map : maps) {
			if (slot > 16) slot = 19; // Move to next row
			if (slot > 25) break; // Max capacity reached

			final String mapName = map;
			set(new GUIClickableItem(slot) {
				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					return ItemStackCreator.getStack("§a" + mapName,
							Material.FILLED_MAP, 1,
							"§7Play " + gameType.getDisplayName(),
							"§7on §e" + mapName,
							"",
							"§eClick to play!");
				}

				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer player) {
					player.closeInventory();

					if (OrchestratorConnector.isSearching(player.getUuid())) {
						player.sendMessage("§cYou are already searching for a game!");
						return;
					}

					OrchestratorConnector connector = new OrchestratorConnector(player);
					connector.sendToGame(gameType, mapName);
				}
			});
			slot++;
		}
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {
		// No-op
	}
}