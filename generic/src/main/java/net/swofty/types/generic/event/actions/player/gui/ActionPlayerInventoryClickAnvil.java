package net.swofty.types.generic.event.actions.player.gui;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.play.WindowPropertyPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.gui.SkyBlockAnvilGUI;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionPlayerInventoryClickAnvil implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(InventoryPreClickEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (SkyBlockAnvilGUI.anvilGUIs.containsKey(player)) {
            // Needed because for some reason if you send this packet too early Client won't register it
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (player.getOpenInventory() != null)
                    player.sendPacket(new WindowPropertyPacket(player.getOpenInventory().getWindowId(), (short) 0, (short) 0));
            }, TaskSchedule.tick(2), TaskSchedule.stop());

            if (event.getInventory() == null) {
                event.setCancelled(true);
                return;
            }
            if (event.getInventory().getInventoryType().equals(InventoryType.ANVIL)) {
                if (event.getSlot() == 2) {
                    SkyBlockAnvilGUI.anvilGUIs.get(player).getValue().complete(SkyBlockAnvilGUI.anvilGUIs.get(player).getKey());
                    SkyBlockAnvilGUI.anvilGUIs.remove(player);

                    player.closeInventory();
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}

