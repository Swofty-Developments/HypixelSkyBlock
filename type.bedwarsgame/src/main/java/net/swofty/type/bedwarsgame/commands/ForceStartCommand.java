package net.swofty.type.bedwarsgame.commands;

import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "forcestart",
		description = "Starts a bedwars game immediately.",
		usage = "/forcestart",
		permission = Rank.STAFF,
		allowsConsole = false)
public class ForceStartCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		command.addSyntax((sender, context) -> {
			if (!permissionCheck(sender)) return;
			BedWarsPlayer player = (BedWarsPlayer) sender;
			Game game = player.getGame();
			if (game == null) {
				player.sendMessage("§cYou are not in a game.");
				return;
			}
			if (game.getGameStatus() != GameStatus.WAITING) {
				player.sendMessage("§cYou can only force start a game that is waiting.");
				return;
			}
			game.startGame();
		});
	}

}
