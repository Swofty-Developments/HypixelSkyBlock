package net.swofty.type.bedwarslobby.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;

public class GUIMapSelection extends HypixelPaginatedGUI<String> {

	private final GameType type;

	public GUIMapSelection(GameType type) {
		super(InventoryType.CHEST_5_ROW);
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
				.handleRequest(new GetMapsProtocolObject.GetMapsMessage(ServerType.BEDWARS_GAME))
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
				return ItemStack.builder(Material.MAP)
						.customName(Component.text(mapId, NamedTextColor.GREEN));
			}

			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer p) {
				// set preference with both mode and map id then transfer
				p.asProxyPlayer().setBedWarsJoinPreference(type.name(), mapId);
				// ask orchestrator which server hosts this map, then transfer directly
				var resp = (GetServerForMapProtocolObject.GetServerForMapResponse) new ProxyService(ServiceType.ORCHESTRATOR)
						.handleRequest(new GetServerForMapProtocolObject.GetServerForMapMessage(
								ServerType.BEDWARS_GAME, mapId, 1
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
