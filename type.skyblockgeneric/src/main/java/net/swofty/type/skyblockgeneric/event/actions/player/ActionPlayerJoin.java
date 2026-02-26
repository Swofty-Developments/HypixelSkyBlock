package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;

public class ActionPlayerJoin implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onPlayerJoin(PlayerSpawnEvent event) {
        int hour = SkyBlockCalendar.getHour();
        long minecraftTime = (hour * 1000L + 6000) % 24000;
        event.getPlayer().getInstance().setTime(minecraftTime);
    }

}
