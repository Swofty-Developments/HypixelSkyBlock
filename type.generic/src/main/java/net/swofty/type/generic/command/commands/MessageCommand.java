package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

@CommandParameters(aliases = "msg message whipser",
        description = "Sends a message to another player",
        usage = "/msg <player> <message>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class MessageCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString playerArgument = ArgumentType.String("player");
        ArgumentStringArray messageArgument = new ArgumentStringArray("message");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String playerName = context.get(playerArgument);
            String[] message = context.get(messageArgument);
            HypixelPlayer player = (HypixelPlayer) sender;

            @Nullable UUID targetUUID = HypixelDataHandler.getPotentialUUIDFromName(playerName);
            if (targetUUID == null) {
                player.sendMessage(I18n.string("commands.message.player_not_found", Map.of("player", playerName)));
                return;
            }

            ProxyPlayer target = new ProxyPlayer(targetUUID);
            if (!target.isOnline().join()) {
                player.sendMessage(I18n.string("commands.message.player_not_online", Map.of("player", playerName)));
                return;
            }
            String targetName = HypixelPlayer.getDisplayName(targetUUID);
            String ourName = player.getFullDisplayName();

            player.sendMessage(I18n.string("commands.message.outgoing", Map.of("target", targetName, "message", String.join(" ", message))));
            target.sendMessage(I18n.string("commands.message.incoming", Map.of("sender", ourName, "message", String.join(" ", message))));
        }, playerArgument, messageArgument);
    }
}
