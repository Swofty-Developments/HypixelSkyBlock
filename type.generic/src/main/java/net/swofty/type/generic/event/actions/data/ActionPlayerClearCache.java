package net.swofty.type.generic.event.actions.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;

import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.HypixelAnvilGUI;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.CustomGroups;

public class ActionPlayerClearCache implements HypixelEventClass {

    @SneakyThrows
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerDisconnectEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        // Remove from caches
        CustomGroups.staffMembers.remove(player);
        HypixelNPC.getPerPlayerNPCs().remove(player.getUuid());
        if (HypixelSignGUI.signGUIs.containsKey(player)) {
            HypixelSignGUI.signGUIs.get(player).future().complete(null);
            HypixelSignGUI.signGUIs.remove(player);
        }
        if (HypixelAnvilGUI.anvilGUIs.containsKey(player)) {
            HypixelAnvilGUI.anvilGUIs.get(player).getValue().complete(null);
            HypixelAnvilGUI.anvilGUIs.remove(player);
        }
        if (HypixelInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            HypixelInventoryGUI.GUI_MAP.get(player.getUuid()).suddenlyQuit(
                    HypixelInventoryGUI.GUI_MAP.get(player.getUuid()).getInventory(),
                    player);
            HypixelInventoryGUI.GUI_MAP.remove(player.getUuid());
        }
        HypixelNPC.removeDialogueCache(player);
        PlayerHolograms.remove(player);

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