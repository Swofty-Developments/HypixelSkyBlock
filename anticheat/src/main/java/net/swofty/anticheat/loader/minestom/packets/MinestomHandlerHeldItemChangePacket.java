package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientHeldItemChangePacket;
import net.swofty.anticheat.event.packet.HeldItemChangePacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerHeldItemChangePacket
        extends LoaderPacketHandler<ClientHeldItemChangePacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientHeldItemChangePacket packet) {
        return new HeldItemChangePacket(uuid, packet.slot());
    }

    @Override
    public ClientHeldItemChangePacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return null;
    }

    @Override
    public Class<ClientHeldItemChangePacket> getHandledPacketClass() {
        return ClientHeldItemChangePacket.class;
    }
}
