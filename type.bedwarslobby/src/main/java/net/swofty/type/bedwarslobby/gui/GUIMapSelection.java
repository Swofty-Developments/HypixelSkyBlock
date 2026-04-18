package net.swofty.type.bedwarslobby.gui;

import io.sentry.Sentry;
import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.commons.protocol.objects.orchestrator.GetMapsProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.data.datapoints.DatapointMapStringLong;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulPaginatedView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ClickContext;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.GameQueueValidator;
import net.swofty.type.lobby.LobbyOrchestratorConnector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

// TODO: order by favorite
public class GUIMapSelection extends StatefulPaginatedView<String, GUIMapSelection.State> {

	private final BedWarsGameType gameType;
	private int requestSequence;

	public GUIMapSelection(BedWarsGameType gameType) {
		this.gameType = gameType;
	}

	@Override
	public ViewConfiguration<State> configuration() {
		return ViewConfiguration.withString(
			(_, __) -> "Map Selection - " + gameType.getDisplayName(),
			InventoryType.CHEST_6_ROW
		);
	}

	@Override
	public State initialState() {
		return new State(Collections.emptyList(), 0, true, false, null, 0);
	}

	@Override
	public void onOpen(State state, ViewContext ctx) {
		if (state.loadAttempted()) {
			return;
		}
		loadMaps(ctx);
	}

	private void loadMaps(ViewContext ctx) {
		int requestId = ++requestSequence;
		ctx.session(State.class).update(current -> current.startLoading(requestId));

		ProxyService orchestratorService = new ProxyService(ServiceType.ORCHESTRATOR);

		GetMapsProtocolObject.GetMapsMessage message =
				new GetMapsProtocolObject.GetMapsMessage(ServerType.BEDWARS_GAME, gameType.toString());

		orchestratorService.handleRequest(message)
				.thenAccept(response -> {
					MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
						var session = ctx.session(State.class);
						if (session.isClosed() || session.state().requestId() != requestId) {
							return;
						}

						if (response instanceof GetMapsProtocolObject.GetMapsResponse(List<String> maps)) {
							List<String> loadedMaps = new ArrayList<>(maps);
							loadedMaps.sort(String.CASE_INSENSITIVE_ORDER);
							session.update(current -> current.loaded(loadedMaps, requestId));
							return;
						}

						session.update(current -> current.failed("Unknown response while loading maps.", requestId));
					});
				})
				.exceptionally(throwable -> {
					Sentry.captureException(throwable);
					MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
						var session = ctx.session(State.class);
						if (session.isClosed() || session.state().requestId() != requestId) {
							return;
						}
						session.update(current -> current.failed(throwable.getMessage(), requestId));
						ctx.player().sendMessage("§cFailed to load maps: " + throwable.getMessage());
					});
					return null;
				});
	}

	@Override
	protected int[] getPaginatedSlots() {
		return SLIM;
	}

	@Override
	protected int getNextPageSlot() {
		return 26;
	}

	@Override
	protected int getPreviousPageSlot() {
		return 18;
	}

	@Override
	protected boolean shouldRenderNavBackground() {
		return false;
	}

	@Override
	protected void layoutBackground(ViewLayout<State> layout, State state, ViewContext ctx) {
		layout.allowHotkey(false);
	}

	@Override
	protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
		if (!Components.back(layout, 49, ctx)) {
			layout.slot(49,
				(_, __) -> ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
					"§7Go back to Play Bed Wars"),
				(_, viewCtx) -> {
					viewCtx.player().openView(new GUIPlay(gameType));
				}
			);
		}

		layout.autoUpdating(39, (s, c) -> {
			return ItemStackCreator.getStack("§aRandom Map", Material.FIREWORK_ROCKET, 1, "", "§7Map Selections: §aUnlimited", "", "§a" + (showArrowPulse() ? "►" : "") + "  Click to Play");
		}, (click, viewCtx) -> playRandomMap(click.state(), viewCtx), Duration.ofSeconds(1));

		layout.autoUpdating(41, (s, _) -> {
			return ItemStackCreator.getStack("§aRandom Favorite", Material.DIAMOND, 1, "", "§7Map Selections: §aUnlimited", "", "§a" + (showArrowPulse() ? "►" : "") + "  Click to Play");
		}, (click, viewCtx) -> playRandomFavorite(click.state(), viewCtx), Duration.ofSeconds(1));

		if (state.loading()) {
			layout.slot(22, (_, __) -> ItemStackCreator.getStack("§eLoading maps...",
				Material.CLOCK, 1,
				"§7Please wait while we fetch",
				"§7available maps for " + gameType.getDisplayName()));
			return;
		}

		if (state.errorMessage() != null) {
			layout.slot(22,
				(_, __) -> ItemStackCreator.getStack("§cFailed to load maps",
					Material.BARRIER, 1,
					"§7" + state.errorMessage(),
					"",
					"§eClick to retry"),
				(_, viewCtx) -> loadMaps(viewCtx)
			);
			return;
		}

		if (state.items().isEmpty()) {
			layout.slot(22,
				(_, _) -> ItemStackCreator.getStack("§cNo maps available",
					Material.BARRIER, 1,
					"§7No maps are currently available",
					"§7for " + gameType.getDisplayName(),
					"",
					"§eClick to refresh"),
				(_, viewCtx) -> loadMaps(viewCtx)
			);
		}
	}

	@Override
	protected ItemStack.Builder renderItem(String mapName, int index, HypixelPlayer player) {
		String mapId = mapId(mapName);
		BedWarsDataHandler data = BedWarsDataHandler.getUser(player);

		boolean isFavorite = false;
		long joins = 0L;
		if (data != null) {
			List<String> favoriteMaps = data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class).getValue();
			if (favoriteMaps != null) {
				isFavorite = favoriteMaps.contains(mapId);
			}

			Map<String, Long> counts = data.get(BedWarsDataHandler.Data.MAP_JOIN_COUNTS, DatapointMapStringLong.class).getValue();
			if (counts != null) {
				joins = counts.getOrDefault(mapId, 0L);
			}
		}

		return ItemStackCreator.getStack((isFavorite ? "§b✯ " : "") + "§a" + mapName,
			Material.FIREWORK_STAR, 1,
			"§7" + gameType.getDisplayName(),
			"",
			"§7Available Games: §a" + 0,
			"§7Times Joined: §a" + joins,
			"§7Map Selections: §aUnlimited", // TODO: limited unless MVP+
			"",
			"§aLeft click to Play",
			"§eRight click to toggle favorite!");
	}

	@Override
	protected void onItemClick(ClickContext<State> click, ViewContext ctx, String mapName, int index) {
		HypixelPlayer player = ctx.player();
		if (click.click() instanceof Click.Right) {
			toggleFavorite(player, mapName);
			ctx.session(State.class).refresh();
			return;
		}

		queueMap(player, mapName);
	}

	private void playRandomMap(State state, ViewContext ctx) {
		if (state.items().isEmpty()) {
			ctx.player().sendMessage("§cNo maps are currently available.");
			return;
		}
        queueMap(ctx.player(), null);
	}

	private void playRandomFavorite(State state, ViewContext ctx) {
		HypixelPlayer player = ctx.player();
		BedWarsDataHandler data = BedWarsDataHandler.getUser(player);
		if (data == null) {
			player.sendMessage("§cFailed to read favorite maps.");
			return;
		}

		List<String> favoriteIds = data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class).getValue();
		if (favoriteIds == null || favoriteIds.isEmpty()) {
			player.sendMessage("§cYou do not have any favorite maps.");
			return;
		}

		List<String> availableFavorites = state.items().stream()
			.filter(mapName -> favoriteIds.contains(mapId(mapName)))
			.toList();
		if (availableFavorites.isEmpty()) {
			player.sendMessage("§cNone of your favorite maps are currently available.");
			return;
		}

		String mapName = availableFavorites.get(ThreadLocalRandom.current().nextInt(availableFavorites.size()));
		queueMap(player, mapName);
	}

	private void queueMap(HypixelPlayer player, String mapName) {
		player.closeInventory();

		if (!GameQueueValidator.canPlayerQueue(player, new GameQueueValidator.QueueRequirements(
			"Bed Wars",
			gameType.getQueueModeDisplayName(),
			gameType.getTeamSize()
		))) {
			return;
		}

		LobbyOrchestratorConnector connector = new LobbyOrchestratorConnector(player);
		connector.sendToGame(ServerType.BEDWARS_GAME, gameType.toString(), mapName);
	}

	@Override
	protected boolean shouldFilterFromSearch(State state, String item) {
		return false;
	}

	private void toggleFavorite(HypixelPlayer player, String mapName) {
		BedWarsDataHandler data = BedWarsDataHandler.getUser(player);
		if (data == null) {
			player.sendMessage("§cFailed to update favorite maps.");
			return;
		}

		String mapId = mapId(mapName);
		var datapoint = data.get(BedWarsDataHandler.Data.FAVORITE_MAPS, DatapointStringList.class);
		List<String> favoriteMaps = datapoint.getValue();
		if (favoriteMaps == null) {
			favoriteMaps = new ArrayList<>();
		} else {
			favoriteMaps = new ArrayList<>(favoriteMaps);
		}

		if (!favoriteMaps.remove(mapId)) {
			favoriteMaps.add(mapId);
		}
		datapoint.setValue(favoriteMaps);
	}

	private String mapId(String mapName) {
		String normalized = mapName.toLowerCase(Locale.ROOT)
			.replaceAll("[^a-z0-9]+", "_")
			.replaceAll("^_+", "")
			.replaceAll("_+$", "");
		if (!normalized.isEmpty()) {
			return normalized;
		}
		return mapName.toLowerCase(Locale.ROOT).replace(" ", "_");
	}

	private static boolean showArrowPulse() {
		return (System.currentTimeMillis() / 1000L) % 2L == 0L;
	}

	public record State(
		List<String> items,
		int page,
		boolean loading,
		boolean loadAttempted,
		String errorMessage,
		int requestId
	) implements PaginatedState<String> {

		@Override
		public State withPage(int page) {
			return new State(items, page, loading, loadAttempted, errorMessage, requestId);
		}

		@Override
		public State withItems(List<String> items) {
			return new State(items, page, loading, loadAttempted, errorMessage, requestId);
		}

		public State startLoading(int requestId) {
			return new State(items, page, true, true, null, requestId);
		}

		public State loaded(List<String> loadedItems, int requestId) {
			return new State(Collections.unmodifiableList(new ArrayList<>(loadedItems)), 0, false, true, null, requestId);
		}

		public State failed(String errorMessage, int requestId) {
			return new State(Collections.emptyList(), 0, false, true, errorMessage, requestId);
		}

	}
}