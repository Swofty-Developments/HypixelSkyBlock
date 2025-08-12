package net.swofty.type.skyblockgeneric.event.actions.player;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.ArrayList;
import java.util.Collections;

public class ActionPlayerRemoveTab implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        Player player = event.getPlayer();

        SkyBlockGenericLoader.getLoadedPlayers().forEach(player2 -> {
            Rank player2Rank = player2.getRank();
            Rank playerRank = ((SkyBlockPlayer) player).getRank();

            if (player2.getUuid().equals(player.getUuid())) return;

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