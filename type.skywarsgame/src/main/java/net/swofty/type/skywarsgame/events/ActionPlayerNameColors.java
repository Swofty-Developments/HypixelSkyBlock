package net.swofty.type.skywarsgame.events;

import net.kyori.adventure.text.Component;
import net.minestom.server.color.TeamColor;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.ArrayList;
import java.util.Collections;

public class ActionPlayerNameColors implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerSpawnEvent event) {
        SkywarsPlayer player = (SkywarsPlayer) event.getPlayer();

        TypeSkywarsGameLoader.getGames().forEach(game -> {
            if (!game.getPlayers().contains(player)) return;

            for (SkywarsPlayer other : game.getPlayers()) {
                boolean isSelf = player.getUuid().equals(other.getUuid());
                TeamColor color = isSelf ? TeamColor.GREEN : TeamColor.RED;

                player.sendPacket(new TeamsPacket(
                        "SW_" + other.getUsername(),
                        new TeamsPacket.CreateTeamAction(
                                new TeamsPacket.Settings(
                                        Component.empty(),
                                        Component.empty(),
                                        Component.empty(),
                                        TeamsPacket.NameTagVisibility.ALWAYS,
                                        TeamsPacket.CollisionRule.ALWAYS,
                                        color,
                                        (byte) 0x01
                                ),
                                new ArrayList<>(Collections.singletonList(other.getUsername()))
                        )
                ));

                if (!isSelf) {
                    other.sendPacket(new TeamsPacket(
                            "SW_" + player.getUsername(),
                            new TeamsPacket.CreateTeamAction(
                                    new TeamsPacket.Settings(
                                            Component.empty(),
                                            Component.empty(),
                                            Component.empty(),
                                            TeamsPacket.NameTagVisibility.ALWAYS,
                                            TeamsPacket.CollisionRule.ALWAYS,
                                            TeamColor.RED,
                                            (byte) 0x01
                                    ),
                                    new ArrayList<>(Collections.singletonList(player.getUsername()))
                            )
                    ));
                }
            }
        });
    }
}
