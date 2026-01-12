package net.swofty.type.bedwarsgame.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "shout",
		description = "Shouts a message to all players on the game.",
		usage = "/shout <message>",
		permission = Rank.DEFAULT,
		allowsConsole = false)
public class ShoutCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		var messageArg = ArgumentType.String("message");

		command.addSyntax((sender, context) -> {
			final String message = context.get("message");
			if (!permissionCheck(sender)) return;

			BedWarsPlayer player = (BedWarsPlayer) sender;
			Game game = player.getGame();
			if (game == null) {
				player.sendMessage("§cYou are not in a game.");
				return;
			}
			if (game.getBedwarsGameType() == BedwarsGameType.SOLO) {
				player.sendMessage("§cThis command is unavailable.");
				return;
			}
			if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
				player.sendMessage("§cYou can only shout messages during an active game.");
				return;
			}
			if (message.isEmpty()) {
				player.sendMessage("§cPlease provide a message to shout.");
				return;
			}
			for (BedWarsPlayer receiver : game.getPlayers()) {
				receiver.sendMessage("§6[SHOUT] §r" + player.getFullDisplayName() + "§r: " + message);
			}
		}, messageArg);

		command.setDefaultExecutor((sender, context) -> {
			sender.sendMessage("§cPlease provide a message to shout.");
		});
	}

}
