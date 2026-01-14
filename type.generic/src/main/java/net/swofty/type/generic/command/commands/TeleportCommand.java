package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.Player;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(
        aliases = "tp",
        description = "Teleport to a player or coordinates",
        usage = "/teleport <player> | /teleport <x> <y> <z>",
        permission = Rank.STAFF,
        allowsConsole = false
)
public class TeleportCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {


        ArgumentEntity entityArgument = ArgumentType.Entity("player")
                .onlyPlayers(true)
                .singleEntity(true);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            Player target = context.get(entityArgument).findFirstPlayer(sender);

            if (target == null) {
                sender.sendMessage("§cCouldn't find a player by the name of §e"
                        + context.getRaw(entityArgument) + "§c.");
                return;
            }

            if (target.getUuid().equals(player.getUuid())) {
                sender.sendMessage("§cYou cannot teleport to yourself.");
                return;
            }

            HypixelPlayer targetPlayer = (HypixelPlayer) target;
            player.teleport(target.getPosition());

            sender.sendMessage("§2Teleported to "
                    + targetPlayer.getColouredDisplayName() + "§2.");
        }, entityArgument);


        ArgumentInteger xArgument = ArgumentType.Integer("x");
        ArgumentInteger yArgument = ArgumentType.Integer("y");
        ArgumentInteger zArgument = ArgumentType.Integer("z");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;

            int x = context.get(xArgument);
            int y = context.get(yArgument);
            int z = context.get(zArgument);

            player.teleport(player.getPosition()
                    .withX(x)
                    .withY(y)
                    .withZ(z));

            sender.sendMessage("§2Teleported to §e"
                    + x + " " + y + " " + z + "§2.");
        }, xArgument, yArgument, zArgument);
    }
}
