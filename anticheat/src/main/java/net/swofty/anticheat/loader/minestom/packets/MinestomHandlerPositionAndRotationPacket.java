package net.swofty.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.packet.PositionAndRotationPacket;
import net.swofty.anticheat.event.packet.SwoftyPacket;
import net.swofty.anticheat.loader.LoaderPacketHandler;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

public class MinestomHandlerPositionAndRotationPacket
        extends LoaderPacketHandler<ClientPlayerPositionAndRotationPacket> {

    @Override
    public SwoftyPacket buildSwoftyPacket(UUID uuid, ClientPlayerPositionAndRotationPacket packet) {
        return new PositionAndRotationPacket(
                SwoftyPlayer.players.get(uuid),
                new Pos(packet.position().x(),
                        packet.position().y(),
                        packet.position().z(),
                        packet.position().yaw(),
                        packet.position().pitch()),
                packet.onGround()
        );
    }

    @Override
    public ClientPlayerPositionAndRotationPacket buildLoaderPacket(UUID uuid, SwoftyPacket packet) {
        return new ClientPlayerPositionAndRotationPacket(
                new net.minestom.server.coordinate.Pos(
                        ((PositionAndRotationPacket) packet).getPos().x(),
                        ((PositionAndRotationPacket) packet).getPos().y(),
                        ((PositionAndRotationPacket) packet).getPos().z(),
                        ((PositionAndRotationPacket) packet).getPos().yaw(),
                        ((PositionAndRotationPacket) packet).getPos().pitch()
                ),
                ((PositionAndRotationPacket) packet).isOnGround(),
                false  // horizontalCollision
        );
    }

    @Override
    public Class<ClientPlayerPositionAndRotationPacket> getHandledPacketClass() {
        return ClientPlayerPositionAndRotationPacket.class;
    }
}
