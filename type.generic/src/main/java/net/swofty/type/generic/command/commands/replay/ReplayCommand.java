package net.swofty.type.generic.command.commands.replay;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.replay.ChooseReplayProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.UUID;

@CommandParameters(
	description = "Watch a specific replay by ID",
	usage = "/replay <uuid> [hex]",
	permission = Rank.DEFAULT,
	allowsConsole = false
)
public class ReplayCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		var replayIdArg = ArgumentType.String("replayId");
		var hexArg = ArgumentType.String("hex");

		command.setDefaultExecutor((sender, _) ->
			sender.sendMessage(Component.text("Usage: /replay <uuid> [hex]", NamedTextColor.RED)));

		command.addSyntax((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender;
			String replayIdStr = context.get(replayIdArg);

			UUID replayId = parseUuid(player, replayIdStr);
			if (replayId == null) return;

			sendToReplayViewer(player, replayId, null);
		}, replayIdArg);

		command.addSyntax((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender;
			String replayIdStr = context.get(replayIdArg);
			String hex = context.get(hexArg);

			UUID replayId = parseUuid(player, replayIdStr);
			if (replayId == null) return;

			String cleanHex = hex.startsWith("#") ? hex : "#" + hex;
			if (cleanHex.length() != 9) {
				player.sendMessage(Component.text("Invalid share code format.", NamedTextColor.RED));
				return;
			}

			sendToReplayViewer(player, replayId, cleanHex);
		}, replayIdArg, hexArg);
	}

	private UUID parseUuid(HypixelPlayer player, String uuidStr) {
		try {
			return UUID.fromString(uuidStr);
		} catch (IllegalArgumentException e) {
			player.sendMessage(Component.text("Invalid replay ID format. Must be a valid UUID.", NamedTextColor.RED));
			return null;
		}
	}

	private void sendToReplayViewer(HypixelPlayer player, UUID replayId, String shareCode) {
		player.sendMessage(Component.text("Loading replay...", NamedTextColor.GREEN));

		ProxyService replayService = new ProxyService(ServiceType.REPLAY);
		var request = new ChooseReplayProtocolObject.ChooseReplayMessage(player.getUuid(), replayId.toString(), shareCode);
		replayService.<ChooseReplayProtocolObject.ChooseReplayMessage, ChooseReplayProtocolObject.ChooseReplayResponse>handleRequest(request).thenAccept(response -> {
			if (!response.error()) {
				player.sendMessage(Component.text("Sending you to the Replay Viewer...", NamedTextColor.GRAY));
				player.sendTo(ServerType.REPLAY_VIEWER);
			} else {
				player.sendMessage(Component.text("Replay not found or failed to load.", NamedTextColor.RED));
			}
		}).exceptionally(e -> {
			player.sendMessage(Component.text("Failed to load replay: " + e.getMessage(), NamedTextColor.RED));
			return null;
		});
	}
}
