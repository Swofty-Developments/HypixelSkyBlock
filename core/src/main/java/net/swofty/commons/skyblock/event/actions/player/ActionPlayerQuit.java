package net.swofty.commons.skyblock.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.commons.skyblock.entity.hologram.PlayerHolograms;
import net.swofty.commons.skyblock.entity.npc.NPCDialogue;
import net.swofty.commons.skyblock.entity.npc.SkyBlockNPC;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.actions.player.fall.ActionPlayerFall;
import net.swofty.commons.skyblock.gui.SkyBlockAnvilGUI;
import net.swofty.commons.skyblock.gui.SkyBlockSignGUI;
import net.swofty.commons.skyblock.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.commons.skyblock.packet.packets.client.anticheat.PacketListenerAirJump;
import net.swofty.commons.skyblock.server.eventcaller.CustomEventCaller;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.SkyBlockScoreboard;
import net.swofty.commons.skyblock.user.categories.CustomGroups;

@EventParameters(description = "Runs on player quit",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
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
