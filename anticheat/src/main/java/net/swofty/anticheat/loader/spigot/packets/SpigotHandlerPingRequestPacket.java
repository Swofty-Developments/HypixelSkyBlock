package net.swofty.anticheat.loader.spigot.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import net.swofty.anticheat.event.packet.RequestPingPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class SpigotHandlerPingRequestPacket extends LoaderPacketHandler {

    @Override
    public Class<? extends SwoftyPacket> getHandledPacketClass() {
        return RequestPingPacket.class;
    }

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, Object loaderPacket) {
        // Not used for outgoing packets in this case
        return null;
    }

    @Override
    public Object buildLoaderPacket(UUID uuid, SwoftyPacket swoftyPacket) {
        if (!(swoftyPacket instanceof RequestPingPacket pingPacket)) return null;

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.PING);
        packet.getIntegers().write(0, pingPacket.getRequestId());

        return packet;
    }
}
