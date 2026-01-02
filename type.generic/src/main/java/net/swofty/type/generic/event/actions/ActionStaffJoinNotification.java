package net.swofty.type.generic.event.actions;

import net.minestom.server.event.player.PlayerSpawnEvent;
import net.swofty.type.generic.chat.StaffChat;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionStaffJoinNotification implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        if (!player.getRank().isStaff()) return;

        StaffChat.sendNotification(player.getFullDisplayName() + " §7joined.");
    }
}


