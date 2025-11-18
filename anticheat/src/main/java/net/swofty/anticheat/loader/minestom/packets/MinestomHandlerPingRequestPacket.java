package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.server.common.PingPacket;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.RequestPingPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerPingRequestPacket
        extends LoaderPacketHandler<PingPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, PingPacket packet) {
        // This method is not used for outgoing server packets
        return null;
    }

    @Override
    public PingPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return new PingPacket(
                (int) ((RequestPingPacket) packet).getRequestId()
        );
    }

    @Override
    public Class<PingPacket> getHandledPacketClass() {
        return PingPacket.class;
    }
}
