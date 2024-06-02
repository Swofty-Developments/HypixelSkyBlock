package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "setrank", description = "Rank command", usage = "/rank <player> <rank>", permission = Rank.ADMIN, allowsConsole = true)
public class RankCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEntity entityArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        ArgumentEnum<Rank> rankArgument = ArgumentType.Enum("rank", Rank.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final Player player = context.get(entityArgument).findFirstPlayer(sender);
            final Rank rank = context.get(rankArgument);

            if (player == null) {
                sender.sendMessage("§cCouldn't find a player by the name of §e" + context.getRaw(entityArgument) + "§c.");
                return;
            }

            final String playerName = player.getUsername();

            DataHandler.getUser(player).get(DataHandler.Data.RANK, DatapointRank.class).setValue(rank);

            sender.sendMessage("§aSuccessfully set §e" + playerName + "§a's rank to §e" + rank.name() + "§a.");
        }, entityArgument, rankArgument);
    }
}
