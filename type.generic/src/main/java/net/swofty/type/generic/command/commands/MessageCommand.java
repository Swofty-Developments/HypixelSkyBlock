package net.swofty.type.generic.command.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

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
                player.sendTranslated("commands.message.player_not_found", Component.text(playerName));
                return;
            }

            ProxyPlayer target = new ProxyPlayer(targetUUID);
            if (!target.isOnline().join()) {
                player.sendTranslated("commands.message.player_not_online", Component.text(playerName));
                return;
            }
            String targetName = HypixelPlayer.getDisplayName(targetUUID);
            String ourName = player.getFullDisplayName();

            String joinedMessage = String.join(" ", message);
            player.sendTranslated("commands.message.outgoing", Component.text(targetName), Component.text(joinedMessage));
            target.sendMessage(I18n.string("commands.message.incoming", Component.text(ourName), Component.text(joinedMessage)));
        }, playerArgument, messageArgument);
    }
}
