package net.swofty.packet.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientClickWindowButtonPacket;
import net.minestom.server.network.packet.client.play.ClientClickWindowPacket;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.network.packet.server.play.CloseWindowPacket;
import net.swofty.gui.SkyBlockAnvilGUI;
import net.swofty.gui.SkyBlockSignGUI;
import net.swofty.packet.SkyBlockPacketClientListener;
import net.swofty.user.SkyBlockPlayer;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PacketListenerWindowClick extends SkyBlockPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientClickWindowPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player) {
        ClientClickWindowPacket windowPacket = (ClientClickWindowPacket) packet;

        if (windowPacket.windowId() == 99 & windowPacket.slot() != -999) {
            if (SkyBlockAnvilGUI.anvilGUIs.containsKey(player)) {
                if (windowPacket.clickedItem().getDisplayName() != null) {
                    Map.Entry<String, CompletableFuture<String>> entry = SkyBlockAnvilGUI.anvilGUIs.get(player);

                    player.sendPacket(
                            new CloseWindowPacket((byte) 99)
                    );

                    event.setCancelled(true);

                    new SkyBlockAnvilGUI(player).open(SkyBlockAnvilGUI.anvilGUIs.get(player).getKey());
                    SkyBlockAnvilGUI.anvilGUIs.put(player, entry);
                } else if (windowPacket.slot() == 2) {
                    SkyBlockAnvilGUI.anvilGUIs.get(player).getValue().complete(SkyBlockAnvilGUI.anvilValues.get(player));
                    SkyBlockAnvilGUI.anvilGUIs.remove(player);
                    SkyBlockAnvilGUI.anvilValues.remove(player);

                    player.sendPacket(
                            new CloseWindowPacket((byte) 99)
                    );
                }
            }
        }
    }
}
