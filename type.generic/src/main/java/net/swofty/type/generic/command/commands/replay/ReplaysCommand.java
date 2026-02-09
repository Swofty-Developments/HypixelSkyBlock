package net.swofty.type.generic.command.commands.replay;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.replay.ChooseReplayProtocolObject;
import net.swofty.commons.protocol.objects.replay.ReplayListProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.gui.v2.views.replay.ReplayEntry;
import net.swofty.type.generic.gui.v2.views.replay.ReplaysListView;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@CommandParameters(
	description = "View your game replays",
	usage = "/replays",
	permission = Rank.DEFAULT,
	allowsConsole = false
)
public class ReplaysCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		command.setDefaultExecutor((sender, _) -> {
			HypixelPlayer player = (HypixelPlayer) sender;

			player.sendMessage(Component.text("Loading replays...", NamedTextColor.GRAY));

			fetchReplays(player).thenAccept(replays -> {
				player.openView(new ReplaysListView(replay -> {
					sendToReplayViewer(player, replay);
				}), new ReplaysListView.State(replays, 0));
			}).exceptionally(e -> {
				player.sendMessage(Component.text("Failed to load replays.", NamedTextColor.RED));
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
						.serverId(summary.serverId())
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

	private void sendToReplayViewer(HypixelPlayer player, ReplayEntry replay) {
		player.sendMessage(Component.text("Loading replay...", NamedTextColor.GREEN));

		ProxyService replayService = new ProxyService(ServiceType.REPLAY);
		var request = new ChooseReplayProtocolObject.ChooseReplayMessage(player.getUuid(), replay.replayId().toString());
		replayService.<ChooseReplayProtocolObject.ChooseReplayMessage, ChooseReplayProtocolObject.ChooseReplayResponse>handleRequest(request).thenAccept(response -> {;
			if (!response.error()) {
				player.sendMessage(Component.text("Sending you to the Replay Viewer...", NamedTextColor.GRAY));
				player.sendTo(ServerType.REPLAY_VIEWER);
			} else {
				player.sendMessage(Component.text("Failed to send you to a replay viewer.", NamedTextColor.RED));
			}
		}).exceptionally(e -> {
			player.sendMessage(Component.text("Failed to load replay: " + e.getMessage(), NamedTextColor.RED));
			return null;
		});
	}
}
