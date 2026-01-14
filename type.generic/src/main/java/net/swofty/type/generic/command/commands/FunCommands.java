package net.swofty.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.proxyapi.ProxyPlayer;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@CommandParameters(
        aliases = "boop",
        description = "Fun useless commands",
        usage = "/boop <player>",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class FunCommands extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {



        ArgumentString playerArgument = ArgumentType.String("player");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            String playerName = context.get(playerArgument);

            @Nullable UUID targetUUID = HypixelDataHandler.getPotentialUUIDFromName(playerName);
            if (targetUUID == null) {
                player.sendMessage("§cCan't find a player by the name of '" + playerName + "'");
                return;
            }

            ProxyPlayer target = new ProxyPlayer(targetUUID);
            if (!target.isOnline().join()) {
                player.sendMessage("§cThe player you tried to boop, " + playerName + ", is not online.");
                return;
            }

            String targetName = HypixelPlayer.getDisplayName(targetUUID);
            String ourName = player.getFullDisplayName();

            String boopMsg = "§d§lBoop!";

            // Feedback to sender
            player.sendMessage("§dTo " + targetName + "§7: " + boopMsg);

            // Whisper to target
            target.sendMessage("§dFrom " + ourName + "§7: " + boopMsg);

        }, playerArgument);
    }
}
