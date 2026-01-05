package net.swofty.type.skywarsgame.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.ArrayList;
import java.util.Collections;

public class ActionPlayerNameColors implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        SkywarsPlayer player = (SkywarsPlayer) event.getPlayer();

        TypeSkywarsGameLoader.getGames().forEach(game -> {
            if (!game.getPlayers().contains(player)) return;

            for (SkywarsPlayer other : game.getPlayers()) {
                boolean isSelf = player.getUuid().equals(other.getUuid());
                NamedTextColor color = isSelf ? NamedTextColor.GREEN : NamedTextColor.RED;

                player.sendPacket(new TeamsPacket(
                    "SW_" + other.getUsername(),
                    new TeamsPacket.CreateTeamAction(
                        Component.empty(),
                        (byte) 0x01,
                        TeamsPacket.NameTagVisibility.ALWAYS,
                        TeamsPacket.CollisionRule.ALWAYS,
                        color,
                        Component.empty(),
                        Component.empty(),
                        new ArrayList<>(Collections.singletonList(other.getUsername()))
                    )
                ));

                if (!isSelf) {
                    other.sendPacket(new TeamsPacket(
                        "SW_" + player.getUsername(),
                        new TeamsPacket.CreateTeamAction(
                            Component.empty(),
                            (byte) 0x01,
                            TeamsPacket.NameTagVisibility.ALWAYS,
                            TeamsPacket.CollisionRule.ALWAYS,
                            NamedTextColor.RED,
                            Component.empty(),
                            Component.empty(),
                            new ArrayList<>(Collections.singletonList(player.getUsername()))
                        )
                    ));
                }
            }
        });
    }
}
