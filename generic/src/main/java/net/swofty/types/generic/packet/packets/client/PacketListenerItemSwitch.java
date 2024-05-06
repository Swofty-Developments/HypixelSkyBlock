package net.swofty.types.generic.packet.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientHeldItemChangePacket;
import net.minestom.server.network.packet.client.play.ClientNameItemPacket;
import net.minestom.server.network.packet.server.play.WindowPropertyPacket;
import net.swofty.types.generic.gui.SkyBlockAnvilGUI;
import net.swofty.types.generic.packet.SkyBlockPacketClientListener;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Map;

public class PacketListenerItemSwitch extends SkyBlockPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientHeldItemChangePacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, SkyBlockPlayer player) {
        if (player.getOpenInventory() != null) {
            player.closeInventory();
            player.sendMessage("§cYou can't switch items while in an inventory!");
        }
    }

    @Override
    public boolean overrideMinestomProcessing() {
        return false;
    }
}
