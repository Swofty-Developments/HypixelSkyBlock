package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.mission.MissionSet;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "h",
        description = "Sends the player to their hub",
        usage = "/hub",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class HubCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = ((SkyBlockPlayer) sender);

            if (!MissionSet.GETTING_STARTED.hasCompleted(player)) {
                player.sendMessage("§cYou must complete your starting missions!");
                return;
            }

            if (HypixelConst.getTypeLoader().getType() == ServerType.SKYBLOCK_HUB) {
                player.teleport(new Pos(-2.5, 72.5, -69.5, 180, 0));
                return;
            }

            player.sendTo(ServerType.SKYBLOCK_HUB);
        });
    }
}
