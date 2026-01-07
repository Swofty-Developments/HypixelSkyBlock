package net.swofty.type.generic.command.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.gui.v2.test.TestStateView;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "debugview", usage = "/debugview", description = "opens a debug view", permission = Rank.STAFF, allowsConsole = false)
public class DebugViewCommand extends HypixelCommand {
	@Override
	public void registerUsage(MinestomCommand command) {
		command.setDefaultExecutor((commandSender, commands) -> {
			HypixelPlayer player = (HypixelPlayer) commandSender; // safe cast
			player.openView(new TestStateView());
		});
	}
}
