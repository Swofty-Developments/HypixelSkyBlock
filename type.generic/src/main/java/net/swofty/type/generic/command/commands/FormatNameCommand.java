package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.UUID;

@CommandParameters(aliases = "getnameformatted",
        description = "Gets the format of a players name from their username",
        usage = "/formatname <name>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class FormatNameCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString playerArgument = ArgumentType.String("player");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String playerName = context.get(playerArgument);
            HypixelPlayer player = (HypixelPlayer) sender;
            UUID uuid = HypixelDataHandler.getPotentialUUIDFromName(playerName);

            if (uuid == null) {
                player.sendMessage("Nope doesn't exist");
                return;
            }

            player.sendMessage(HypixelPlayer.getDisplayName(uuid));
            player.sendMessage(HypixelPlayer.getRawName(uuid));
        }, playerArgument);
    }
}
