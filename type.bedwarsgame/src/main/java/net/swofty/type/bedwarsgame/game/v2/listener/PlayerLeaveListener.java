package net.swofty.type.bedwarsgame.game.v2.listener;

import net.swofty.commons.ServerType;
import net.swofty.type.game.game.event.PlayerLeaveGameEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class PlayerLeaveListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerLeave(PlayerLeaveGameEvent event) {
        ((HypixelPlayer) event.player()).sendTo(ServerType.BEDWARS_LOBBY);
    }
}
