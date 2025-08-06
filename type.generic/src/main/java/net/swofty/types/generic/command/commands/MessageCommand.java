package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@CommandParameters(aliases = "msg",
        description = "Sends a message to another player",
        usage = "/msg <player> <message>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class MessageCommand extends SkyBlockCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString playerArgument = ArgumentType.String("player");
        ArgumentStringArray messageArgument = new ArgumentStringArray("message");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String playerName = context.get(playerArgument);
            String[] message = context.get(messageArgument);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            @Nullable UUID targetUUID = DataHandler.getPotentialUUIDFromName(playerName);
            if (targetUUID == null) {
                player.sendMessage("§cCan't find a player by the name of '" + playerName + "'");
                return;
            }

            ProxyPlayer target = new ProxyPlayer(targetUUID);
            if (!target.isOnline().join()) {
                player.sendMessage("§cThe player you tried to message, " + playerName + ", is not online.");
                return;
            }
            String targetName = SkyBlockPlayer.getDisplayName(targetUUID);
            String ourName = player.getFullDisplayName();

            player.sendMessage("§dTo " + targetName + "§7: " + String.join(" ", message));
            target.sendMessage("§dFrom " + ourName + "§7: " + String.join(" ", message));
        }, playerArgument, messageArgument);
    }
}
