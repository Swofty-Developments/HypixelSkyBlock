package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.scoreboard.TeamBuilder;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.PlayerAssignedTeamEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class PlayerTeamAssignationListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerTeamAssign(PlayerAssignedTeamEvent<BedWarsTeam> event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        player.setTeamName(event.team().getTeamKey());

        TextColor textColor = TextColor.color(event.team().getTeamKey().rgb());
        NamedTextColor color = NamedTextColor.nearestTo(textColor); // possible performance hit?
        player.setTeam(new TeamBuilder(event.team().getName(), MinecraftServer.getTeamManager())
            .prefix(Component.text(event.team().firstLetter(), textColor, TextDecoration.BOLD).appendSpace())
            .teamColor(color)
            .build());
        player.getTeam().sendUpdatePacket();
        event.team().setBedAlive(true); // when adding Swappage gamemode, this might need to be rethought
    }

}
