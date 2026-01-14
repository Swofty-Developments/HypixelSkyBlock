package net.swofty.type.generic.command.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(permission = Rank.STAFF, description = "Throws an exception for testing purposes", usage = "/throwexception", aliases = "throwexception", allowsConsole = true)
public class ThrowExceptionCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		command.addSyntax((sender, context) -> {
			if (!permissionCheck(sender)) return;

			throw new RuntimeException("This is a test exception thrown with /throwexception command.");
		});
	}

}
