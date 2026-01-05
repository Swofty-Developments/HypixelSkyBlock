package net.swofty.type.lobby.commands;

import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.entity.Player;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.lobby.LobbyTypeLoader;
import net.swofty.type.lobby.parkour.LobbyParkourManager;
import net.swofty.type.lobby.parkour.Parkour;

@CommandParameters(aliases = "parkour", allowsConsole = false, description = "Parkour related commands", permission = Rank.DEFAULT, usage = "/parkour")
public class ParkourCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		LobbyParkourManager manager;
		if (HypixelConst.getTypeLoader() instanceof LobbyTypeLoader loader) {
			manager = loader.getParkourManager();
		} else {
			manager = null;
		}

		// start
		command.addSyntax((sender, context) -> {
			if (manager == null) {
				sender.sendMessage("§cThis command is unavailable here.");
				return;
			}
			if (!(sender instanceof Player player)) return;
			player.teleport(manager.getParkour().getStartLocation());
		}, new ArgumentLiteral("start"));

		// cancel
		command.addSyntax((sender, context) -> {
			if (manager == null) {
				sender.sendMessage("§cThis command is unavailable here.");
				return;
			}
			if (!(sender instanceof HypixelPlayer player)) return;
			manager.cancelParkour(player);
		}, new ArgumentLiteral("cancel"));

		// reset
		command.addSyntax((sender, context) -> {
			if (manager == null) {
				sender.sendMessage("§cThis command is unavailable here.");
				return;
			}
			if (!(sender instanceof HypixelPlayer player)) return;
			manager.resetPlayer(player);
		}, new ArgumentLiteral("reset"));
	}
}
