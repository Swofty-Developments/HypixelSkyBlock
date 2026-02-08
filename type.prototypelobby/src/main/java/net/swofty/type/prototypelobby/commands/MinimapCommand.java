package net.swofty.type.prototypelobby.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.prototypelobby.minimap.MinimapManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Toggles the minimap display",
        usage = "/minimap",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class MinimapCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            HypixelPlayer player = (HypixelPlayer) sender;
            MinimapManager manager = MinimapManager.getInstance();

            if (manager == null) {
                player.sendMessage("§cMinimap is not available on this server.");
                return;
            }

            manager.toggle(player);

            if (manager.isEnabled(player)) {
                player.sendMessage("§aMinimap enabled.");
            } else {
                player.sendMessage("§cMinimap disabled.");
            }
        });
    }
}
