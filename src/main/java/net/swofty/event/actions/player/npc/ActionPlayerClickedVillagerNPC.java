package net.swofty.event.actions.player.npc;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.entity.villager.SkyBlockVillagerNPC;
import net.swofty.entity.villager.VillagerEntityImpl;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.VillagerSpokenToEvent;
import net.swofty.mission.MissionData;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Checks to see if a player clicks on a Villager NPC",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = true)
public class ActionPlayerClickedVillagerNPC extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerEntityInteractEvent playerEvent = (PlayerEntityInteractEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerEvent.getPlayer();

        if (playerEvent.getHand() != Player.Hand.MAIN) return;

        if (playerEvent.getTarget() instanceof VillagerEntityImpl npcImpl) {
            SkyBlockVillagerNPC npc = SkyBlockVillagerNPC.getFromImpl(npcImpl);
            if (npc == null) return;

            VillagerSpokenToEvent spokenToEvent = new VillagerSpokenToEvent(player, npc);
            SkyBlockEvent.callSkyBlockEvent(spokenToEvent);

            if (spokenToEvent.isCancelled()) return;

            npc.onClick(new SkyBlockVillagerNPC.PlayerClickVillagerNPCEvent(
                    player,
                    npc
            ));
        }
    }
}
