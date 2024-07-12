package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.common.ClientPingRequestPacket;
import net.minestom.server.network.packet.client.common.ClientPongPacket;
import net.minestom.server.network.packet.server.common.PingResponsePacket;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.RequestPingPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerPingResponsePacket
        extends LoaderPacketHandler<ClientPongPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientPongPacket packet) {
        return new net.swofty.anticheat.event.packet.PingResponsePacket(
                SwoftyPlayer.players.get(uuid),
                packet.id()
        );
    }

    @Override
    public ClientPongPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return new ClientPongPacket(
                ((RequestPingPacket) packet).getRequestId()
        );
    }

    @Override
    public Class<ClientPongPacket> getHandledPacketClass() {
        return ClientPongPacket.class;
    }
}
