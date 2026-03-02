package net.swofty.type.replayviewer.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.playback.ReplaySession;
import net.swofty.type.replayviewer.util.ReplayShareCodec;

@CommandParameters(
	description = "Share your current replay position",
	usage = "/share",
	permission = Rank.DEFAULT,
	allowsConsole = false
)
public class ShareCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		command.setDefaultExecutor((sender, _) -> {
			HypixelPlayer player = (HypixelPlayer) sender;

			var sessionOpt = TypeReplayViewerLoader.getSession(player.getUuid());
			if (sessionOpt.isEmpty()) {
				player.sendMessage(Component.text("You are not watching a replay!", NamedTextColor.RED));
				return;
			}

			ReplaySession session = sessionOpt.get();
			String shareCode = ReplayShareCodec.encode(
				player.getPosition(),
				session.getCurrentTick(),
				session.getMetadata().getMapCenterX(),
				session.getMetadata().getMapCenterZ()
			);
			String fullCommand = "/replay " + session.getReplayId() + " " + shareCode;

			Component message = Component.text()
				.append(Component.text("§6§lClick here to put share command in chat!"))
				.clickEvent(ClickEvent.suggestCommand(fullCommand))
				.hoverEvent(Component.text("Click to copy command to chat", NamedTextColor.YELLOW))
				.build();

			player.sendMessage(message);
		});
	}
}

