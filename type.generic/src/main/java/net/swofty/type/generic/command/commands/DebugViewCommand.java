package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.gui.v2.test.*;
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

		command.addSyntax(((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender; // safe cast
			player.openView(new TestContainerView());
		}), new ArgumentLiteral("container"));

		command.addSyntax(((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender; // safe cast
			player.openView(new TestMixedView());
		}), new ArgumentLiteral("mixedView"));

		// sharedView
		command.addSyntax(((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender; // safe cast
			TestSharedContainerView.open(player, "a", "a");
		}), new ArgumentLiteral("sharedView"));

		// sharedState
		command.addSyntax(((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender; // safe cast
			TestSharedStateView.openNew(player, "b");
		}), new ArgumentLiteral("sharedState"));

		// joinSharedState
		command.addSyntax(((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender; // safe cast
			TestSharedStateView.join(player, "b");
		}), new ArgumentLiteral("joinSharedState"));

		// noState
		command.addSyntax(((sender, context) -> {
			HypixelPlayer player = (HypixelPlayer) sender; // safe cast
			player.openView(new TestNoStateView());
		}), new ArgumentLiteral("noState"));
	}
}
