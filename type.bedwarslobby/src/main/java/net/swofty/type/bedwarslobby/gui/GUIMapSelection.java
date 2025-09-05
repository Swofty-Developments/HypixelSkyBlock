package net.swofty.type.bedwarslobby.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.GetMapsProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.bedwarsgeneric.game.GameType;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.bedwarsgeneric.data.BedWarsDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.data.datapoints.DatapointMapStringLong;

public class GUIMapSelection extends HypixelPaginatedGUI<String> {

	private final GameType type;

	public GUIMapSelection(GameType type) {
		super(InventoryType.CHEST_6_ROW);
		this.type = type;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {

	}

	@Override
	protected int[] getPaginatedSlots() {
		return new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};
	}

	@Override
	protected boolean shouldFilterFromSearch(String query, String item) {
		return false;
	}

	@Override
	protected PaginationList<String> fillPaged(HypixelPlayer player, PaginationList<String> paged) {
		// fetch maps for this server type from orchestrator (blocking)
		GetMapsProtocolObject.GetMapsResponse resp = (GetMapsProtocolObject.GetMapsResponse) new ProxyService(ServiceType.ORCHESTRATOR)
				.handleRequest(new GetMapsProtocolObject.GetMapsMessage(ServerType.BEDWARS_GAME, type.name()))
				.join();

		paged.addAll(resp.maps());
		return paged;
	}

	@Override
	protected void performSearch(HypixelPlayer player, String query, int page, int maxPage) {

	}

	@Override
	protected GUIClickableItem createItemFor(String mapId, int slot, HypixelPlayer player) {
		return new GUIClickableItem(slot) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				BedWarsDataHandler data = BedWarsDataHandler.getUser(player);
				boolean isFav = false;
				long joins = 0L;
				if (data != null) {
					var favs = data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class).getValue();
					isFav = favs != null && favs.contains(mapId);
					var counts = data.get(BedWarsDataHandler.Data.MAP_JOIN_COUNTS, DatapointMapStringLong.class).getValue();
					if (counts != null && counts.containsKey(mapId)) joins = counts.get(mapId);
				}

				return ItemStackCreator.getStack(
						(isFav ? "§b✯ " : "") + "§a" + mapId,
						Material.MAP,
						1,
						"§7" + type.getDisplayName(),
						"",
						"§7Available Servers: §aUnknown",
						"§7Times Joined: §a" + joins,
						"§7Map Selections: §aUnlimited",
						"",
						"§eClick to Play",
						"§7Right click to toggle favorite!"
				);
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				if (e.getClick() instanceof Click.Right) {
					BedWarsDataHandler data = BedWarsDataHandler.getUser(p);
					if (data != null) {
						var favs = data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class).getValue();
						if (favs.contains(mapId)) favs.remove(mapId); else favs.add(mapId);
						data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class).setValue(favs);
					}
					GUIMapSelection.this.open(p);
					return;
				}

				BedWarsDataHandler data = BedWarsDataHandler.getUser(p);
				if (data != null) {
					var counts = data.get(BedWarsDataHandler.Data.MAP_JOIN_COUNTS, DatapointMapStringLong.class).getValue();
					counts.put(mapId, counts.getOrDefault(mapId, 0L) + 1);
					data.get(BedWarsDataHandler.Data.MAP_JOIN_COUNTS, DatapointMapStringLong.class).setValue(counts);
				}

				// set preference with both mode and map id then transfer
				p.asProxyPlayer().setBedWarsJoinPreference(type.name(), mapId);
				// ask orchestrator which server hosts this map, then transfer directly
				var resp = (GetServerForMapProtocolObject.GetServerForMapResponse) new ProxyService(ServiceType.ORCHESTRATOR)
						.handleRequest(new GetServerForMapProtocolObject.GetServerForMapMessage(
								ServerType.BEDWARS_GAME, mapId, type.name(), 1
						))
						.join();
				if (resp != null && resp.server() != null) {
					p.asProxyPlayer().transferToWithIndication(resp.server().uuid());
				} else {
					// just send it to a game (should probably error)
					p.sendTo(ServerType.BEDWARS_GAME);
				}
			}
		};
	}

	@Override
	protected String getTitle(HypixelPlayer player, String query, int page, PaginationList<String> paged) {
		return "Bed Wars " + type.getDisplayName();
	}

}
