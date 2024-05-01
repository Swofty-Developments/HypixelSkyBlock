package net.swofty.types.generic.event.actions.player.npc;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.entity.villager.VillagerEntityImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.VillagerSpokenToEvent;

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
        if (SkyBlockConst.isIslandServer()) return;

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
