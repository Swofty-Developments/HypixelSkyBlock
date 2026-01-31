package net.swofty.type.generic.command.commands.replay;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.replay.ReplayListProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.gui.v2.views.replay.ReplayEntry;
import net.swofty.type.generic.gui.v2.views.replay.ReplaysListView;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Opens the replay browser GUI.
 */
@CommandParameters(
	description = "View your game replays",
	usage = "/replays",
	permission = Rank.DEFAULT,
	allowsConsole = false
)
public class ReplaysCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		command.setDefaultExecutor((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender;

			player.sendMessage(Component.text("Loading replays...", NamedTextColor.GRAY));

			fetchReplays(player).thenAccept(replays -> {
				player.openView(new ReplaysListView(replay -> {
					sendToReplayViewer(player, replay);
				}), new ReplaysListView.State(replays, 0));
			}).exceptionally(e -> {
				player.sendMessage(Component.text("Failed to load replays: " + e.getMessage(), NamedTextColor.RED));
				return null;
			});
		});
	}

	private CompletableFuture<List<ReplayEntry>> fetchReplays(HypixelPlayer player) {
		var request = new ReplayListProtocolObject.ListRequest(player.getUuid(), 50, null);
		ProxyService replayService = new ProxyService(ServiceType.REPLAY);

		return replayService.<ReplayListProtocolObject.ListRequest, ReplayListProtocolObject.ListResponse>handleRequest(request)
			.thenApply(response -> {
				if (!response.success()) {
					return List.of();
				}

				return response.replays().stream()
					.map(summary -> ReplayEntry.builder()
						.replayId(summary.replayId())
						.gameId(summary.gameId())
						.serverType(summary.serverType())
						.gameTypeName(summary.gameTypeName())
						.mapName(summary.mapName())
						.startTime(summary.startTime())
						.endTime(summary.endTime())
						.durationTicks(summary.durationTicks())
						.players(summary.players())
						.winnerId(summary.winnerId())
						.winnerType(summary.winnerType())
						.dataSize(summary.dataSize())
						.build())
					.collect(Collectors.toList());
			});
	}

	// Map to store pending replay IDs for players joining the replay viewer
	// This is used to pass the replay ID from the lobby to the replay viewer server
	private static final Map<UUID, UUID> pendingReplayIds = new java.util.concurrent.ConcurrentHashMap<>();

	/**
	 * Gets and removes the pending replay ID for a player.
	 * Called by the replay viewer when a player joins.
	 */
	public static UUID getAndRemovePendingReplayId(UUID playerId) {
		return pendingReplayIds.remove(playerId);
	}

	private void sendToReplayViewer(HypixelPlayer player, ReplayEntry replay) {
		player.sendMessage(Component.text("Loading replay...", NamedTextColor.GREEN));

		// Store the replay ID for the player before transferring
		pendingReplayIds.put(player.getUuid(), replay.replayId());

		player.sendTo(ServerType.REPLAY_VIEWER);
	}
}
