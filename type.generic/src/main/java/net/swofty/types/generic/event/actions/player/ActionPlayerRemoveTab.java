package net.swofty.types.generic.event.actions.player;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.ArrayList;
import java.util.Collections;

public class ActionPlayerRemoveTab implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        Player player = event.getPlayer();

        SkyBlockGenericLoader.getLoadedPlayers().forEach(player2 -> {
            Rank player2Rank = player2.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue();
            Rank playerRank = ((SkyBlockPlayer) player).getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue();

            player2.sendPacket(new TeamsPacket("ZZZZZ" + player.getUsername(), new TeamsPacket.CreateTeamAction(
                    Component.text(playerRank.getPrefix()),
                    (byte) 0x01,
                    TeamsPacket.NameTagVisibility.ALWAYS,
                    TeamsPacket.CollisionRule.ALWAYS,
                    playerRank.getTextColor(),
                    Component.text(playerRank.getPrefix()),
                    Component.empty(),
                    new ArrayList<>(Collections.singletonList(player.getUsername()))
            )));
            player.sendPacket(new TeamsPacket("ZZZZZ" + player2.getUsername(), new TeamsPacket.CreateTeamAction(
                    Component.text(player2Rank.getPrefix()),
                    (byte) 0x01,
                    TeamsPacket.NameTagVisibility.ALWAYS,
                    TeamsPacket.CollisionRule.ALWAYS,
                    player2Rank.getTextColor(),
                    Component.text(player2Rank.getPrefix()),
                    Component.empty(),
                    new ArrayList<>(Collections.singletonList(player2.getUsername()))
            )));
        });
    }
}