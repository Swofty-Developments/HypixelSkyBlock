package net.swofty.type.generic.packet.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.swofty.type.generic.gui.HypixelSignGUI;
import net.swofty.type.generic.packet.HypixelPacketClientListener;
import net.swofty.type.generic.user.HypixelPlayer;

public class PacketListenerSignUpdate extends HypixelPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientUpdateSignPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, HypixelPlayer player) {
        ClientUpdateSignPacket signPacket = (ClientUpdateSignPacket) packet;

        if (HypixelSignGUI.signGUIs.containsKey(player)) {
            HypixelSignGUI.SignGUI signGUI = HypixelSignGUI.signGUIs.get(player);
            signGUI.future().complete(signPacket.lines().getFirst());

            player.sendPacket(
                    new BlockChangePacket(signGUI.pos(), signGUI.block())
            );

            HypixelSignGUI.signGUIs.remove(player);
        }
    }

    @Override
    public boolean overrideMinestomProcessing() {
        return true;
    }
}
