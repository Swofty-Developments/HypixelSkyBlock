package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.common.ClientPingRequestPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.server.common.PingPacket;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.PositionAndRotationPacket;
import net.swofty.anticheat.event.packet.RequestPingPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

public class MinestomHandlerPingRequestPacket
        extends LoaderPacketHandler<PingPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, PingPacket packet) {
        return new RequestPingPacket(
                SwoftyPlayer.players.get(uuid),
                packet.id()
        );
    }

    @Override
    public PingPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return new PingPacket(
                ((RequestPingPacket) packet).getRequestId()
        );
    }

    @Override
    public Class<PingPacket> getHandledPacketClass() {
        return PingPacket.class;
    }
}
