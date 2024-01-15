package net.swofty.types.generic.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.gui.SkyBlockAnvilGUI;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.packet.packets.client.anticheat.PacketListenerAirJump;
import net.swofty.types.generic.server.eventcaller.CustomEventCaller;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.SkyBlockScoreboard;
import net.swofty.types.generic.user.categories.CustomGroups;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.actions.player.fall.ActionPlayerFall;

@EventParameters(description = "Runs on player quit",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerQuit extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerDisconnectEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerDisconnectEvent playerDisconnectEvent = (PlayerDisconnectEvent) event;

        final SkyBlockPlayer player = (SkyBlockPlayer) playerDisconnectEvent.getPlayer();

        /*
        Remove from caches
         */
        CustomGroups.staffMembers.remove(player);
        SkyBlockScoreboard.removeCache(player);
        ActionPlayerFall.fallHeight.remove(player);
        SkyBlockNPC.getNpcs().forEach((npc, entity) -> {
            entity.clearCache(player);
        });
        if (SkyBlockSignGUI.signGUIs.containsKey(player)) {
            SkyBlockSignGUI.signGUIs.get(player).future().complete(null);
            SkyBlockSignGUI.signGUIs.remove(player);
        }
        if (SkyBlockAnvilGUI.anvilGUIs.containsKey(player)) {
            SkyBlockAnvilGUI.anvilGUIs.get(player).getValue().complete(null);
            SkyBlockAnvilGUI.anvilGUIs.remove(player);
        }
        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid()).suddenlyQuit(
                    SkyBlockInventoryGUI.GUI_MAP.get(player.getUuid()).getInventory(),
                    player);
            SkyBlockInventoryGUI.GUI_MAP.remove(player.getUuid());
        }
        PacketListenerAirJump.playerData.remove(player);
        CustomEventCaller.clearCache(player);
        NPCDialogue.remove(player);
        PlayerHolograms.remove(player);
    }
}
