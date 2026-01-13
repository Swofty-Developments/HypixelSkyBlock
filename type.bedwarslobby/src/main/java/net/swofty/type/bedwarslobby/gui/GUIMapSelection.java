package net.swofty.type.bedwarslobby.gui;

import io.sentry.Sentry;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.GetMapsProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.lobby.LobbyOrchestratorConnector;
import net.swofty.type.generic.data.datapoints.DatapointMapStringLong;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.commons.party.FullParty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
					Sentry.captureException(throwable);
					throwable.printStackTrace();
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
			if (slot > 25) break; // Max capacity reached

			final String mapName = map;
			// TODO: proper map ID handling
			String mapId = mapName.toLowerCase().replace(" ", "_");

			set(new GUIClickableItem(slot) {
				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {

					BedWarsDataHandler data = BedWarsDataHandler.getUser(player);
					List<String> favouriteMaps = data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class).getValue();
					boolean isFav = favouriteMaps != null && favouriteMaps.contains(mapId);
					Map<String, Long> counts = data.get(BedWarsDataHandler.Data.MAP_JOIN_COUNTS, DatapointMapStringLong.class).getValue();
					Long joins = counts.get(mapId);
					if (joins == null) {
						joins = 0L;
					}

					return ItemStackCreator.getStack((isFav ? "§b✯ " : "") + "§a" + mapName,
							Material.FIREWORK_STAR, 1,
							"§7" + gameType.getDisplayName(),
							"",
							"§7Available Games: §aUnknown",
							"§7Times Joined: §a" + joins,
							"§7Map Selections: §aUnlimited",
							"",
							" §aClick to Play",
							"§eRight click to toggle favorite!"
					);
				}

				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer player) {
					if (e.getClick() instanceof Click.Right) {
						BedWarsDataHandler data = BedWarsDataHandler.getUser(player);
						if (data != null) {
							var favs = data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class).getValue();
							if (favs.contains(mapId)) favs.remove(mapId); else favs.add(mapId);
							data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class).setValue(favs);
						}
						GUIMapSelection.this.open(player);
						return;
					}
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
					connector.sendToGame(ServerType.BEDWARS_GAME, gameType.toString(), mapName);
				}
			});

			if (slot > 16) slot = 18; // Move to the next row
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