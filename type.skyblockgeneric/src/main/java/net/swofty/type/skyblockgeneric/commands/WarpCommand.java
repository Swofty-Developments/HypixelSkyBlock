package net.swofty.type.skyblockgeneric.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.warps.TravelScrollIslands;
import net.swofty.type.skyblockgeneric.warps.TravelScrollType;

import java.util.List;

@CommandParameters(description = "Warps to the given destination",
        usage = "/warp <warp>",
        permission = Rank.DEFAULT,
        aliases = "teleportregion",
        allowsConsole = false)
public class WarpCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString warpArgument = ArgumentType.String("warp");
        warpArgument.setSuggestionCallback(((sender, context, suggestion) -> {
            for (TravelScrollIslands island : TravelScrollIslands.values()) {
                suggestion.addEntry(new SuggestionEntry(island.getInternalName(), Component.text(island.getDescriptiveName())));
            }
        }));

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            final String warp = context.get(warpArgument);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            TravelScrollIslands island = TravelScrollIslands.getFromInternalName(warp);
            if (island != null) {
                List<String> unlockedIslands = player.getSkyblockDataHandler()
                        .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class)
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
                List<String> unlockedWarps = player.getSkyblockDataHandler()
                        .get(SkyBlockDataHandler.Data.USED_SCROLLS, DatapointStringList.class)
                        .getValue();
                TravelScrollIslands islandFromScroll = TravelScrollIslands.getFromTravelScroll(scroll);
                if (unlockedWarps.contains(warp)) {
                    ServerType serverType = islandFromScroll.getServerType();

                    if (HypixelConst.getTypeLoader().getType() == serverType) {
                        player.asProxyPlayer().sendMessage("§7You have been warped to " + scroll.getDisplayName() + "§7!");
                        player.teleport(scroll.getLocation());
                        return;
                    }

                    player.asProxyPlayer().transferToWithIndication(serverType).thenRun(() -> {
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
