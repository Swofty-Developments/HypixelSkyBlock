package net.swofty.type.bedwarsgame.game.v2.listener;

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
    }

}
