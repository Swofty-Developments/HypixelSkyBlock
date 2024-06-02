package net.swofty.types.generic.packet.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.packet.SkyBlockPacketClientListener;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class PacketListenerSignUpdate extends SkyBlockPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientUpdateSignPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player) {
        ClientUpdateSignPacket signPacket = (ClientUpdateSignPacket) packet;

        if (SkyBlockSignGUI.signGUIs.containsKey(player)) {
            SkyBlockSignGUI.SignGUI signGUI = SkyBlockSignGUI.signGUIs.get(player);
            signGUI.future().complete(signPacket.lines().get(0));

            player.sendPacket(
                    new BlockChangePacket(signGUI.pos(), signGUI.block())
            );

            SkyBlockSignGUI.signGUIs.remove(player);
        }
    }

    @Override
    public boolean overrideMinestomProcessing() {
        return true;
    }
}
