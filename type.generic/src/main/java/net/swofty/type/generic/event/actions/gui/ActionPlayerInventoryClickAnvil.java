package net.swofty.type.generic.event.actions.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.type.AnvilInventory;
import net.minestom.server.network.packet.server.play.WindowPropertyPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.HypixelAnvilGUI;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerInventoryClickAnvil implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(InventoryPreClickEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (HypixelAnvilGUI.anvilGUIs.containsKey(player)) {
            // Needed because for some reason if you send this packet too early Client won't register it
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (player.getOpenInventory() != null)
                    player.sendPacket(new WindowPropertyPacket(player.getOpenInventory().getWindowId(), (short) 0, (short) 0));
            }, TaskSchedule.tick(2), TaskSchedule.stop());

            if (event.getInventory() == null) {
                event.setCancelled(true);
                return;
            }
            if (event.getInventory().getClass().equals(AnvilInventory.class)) {
                if (event.getSlot() == 2) {
                    HypixelAnvilGUI.anvilGUIs.get(player).getValue().complete(HypixelAnvilGUI.anvilGUIs.get(player).getKey());
                    HypixelAnvilGUI.anvilGUIs.remove(player);

                    player.closeInventory();
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}

