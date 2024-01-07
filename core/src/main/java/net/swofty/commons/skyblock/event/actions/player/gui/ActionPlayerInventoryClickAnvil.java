package net.swofty.commons.skyblock.event.actions.player.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.play.WindowPropertyPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.gui.SkyBlockAnvilGUI;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Handles when a player clicks on an AnvilGUI",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
public class ActionPlayerInventoryClickAnvil extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return InventoryPreClickEvent.class;
    }

    @Override
    public void run(Event event) {
        InventoryPreClickEvent inventoryClick = (InventoryPreClickEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) inventoryClick.getPlayer();

        if (SkyBlockAnvilGUI.anvilGUIs.containsKey(player)) {
            // Needed because for some reason if you send this packet too early Client won't register it
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (player.getOpenInventory() != null)
                    player.sendPacket(new WindowPropertyPacket(player.getOpenInventory().getWindowId(), (short) 0, (short) 0));
            }, TaskSchedule.tick(2), TaskSchedule.stop());

            if (inventoryClick.getInventory() == null) {
                inventoryClick.setCancelled(true);
                return;
            }
            if (inventoryClick.getInventory().getInventoryType().equals(InventoryType.ANVIL)) {
                if (inventoryClick.getSlot() == 2) {
                    SkyBlockAnvilGUI.anvilGUIs.get(player).getValue().complete(SkyBlockAnvilGUI.anvilGUIs.get(player).getKey());
                    SkyBlockAnvilGUI.anvilGUIs.remove(player);

                    player.closeInventory();
                } else {
                    inventoryClick.setCancelled(true);
                }
            }
        }
    }
}

