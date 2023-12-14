package net.swofty.packet.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientClickWindowPacket;
import net.minestom.server.network.packet.client.play.ClientCloseWindowPacket;
import net.minestom.server.network.packet.server.play.CloseWindowPacket;
import net.swofty.gui.SkyBlockAnvilGUI;
import net.swofty.packet.SkyBlockPacketClientListener;
import net.swofty.user.SkyBlockPlayer;

public class PacketListenerWindowClose extends SkyBlockPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientCloseWindowPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player) {
        ClientCloseWindowPacket windowPacket = (ClientCloseWindowPacket) packet;

        if (windowPacket.windowId() == 99) {
            if (SkyBlockAnvilGUI.anvilGUIs.containsKey(player)) {
                SkyBlockAnvilGUI.anvilGUIs.get(player).getValue().complete("");

                SkyBlockAnvilGUI.anvilGUIs.remove(player);
                SkyBlockAnvilGUI.anvilValues.remove(player);
            }
        }
    }
}
