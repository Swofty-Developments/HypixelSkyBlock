package net.swofty.types.generic.command.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.commons.ServerType;
import net.swofty.proxyapi.redis.RedisMessage;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.mission.MissionSet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.StringUtility;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@CommandParameters(aliases = "pc",
        description = "Announces The Player Count of The Server!",
        usage = "/playercount",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class PlayerCountCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ServerType> serverTypeArgument = ArgumentType.Enum("servertype" , ServerType.class);
        serverTypeArgument.setFormat(ArgumentEnum.Format.LOWER_CASED);


        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = ((SkyBlockPlayer) sender);

            RedisMessage.sendMessageToProxy("player-count" , "ALL" , (response) ->{
                player.sendMessage("§aTotal number of players in the server: §6" + response);
            });
        });

        command.addSyntax((commandSender, commandContext) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) commandSender;
            ServerType serverType = commandContext.get(serverTypeArgument);

            RedisMessage.sendMessageToProxy("player-count" , serverType.name() , (response) ->{
                player.sendMessage("§aTotal number of players in " + StringUtility.toNormalCase(serverType.name()) + " server: §6" + response);
            });

        } , serverTypeArgument);

    }
}
