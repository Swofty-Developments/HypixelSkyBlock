package net.swofty.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointRank;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "setrank", description = "Rank command", usage = "/rank <player> <rank>", permission = Rank.HELPER, allowsConsole = false)
public class RankCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEntity entityArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        ArgumentEnum<Rank> rankArgument = ArgumentType.Enum("rank", Rank.class);

        command.addSyntax((sender, context) -> {
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
