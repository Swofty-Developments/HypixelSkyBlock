package net.swofty.type.skyblockgeneric.event.actions.player;


import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIViewPlayerProfile;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerRightClickOnPlayer implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof SkyBlockPlayer skyBlockPlayer) {
            new GUIViewPlayerProfile(skyBlockPlayer).open(player);
        }
    }
}
