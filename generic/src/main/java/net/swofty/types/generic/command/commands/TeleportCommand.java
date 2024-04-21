package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "tp",
        description = "teleports to a player",
        usage = "/teleport <player>",
        permission = Rank.HELPER,
        allowsConsole = false)
public class TeleportCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEntity entityArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            final Player target = context.get(entityArgument).findFirstPlayer(sender);

            if (target == null) {
                sender.sendMessage("§cCouldn't find a player by the name of §e" + context.getRaw(entityArgument) + "§c.");
                return;
            }
            player.teleport(target.getPosition());
        }, entityArgument);
    }
}