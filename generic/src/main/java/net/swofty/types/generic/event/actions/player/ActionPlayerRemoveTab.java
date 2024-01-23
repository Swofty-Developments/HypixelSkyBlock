package net.swofty.types.generic.event.actions.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

import java.util.ArrayList;
import java.util.Collections;

@EventParameters(description = "Runs on player join",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerRemoveTab extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSpawnEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerSpawnEvent event = (PlayerSpawnEvent) tempEvent;
        Player player = event.getPlayer();

        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player2 -> {
            Rank player2Rank = ((SkyBlockPlayer) player2).getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue();
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