package net.swofty.types.generic.command.commands;

import net.minestom.server.MinecraftServer;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.mission.MissionSet;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "pc",
        description = "Announces The Player Count of The Server!",
        usage = "/playercount",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class PlayerCountCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = ((SkyBlockPlayer) sender);

            int playerCount = MinecraftServer.getConnectionManager().getOnlinePlayerCount();

            player.sendMessage("§aTotal Number Of Player In this Server: §6" + playerCount);
        });
    }
}
