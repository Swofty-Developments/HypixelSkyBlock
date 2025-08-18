package net.swofty.type.bedwarsgame.commands;

import net.minestom.server.entity.Player;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "forcestart",
		description = "Starts a bedwars game immediately.",
		usage = "/forcestart",
		permission = Rank.ADMIN,
		allowsConsole = false)
public class ForceStartCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		command.addSyntax((sender, context) -> {
			if (!permissionCheck(sender)) return;
			if (!(sender instanceof Player player)) {
				sender.sendMessage("This command can only be used by players.");
				return;
			}

			Game game = TypeBedWarsGameLoader.getPlayerGame(player);
			if (game == null) {
				player.sendMessage("You are not in a game.");
				return;
			}
			if (game.getGameStatus() != GameStatus.WAITING) {
				player.sendMessage("You can only force start a game that is waiting.");
				return;
			}
			game.startGame();
		});
	}

}
