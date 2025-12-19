package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeBlockPosition;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.location.RelativeVec;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "tp",
        description = "teleports to a player",
        usage = "/teleport",
        permission = Rank.HELPER,
        allowsConsole = false)
public class TeleportCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEntity entityArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            final Player target = context.get(entityArgument).findFirstPlayer(sender);

            if (target == null) {
                sender.sendMessage("§cCouldn't find a player by the name of §e" + context.getRaw(entityArgument) + "§c.");
                return;
            }
            player.teleport(target.getPosition());
        });

        ArgumentInteger xArgument = ArgumentType.Integer("x");
        ArgumentInteger yArgument = ArgumentType.Integer("y");
        ArgumentInteger zArgument = ArgumentType.Integer("z");
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            int x = context.get(xArgument);
            int y = context.get(yArgument);
            int z = context.get(zArgument);
            player.teleport(player.getPosition().withX(x).withY(y).withZ(z));
        }, xArgument, yArgument, zArgument);

        ArgumentRelativeVec3 blockPosArgument = ArgumentType.RelativeVec3("position");
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            RelativeVec relativeVec = context.get(blockPosArgument);
            player.teleport(player.getPosition().add(relativeVec.from(player)));
        }, blockPosArgument);
    }
}