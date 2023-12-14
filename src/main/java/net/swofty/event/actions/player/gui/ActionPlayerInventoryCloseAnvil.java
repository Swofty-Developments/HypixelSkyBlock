package net.swofty.event.actions.player.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.play.WindowPropertyPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.gui.SkyBlockAnvilGUI;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Handles when a player closes an AnvilGUI",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerInventoryCloseAnvil extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return InventoryCloseEvent.class;
    }

    @Override
    public void run(Event event) {
        InventoryCloseEvent inventoryClose = (InventoryCloseEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) inventoryClose.getPlayer();

        if (SkyBlockAnvilGUI.anvilGUIs.containsKey(player)) {
            if (inventoryClose.getInventory().getInventoryType().equals(InventoryType.ANVIL)) {
                SkyBlockAnvilGUI.anvilGUIs.get(player).getValue().complete(SkyBlockAnvilGUI.anvilGUIs.get(player).getKey());
                SkyBlockAnvilGUI.anvilGUIs.remove(player);
            }
        }
    }
}

