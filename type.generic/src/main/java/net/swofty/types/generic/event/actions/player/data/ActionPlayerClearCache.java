package net.swofty.types.generic.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.types.generic.entity.animalnpc.SkyBlockAnimalNPC;
import net.swofty.types.generic.entity.hologram.PlayerHolograms;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.SkyBlockNPC;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.actions.player.ActionPlayerStrayTooFar;
import net.swofty.types.generic.event.actions.player.fall.ActionPlayerFall;
import net.swofty.types.generic.gui.SkyBlockAnvilGUI;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.packet.packets.client.anticheat.PacketListenerAirJump;
import net.swofty.types.generic.server.eventcaller.CustomEventCaller;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.SkyBlockScoreboard;
import net.swofty.types.generic.user.categories.CustomGroups;

public class ActionPlayerClearCache implements SkyBlockEventClass {

    @SneakyThrows
    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!player.hasAuthenticated) return;

        /*
        Remove from caches
         */
        CustomGroups.staffMembers.remove(player);
        SkyBlockScoreboard.removeCache(player);
        ActionPlayerFall.fallHeight.remove(player);
        player.getPetData().updatePetEntityImpl(null);
        SkyBlockNPC.getPerPlayerNPCs().remove(player.getUuid());
        SkyBlockAnimalNPC.getAnimalNPCs().forEach((npc, entity) -> {
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
        PlayerItemOrigin.clearCache(player.getUuid());
        PacketListenerAirJump.playerData.remove(player);
        CustomEventCaller.clearCache(player);
        NPCDialogue.remove(player);
        PlayerHolograms.remove(player);
        ActionPlayerStrayTooFar.startedStray.remove(player.getUuid());

        // Remove external player holograms associated with the player
        PlayerHolograms.externalPlayerHolograms.entrySet().removeIf(entry -> {
            PlayerHolograms.ExternalPlayerHologram hologram = entry.getKey();
            if (hologram.getPlayer().equals(player)) {
                PlayerHolograms.removeExternalPlayerHologram(hologram);
                return true;
            }
            return false;
        });
    }
}