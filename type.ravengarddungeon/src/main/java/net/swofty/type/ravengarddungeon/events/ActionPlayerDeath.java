package net.swofty.type.ravengarddungeon.events;

import net.minestom.server.event.player.PlayerDeathEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.utility.ScheduleUtility;
import net.swofty.type.ravengarddungeon.TypeRavengardDungeonLoader;
import net.swofty.type.ravengarddungeon.user.RavengardDungeonPlayer;

public class ActionPlayerDeath implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerDeathEvent event) {
        event.setChatMessage(null);
        RavengardDungeonPlayer player = (RavengardDungeonPlayer) event.getPlayer();
        ScheduleUtility.nextTick(() -> TypeRavengardDungeonLoader.getGame().eliminate(player));
    }
}
