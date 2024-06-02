package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointStringList;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.warps.TravelScrollIslands;
import net.swofty.types.generic.warps.TravelScrollType;

import java.util.List;

@CommandParameters(description = "Warps to the given destination",
        usage = "/warp <warp>",
        permission = Rank.DEFAULT,
        aliases = "teleportregion",
        allowsConsole = false)
public class WarpCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentString warpArgument = ArgumentType.String("warp");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            final String warp = context.get(warpArgument);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            TravelScrollIslands island = TravelScrollIslands.getFromInternalName(warp);
            if (island != null) {
                List<String> unlockedIslands = player.getDataHandler()
                        .get(DataHandler.Data.VISITED_ISLANDS, DatapointStringList.class)
                        .getValue();
                if (unlockedIslands.contains(warp)) {
                    player.asProxyPlayer().transferToWithIndication(island.getServerType()).thenRun(() -> {
                        player.asProxyPlayer().sendMessage("§7You have been warped to " + island.getDescriptiveName() + "§7!");
                    });
                } else {
                    player.sendMessage("§cYou have not unlocked this warp.");
                }
                return;
            }

            TravelScrollType scroll = TravelScrollType.getFromInternalName(warp);
            if (scroll != null) {
                List<String> unlockedWarps = player.getDataHandler()
                        .get(DataHandler.Data.USED_SCROLLS, DatapointStringList.class)
                        .getValue();
                TravelScrollIslands islandFromScroll = TravelScrollIslands.getFromTravelScroll(scroll);
                if (unlockedWarps.contains(warp)) {
                    player.asProxyPlayer().transferToWithIndication(islandFromScroll.getServerType()).thenRun(() -> {
                        player.asProxyPlayer().sendMessage("§7You have been warped to " + scroll.getDisplayName() + "§7!");
                        player.asProxyPlayer().teleport(scroll.getLocation());
                    });
                } else {
                    player.sendMessage("§cYou have not unlocked this warp.");
                }
                return;
            }

            player.sendMessage("§cCould not find a warp with the name '" + warp + "'.");
        }, warpArgument);
    }
}
