package net.swofty.type.bedwarsgame.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "gamestate",
        description = "Changes something about the game state.",
        usage = "/gamestate <message>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class GameStateCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString teamArg = ArgumentType.String("team");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof BedWarsPlayer player)) return;
            String team = context.get("team");
            Game game = player.getGame();

            if (game == null) {
                player.sendMessage("§cYou are not in a game.");
                return;
            }

            game.recordBedDestroyed(BedWarsMapsConfig.TeamKey.valueOf(team));
        }, ArgumentType.Literal("breakBed"), teamArg);
    }
}
