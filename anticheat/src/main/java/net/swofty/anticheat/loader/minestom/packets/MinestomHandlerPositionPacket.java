package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.PositionPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerPositionPacket
        extends LoaderPacketHandler<ClientPlayerPositionPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientPlayerPositionPacket packet) {
        return new PositionPacket(
                SwoftyPlayer.players.get(uuid),
                packet.position().x(),
                packet.position().y(),
                packet.position().z(),
                packet.onGround()
        );
    }

    @Override
    public ClientPlayerPositionPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return new ClientPlayerPositionPacket(
                new net.minestom.server.coordinate.Pos(
                        ((PositionPacket) packet).getX(),
                        ((PositionPacket) packet).getY(),
                        ((PositionPacket) packet).getZ()
                ),
                (byte) (((PositionPacket) packet).isOnGround() ? 1 : 0)
        );
    }

    @Override
    public Class<ClientPlayerPositionPacket> getHandledPacketClass() {
        return ClientPlayerPositionPacket.class;
    }
}
